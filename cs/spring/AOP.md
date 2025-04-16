## 스프링 트랜잭션 AOP 동작 흐름 
- 스프링에서 @Transactional 만 선언하면 트랜잭션 관리해준다. 
- 왜? -> AOP 덕분이다. 

## AOP 기반 트랜잭션 동작 흐름 요약 
1. 프록시 객체 생성 
- @Transactional 붙은 클래스나 메서드는 스프링 컨테이너가 프록시 객체를 생성한다. 
- 해당 프록시는 JDK 동적 프록시 또는 CGLIB 프록시가 대상이 된다. 
```java
MyService myService = applicationContext.getBean(MyService.class);
System.out.println(myService.getClass());
// class com.sun.proxy.$Proxy… 또는 CGLIB proxy
```

2. 메서드 호출을 하게 되면 프록시가 가로챈다. 
- 예를들어서 위의 예시에서 myService.someMethod() 호출하게 되면 프록시 객체가 가로채서 트랜잭션을 적용할지 판단하게 된다. 

3. 트랜잭션 여부를 판단
- TransactionalInterceptor 가 실행된다. 
- @Transactional 이 붙어있는지, 전파 속성, 롤백 조건을 확인하게 된다. 
- 필요하면 트랜잭션을 시작한다. (PlatformTransactionManager 사용한다.)
- 프록시는 트랜잭션을 시작 하고 실제 타깃 객체의 메서드를 실행한다. 

4. 정상 or 예외
- 정상이면 TransactionManager.commit() 호출
- 실패면 TransactionManager.rollback() 호출 
- 단, RuntimeException 이나 Error 만 기본 롤백 대상이다. @Transactional(rollbackFor = Exception.class) 로 지정한다. 

## 주의
- 자기 클래스 내의 메서드 호출은 AOP 가 적용되지 않음
- private, static, final 클래스/메서드 트랜잭션 적용 안됌 