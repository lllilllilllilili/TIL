- FutureTask<O> task = new FutureTask<>(Callable<V>)
    - Thread 가 수행하는 Task로서 Callable 을 실행시키고 결과를 Future<V>에 담아 반환
    - Runnable 은 스레드를 실행하고 코드를 실행할 수 있는 인터페이스를 제공하며, 반환 값을 가질 수 없다. 반면에 Callable 은 스레드를 실행하고 코드를 실행하면서 반환 값을 가질 수 있다. 

- 스프링 배치 멀티 스레드 프로세싱
    - Step 안에 ItemProcessor 가 비동기적으로 동작하는 구조 
    - AsyncItemProcessor 와 AsyncItemWriter 가 함께 구성이 되어야 함

- 기본 처리 방식은 아래와 같다. 

## Job
- Step
    - ItemReader
    - AsyncItemProcessor -> ItemProcessor
    - AsyncItemWriter -> ItemWriter 
(-> 는 delegate 의미)

- AsyncItemProcessor 는 ItemProcessor 에 위임하게 되는데 내부적으로 TaskExecutor 를 가지고 있다. (new SyncTaskExecutor() 이거는 Thread 생성하고 Task를 할당해주는 역할을 하는듯) 그리고나서 Thread가 수행하는 Task로 Callable을 실행시키고 결과를 Future<V> 에 담에서 반환하게 된다. 

```java
    //아래 순서대로 실행되는듯 하다. 
    AsyncItemProcessor
    ItemProcessor<I, O> delegate;
    TaskExecutor taskExecutor = new SyncTaskExecutor(); 
    FutureTask<O> task = new FutureTask<>(Callable<V>)
```

## 1차 요약
- taskExecutor 에 execute 가 실행이 되면 FutureTask<O> task = new FutureTask<>(Callable<V>) 에 Callable 이 실행이 되는데 Callable 은 ItemProcessor 를 실행시킨다. 

```java
@Nullable
public Future<0> process (final I item) throws Exception {
    final StepExecution stepExecution = getStepExecution(); 
    FutureTask<0> task = new FutureTask<>(new Callable<0>() { 
        public 0 call() throws Exception {
            if (stepExecution != null) {
                StepSynchronizationManager.register(stepExecution);
            }
            try {
                return delegate.process(item); //이게 마지막에 동작하나 보다. 
            }
            finally {
            if (stepExecution != null) {
                StepSynchronizationManager.close();
            }
            });
        }
    taskExecutor.execute(task);
    return task;
```

```java
public Step step() throws Execption {
        return stepBuilderFactory.get("step")
                                 .chunk(100)
                                 .reader(pagingItemReader())
                                 .processor(asyncItemProceesor())
                                 .writer(asyncItemWriter())
                                 .build()
}
```

## 스프링 배치 멀티 스레드 프로세싱
- Step 내에서 멀티 스레드로 Chunk 기반 처리가 이루어지는 구조 
- TaskExecutorRepeatTemplate 이 반복자로 사용되며 설정한 개수(throttleLimit) 개수 만큼 스레드 생성
- Job > Step > TaskExecutorRepeatTemplate 
    - TaskExecutorRepeatTemplate (멀티스레드)
        - Runnable -> RepeatCallback -> ChunkOrientedTasklet 
    

## 공통 처리
- ChunkOrientedTasklet 
    - ItemReader -> ItemProceesor -> ItemWriter
    - 위 과정이 모두 thread-safe 하다.  

## TaskExecutorRepeatTemplate
- 조절 제한 개수
    - int throttleLimit = DEFAULT_THROTTLE_LIMIT 
- Thread 를 조절 제한 수 만큼 생성하고 Task를 할당
    - TaskExecutor taskExecutor = new SyncTaskExecutor(); 
    - 단 위와 같은 방식은 동기적으로 처리된다. 

## Multi-threaded Step
- Job -> taskletStep -> TaskExecutorRepeatTemplate -> TaskExecutor -> 스레드 생성 -> Runnable 안에 ChunkOrientedTasklet 이 있음 -> ItemReader - ItemProcessor - ItemWriter 가 동작

## 주의
- ItemReader 는 DB에서 데이터를 읽어올텐데 데이터 동기화가 필요하니 각 스레드 별로 thread-safe 가 필요함, 데이터 중복 등 문제 발생, 청크는 동시성 이슈 때문에 스레드 별로 하나씩 갖는다. 
- ItemReader에서는 Synchronize 로 동시성 이슈가 없도록 한다. 
- 각각의 스레드는 스택을 가지는데 청크를 담는다. 공유가 되지 않는다. 

## 스레드에 안전한 페이징 제공
- jdbcPagingItemReader
- jpaPagingItemReader
```java
public Step step() throws Exception {
        return stepBuilderFactory.get("step")
                                 .<Customer, Customer>chunk(100)
                                 .reader(pagingItemReader())
                                 .processor(customerItemProcessor())
                                 .writer(customerItemWriter())
                                 .taskExecutor(taskExecutor())
                                 .build()
}
```
- 스레드 생성 및 실행을 위한 taskExecutor 설정
- 자바에서 스레드 풀 관리할 수 있는 ThreadPoolTaskExecutor 
- 이런식으로 병렬 처리 할 수 있따.
```java
@Bean
public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(4);
    taskExecutor.setMaxPoolSize(8);
    taskExecutor.setThreadNamePrefix("async-thread");
    return taskExecutor;
}
```
- SimpleChunkProvider 클래스에서 provide 메소드는 Chunk<T> inputs = new Chunk<>() 로 Chunk 를 스레드 별로 생성하게 된다.

## Cursor
- Cursor 는 Thread 동기화를 위한 작업이 없다. 중복된 데이터를 읽어올 수 있다. 