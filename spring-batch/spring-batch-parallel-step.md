## Parallel Steps
- SplitState를 여러 개의 Flow 들을 병렬적으로 실행하는 구조 
- 실행이 다 완료된 후 FlowExecutionStatus 결과들을 취합해서 다음 단계를 결정한다. 
- Job > SimpleFlow > SplitState (flows) > TaskExecutor -> 스레드 Worker (Future Task > SimpleFlow) > FlowExecution 실행 결과 > Collection<FlowExecution> > aggregator.aggregate(results) > FlowExecutionAggregator 반환하는거 같은데 → SimpleFlow 
- (return FlowExecutionStatus : COMPLETE, STOPPED, UNKNOWN 최종 실행결과의 상태값을 반환하여 다음 Step을 결정하도록 한다.)

## SplitState
- 병렬로 수행할 Flow 들을 담은 컬렉션 
    - Collection<Flow> flows;
- Thread 생성하고 Task를 할당
    - TaskExecutor taskExecutor;
- 병렬로 수행 후 하나의 종료 상태로 집계하는 클래스
    - FlowExecutionAggregator aggregator
- FlowExecutionStatus handle(final FlowExecutor executor)

## Parallel Steps
```java
public Job job() {
    return jobBuilderFactory.get("job")
                            .start(flow1())
                            .split(TaskExecutor).add(flow2(), flow3())
                            .next(flow4())
                            .end()
                            .build();
}
```
- taskExecutor 에서 flow 개수만큼 스레드를 생성해서 각 flow를 실행시킨다.
- next는 메인스레드가 실행시킨다. 

## 동시성 이슈 해결 
- 메인 스레드 말고 병렬 스레드 이용 시 split(flow).add(flow2) 이런식으로 처리가 된다고 하면 전역 변수가 공유되고 있으면 동시성 이슈가 발생할 수 있다. 
- lock 사용 (성능은 조금 떨어지겠지만)
```java
private Object lock = new Object();
...
synchronized(lock) {
    //전역변수 작업 라인
}
 
```