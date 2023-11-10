## JPA
- JAVA Persistence API 
  - ORM 
    - 객체 관계 매핑 
    - 대중적인것은 ORM을 제공한다.
- JPA는 애플리케이션과 JDBC 사이에서 동작
  - JPA 동작
    - MemberDAO 넘기고 JPA 던지면 회원 객체 분석
    - insert SQL을 만들어준다. 
    - 패러다임의 불일치? 를 해결해준다. 
    - 알아서 다 해준다. (좋음)
- JPA 소개
  - EJB -> 하이버네이트(오픈 소스) -> JPA(자바 표준)
  - EJB는 거의 쓰지 않았음
  - 둘다 ORM인데 자바 표준 ORM -> JPA 
- JPA는 표준 명세
  - JPA 인터페이스 모음
  - JPA 2.1 표준 명세를 구현한 3가지 구현체
  - 하이버네이트를 거의 쓴다.
- JPA는 왜 사용해야 하는가?
  - SQL 중심에서 객체 중심으로 개발
  - 생산성
  - 유지보수
  - 패러다임의 불일치 해결 (이건 뭐지?)
- 저장: jpa.persist(member)
- 조회: jpa.find(memberId)
- 수정: member.setName("이름")
- 삭제: jpa.remove(member)

## 상속
- 슈퍼타입, 서브타입 관계일 때 insert 가 두 번 나가야함
  - persist만 써주면 jpa가 알아서해줌 
  - 성능 과정에서는 동일한거 아닌가?
- jpa.find
  - select ~~ from join ~ on ~ 이렇게 
  - 이것도 마찬가지로 jpa가 알아서 해준다. 
- 그냥 jpa가 다 알아서 해준다.

## jpa 연관관계
- jpa.find
- member.getTeam(); 
- 성능 최적화해서 알아서 뽑아준다. 

- jpa는 엔티티 계층을 신뢰할 수 있다. jpa 관리 엔티티 데이터 있다는 전제하에 데이터를 다 꺼내온다. 
  - 지연로딩을 이용해서 꺼내온다.
- jpa를 통해서 member1, member2 꺼내오면 엔티티가 같음을 보장한다. 
  - 왜? 동일한 트랜잭션안에서 보장이 되어야 한다. 
- jpa 성능 최적화
  - 1차 캐시 동일성 보장

## 1차 캐시 동일성 보장
- 같은 트랜잭션 안에서는 같은 엔티티를 반환
  - 약간 조회 성능 향상
- DB Isolation Level이 Read Commit 일지라도 애플리케이션에서 Repeatable Read를 보장
- jpa에서는 같은 멤버 조회하게 되면 
  - sql 쿼리가 나감 
  - 한번 더 하면 sql을 하지 않고 메모리상에서 내보낸다. 
  - 같은 트랜잭션 안에서만 가능하다. 
- 트랜잭션 쓰기 지연 
  - 버퍼로 모음, insert sql을 모아서 batch sql을 이용해서 트랜잭션 시작하고 모았다가 트랜잭션  커밋하면 insert 를 하나의 네트워크로 보냄
  - commit 기준으로
  - bufferWriting 이 가능해진다. 
  - jpa가 알아서 해준다. 
- 지연 로딩과 즉시 로딩 
  - 지연 로딩 : 로직을 짜다 보니까 지연 로딩, 즉시 로딩
    - 지연 로딩 객체가 실제 사용될 때 불러옴
    - 즉시 로딩 - 멤버 조회하는 순간 팀도 같이 로딩
    - 성능에 따라 고민하게 됌