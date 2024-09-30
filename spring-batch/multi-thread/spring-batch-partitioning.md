## spring batch 
- masterstep 이 slavestep 을 실행시키는 구조 
- slavestep은 독립적으로 실행 
- slavestep은 stepExecution 파라미터 환경 구성
- slavestep은 다음으로 구성
    - ItemReader
    - ItemProcessor
    - ItemWriter
- masterstep은 partitionStep 이다.
- slavestep은 taskletstep, flowstep 이 올 수 있다.  

## partitioning
- partitionStep
    - 파티셔닝 기능을 수행하는 Step 구현체
- PartitionHandler
    - PartitionStep 에 의해 호출되며 스레드를 생성해서 WorkStep을 병렬로 실행한다. 
- StepExecutionSplitter
    - WorkStep에서 사용할 StepExecution 을 gridSize 만큼 생성한다. 
- Partitioner
    - StepExecution 에 매핑 할 ExecutionContext 를 gridSize 만큼 생성한다.  