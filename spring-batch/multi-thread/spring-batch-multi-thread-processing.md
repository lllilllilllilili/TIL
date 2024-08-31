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