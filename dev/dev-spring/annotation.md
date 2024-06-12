## Annotation Proxy 동작방식 이해
Annotation 주석처럼 코드에 붙여 사용하는 메타데이터이다. 

실행 시점에 특정 동작을 할 수 있다. 

Proxy는 메소드 호출을 가로채서 다른 동작을 수행하거나 실제 또는 후에 추가 동작을 수행할 수 있다. 

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAnnotation {
    String value();
}
```
커스텀 annotation을 정의 

proxy 클래스 생성
```java
public interface MyService {
    void doSomething();
}
```
```java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyServiceProxy implements InvocationHandler {

    private final MyService myService;

    public MyServiceProxy(MyService myService) {
        this.myService = myService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Annotation이 있는지 확인
        if (method.isAnnotationPresent(MyAnnotation.class)) {
            MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
            System.out.println("Annotation value: " + annotation.value());
        }
        // 실제 메소드 호출
        return method.invoke(myService, args);
    }

    public static MyService newInstance(MyService myService) {
        return (MyService) Proxy.newProxyInstance(
                myService.getClass().getClassLoader(),
                new Class<?>[]{MyService.class},
                new MyServiceProxy(myService)
        );
    }
}
```

서비스 클래스
```java
public class MyServiceImpl implements MyService {
    @Override
    @MyAnnotation("Hello World")
    public void doSomething() {
        System.out.println("Doing something...");
    }
}
```

이건 annotation이 존재하면 annotation값을 출력한다. 

이 내용을 @Controller 에 적용해보자.

선내용으로 DispatcherServlet > HandlerMapping > HandlerAdapter 과정을 우선 거치게 된다. 

그리고 @Controller 로 넘어오게 되면 `프록시 생성`을 하게 된다. 

이 프록시는 실제 컨트롤러 메서드 호출 전에, 후에 또는 대신에 특정 로직을 수행할 수 있다. 

프록시에서는
- Pre-processing : 요청 로깅, 인증 및 인가, 트랙잭션 관리
- Post-processing : 응답 로깅, 예외 처리 담당 

프록시 객체는 실제 메서드를 호출하기 전에 추가적인 로직을 수행한다. 

Spring AOP는 JdkDynamicAopProxy, CglibAopProxy 를 사용하여 프록시 객체를 생성한다. 

InvocationHandler 의 invoke 메서드가 실행된다. 
```java
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 추가 로직 (Advice) 수행
        System.out.println("Before method execution");
        
        // 실제 메서드 호출
        Object result = method.invoke(target, args);
        
        // 추가 로직 (Advice) 수행
        System.out.println("After method execution");
        
        return result;
    }
}
```

InvocationHandler 인터페이스를 구현하여 메소드를 호출을 가로채 invoke 메서드 내에서 어노테이션이 있는지 확인하고 필요한 로직을 실행한다. 

실제 실행되는 메서드는 어노테이션이 붙어 있는 클래스의 메서드이다. 

## 정리
- 어노테이션 정리
- 인터페이스 정리
- 실제 클래스 정의
- 프록시 클래스 생성
- 어노테이션 처리 로직 구현
- 프록시 객체를 통한 메서드 호출

마지막 프록시 객체를 통한 메서드 호출은 AOP을 통해 이루어진다. AOP는 프록시 객체를 생성하여 빈 메서드 호출을 가로채고 어노테이션 기반의 작업을 처리한다. 

## 스프링에서 프록시 객체를 통한 메서드 호출 과정
- Spring 컨테이너 초기화 
Spring 컨테이너는 설정 파일이나 어노테이션 기반으로 빈을 스캔하고 등록한다. AOP설정도 같이 로드된다. 
- 빈 정의 및 프록시 생성
Spring 컨테이너는 빈 정의를 생성할 때, 빈에 AOP 어드바이스가 적용되는지 확인한다. 적용된다면, 프록시 객체를 생성하여 빈 대신 사용한다. 
- 프록시 객체를 통한 메서드 호출
클라이언트 코드가 빈의 메서드를 호출하면 실제로는 프록시 객체의 메서드를 호출하게 된다. 프록시 객체는 AOP 어드바이스를 적용한 후 실제 빈의 메서드를 호출한다. 

어노테이션 정의 
```java
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecution {
    String value() default "Executing method";
}
```

서비스 인터페이스 및 구현
```java
public interface MyService {
    void performTask();
}

import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService {
    @Override
    @LogExecution("Performing task")
    public void performTask() {
        System.out.println("Task performed.");
    }
}
```

Aspect정의 
```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(logExecution)")
    public void logBefore(LogExecution logExecution) {
        System.out.println(logExecution.value());
    }
}
```

Spring Boot를 사용하면, AOP와 관련된 설정을 자동으로 처리. Spring Boot 프로젝트에서는 @EnableAspectJAutoProxy 어노테이션을 사용하여 AOP를 활성화

(단, Spring Boot의 스타터 패키지를 사용할 경우 AOP 관련 종속성이 자동으로 설정되기 때문에, @EnableAspectJAutoProxy 없이도 AOP가 동작)
```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

----------------------------------
@Target: 어노테이션이 적용될 수 있는 요소를 지정

ElementType.TYPE은 어노테이션이 클래스, 인터페이스, 열거형에 적용

```java
@Target(ElementType.TYPE)
public @interface MyAnnotation {}
```

@Retention: 어노테이션이 어느 시점까지 유지될 것인지를 지정합니다. 예를 들어, RetentionPolicy.RUNTIME은 어노테이션이 런타임까지 유지됨을 의미
```java
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {}
```

빈 생성: MyService와 MyCustomAdvice 빈이 Spring 컨테이너에 의해 생성됩니다.

프록시 생성: Spring AOP는 @Aspect와 @Pointcut을 사용하여 MyService 빈의 프록시를 생성합니다.

메서드 호출 가로채기: myService.myMethod()가 호출되면, 프록시가 이 호출을 가로챕니다.

어드바이스 실행: 프록시가 @MyCustomAnnotation이 붙은 메서드를 호출하는 포인트컷을 발견하고, aroundAdvice를 실행합니다.

메서드 실행: aroundAdvice 내부에서 joinPoint.proceed()가 호출되면 실제 메서드(myMethod())가 실행됩니다.

어드바이스 후 처리: 실제 메서드 실행 후, aroundAdvice의 후처리가 실행됩니다.