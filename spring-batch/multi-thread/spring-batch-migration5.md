**Spring Batch 5.0 Migration Guide** 

## @EnableBatchProcessing
- @EnableBatchProcessing는 Spring Batch가 트랜잭션 관리를 위해 사용할 수 있는 트랜잭션 관리자 빈이 애플리케이션 컨텍스트에 등록되었고, 이를 다른 빈이나 서비스가 사용할 수 있게 해주었다. 트랜잭션 관리자의 무조건적인 노출은 사용자 정의 트랜잭션 관리자와 간섭할 수 있다. 
    - (문제) https://github.com/spring-projects/spring-batch/issues/816 
    - StepBuilderHelper#transactionManager(PlatformTransactionManager)한 단계 아래로 이동되었다. 
    - 태스크릿 단계에만 필요

- https://github.com/spring-projects/spring-batch/wiki/Spring-Batch-5.0-Migration-Guide
- 빌더 팩토리 사용 중단 이슈 :
    - https://github.com/spring-projects/spring-batch/issues/4188
- TransactionManager
    - `PlatformTransactionManager`는 Spring이 제공하는 트랜잭션 관리 인터페이스로, 일반적으로 데이터베이스와 관련된 트랜잭션을 관리합니다.
    - `JobConfig` 또는 `BatchConfig`에서 주입받는 `PlatformTransactionManager`는 보통 Spring이 관리하는 `DataSourceTransactionManager`나 `JpaTransactionManager`와 같은 구체적인 구현체를 사용합니다. 이 구현체들은 애플리케이션에서 설정된 데이터베이스 연결(즉, `DataSource`나 `EntityManagerFactory`)을 기반으로 트랜잭션을 관리합니다.
    
    ```jsx
    // Sample with v4
    @Configuration
    @EnableBatchProcessing
    public class MyStepConfig {
    
        @Autowired
        private StepBuilderFactory stepBuilderFactory;
    
        @Bean
        public Step myStep() {
            return this.stepBuilderFactory.get("myStep")
                    .tasklet(..) // or .chunk()
                    .build();
        }
    
    // Sample with v5
    @Configuration
    @EnableBatchProcessing
    public class MyStepConfig {
    
        @Bean
        public Tasklet myTasklet() {
           return new MyTasklet();
        }
    
        @Bean
        public Step myStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
            return new StepBuilder("myStep", jobRepository)
                    .tasklet(myTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
                    .build();
        }
    
    }
    ```
    

개요

Spring Batch 5.0부터 `JobBuilderFactory`와 같은 일부 팩토리 클래스들이 deprecated 되었고, 대신 직접 빌더를 생성하는 방식으로 변경

- 팩토리 메서드는 보통 매개변수가 숨겨져 있는 경우가 많아 코드의 동작을 이해하는 데에 다소 어려움 해결
- 

fade-out 함수

- JobBuilderFactory → JobBuilder
    - Class JobBuilder : https://docs.spring.io/spring-batch/docs/current/api/org/springframework/batch/core/job/builder/JobBuilder.html
    - 
- StepBuilderFactory → StepBuilder
    - get

대응 예시

```jsx
// Sample with v4
@Configuration
@EnableBatchProcessing
public class MyStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step myStep() {
        return this.stepBuilderFactory.get("myStep")
                .tasklet(..) // or .chunk()
                .build();
    }
}
// Sample with v5
@Configuration
@EnableBatchProcessing
public class MyStepConfig {

    @Bean
    public Tasklet myTasklet() {
       return new MyTasklet();
    }

    @Bean
    public Step myStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep", jobRepository)
                .tasklet(myTasklet, transactionManager) // or .chunk(chunkSize, transactionManager)
                .build();
    }
}
```