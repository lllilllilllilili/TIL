## Restful 과 Restful 설계시 고려하면 좋을 점 
- RESTful API는 REST (Representational State Transfer) 아키텍처를 따르는 API로, **HTTP 메서드(GET, POST, PUT, DELETE 등)**를 활용하여 자원(Resource)을 조작한다. 


### RESTful 설계 시 고려할 점
- 일관된 인터페이스 (Uniform Interface): URL 경로를 통해 자원을 명확하게 식별해야 하며, 자원에 대한 표현(예: /users, /products)이 명확하게 일관되어야 한다. RESTful API 설계 시, HTTP 메서드(GET, POST, PUT, DELETE 등)를 일관되게 사용하여 자원 조작 방식을 구분한다. 
- 무상태성 (Stateless): 서버는 클라이언트의 상태를 저장하지 않으므로, 각 요청은 독립적이어야 한다.
- 캐시 가능성 (Cacheable): 성능 최적화를 위해 응답에 캐시 관련 헤더(예: Cache-Control)를 포함하여 클라이언트가 응답을 캐시할 수 있게 한다. 
- 클라이언트-서버 구조 (Client-Server Architecture): 클라이언트와 서버는 서로 독립적으로 개발, 배포될 수 있어야 하며, 서로 다른 목적을 수행해야 한다. 
- 계층형 시스템 (Layered System): API 구조를 설계할 때, 로드 밸런서나 캐시 서버와 같은 중간 계층을 두어 성능을 높일 수 있다.
- 자원 중심 설계 (Resource-Centric Design): RESTful 설계 시 API 엔드포인트는 명확한 자원 중심 설계를 따르는 것이 좋다. 

## RESTful 설계의 장점
이렇게 RESTful 설계를 따름으로써 API의 일관성, 확장성, 유지 보수성을 높일 수 있다. 