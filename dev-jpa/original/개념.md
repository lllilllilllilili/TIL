## 관계형 DB에 관리
- SQL
- 객체 지향 프로그래밍은 추상화, 캡슐화, 정보은닉, 상속, 다형성 등 시스템의 복잡성을 제어할 수 있는 장치를 제공
- 객체 데이터를 SQL 변환해서 RDB에 전환 
  - 개발자가 SQL 매퍼의 역할
- 상속
  - 객체 기반 이걸 디비 테이블에서 고려해본다면
  - 디비 테이블 설계를 잘했다 하더라도 조인 SQL 작성 -> 객체 생성 (복잡성)
  - 자바 컬렉션에 저장하면
    - ```java
      list.add(object);
      ```
- 연관관계
  - 객체는 참조를 사용
  - 테이블은 객체 레퍼런스가 없어서 외래키로 조인을 해서 연관관계를 맺는다.
  - ```java
    insert into member(member_id, team_id, username) values ... 
    ``` 
- 객체는 참조로 연관관계를 맺는다. 
  - ```java
    class Member {
        String id;
        Team team;
        String username; 
    
        Team getTeam() {
            return team;
        }   
    } //이렇게 설계하면 data insert가 어려움
    ```
- member, team을 자바 컬렉션 관리 하면 한줄로 관리
  - 컬렉션 관리를 안하면 조인 같은것으로 관리가 되니 어렵다. 
- 객체는 그래프 탐색
  - 참조로 따라갈 수 있어야 한다. 
  - ```java
    //엔티티  신뢰 문제가  생긴다. Order 같은게 null이 나오면 계층형대로 따라갈 수가 없으니까 
    public void process() {
        Member member = memberDAO.find(memberId);
        member.getTeam();
        member.getOrder().getDelivery(); //dao 코드를 계층에 따라 조회하는 불편함 
    }
    ```
- 믿고 쓸려면 한번에 가져와야할까? 
  - member.getOrderItem() .. 
  - 한번에 데이터를 다 가지고 오면 사용하지 않는게 있어서 성능 이슈
- 계층형 아키텍처는... 그래서 계층 분할이 어렵다.
  - 중요! 물리적으로는 분할되어 있으나 논리적으로는 엮여있다.
- 객체답게 모델링 할수록 매핑 작업만 늘어난다.