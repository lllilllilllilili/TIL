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