## Jenkins 설정 및 구성
- Jenkins 서버를 구축하고, 필요한 플러그인을 설치 
- 빌드에 필요한 자원들을 Jenkins 내에서 설정

## 소스코드 관리
- SCM 연결
    - Git 과 같은 코드 저장소를 Jenkins 에 연결하여 배포할 코드를 자동으로 가져올 수 있게 설정한다. 이를 통해서 최신 소스코드를 Jenkins 가 감지하여 빌드 및 배포를 시작할 수 있게 한다. 

## 배치 서버 접근 설정
- SSH 설정 
    - Jenkins 가 배치 서버에 접근할 수 있게 SSH 설정을 한다. 
    - 배치 서버의 공개키를 Jenkins 에 등록하거나 Jenkins 서버의 공개키를 배치 서버에 등록해서 SSH 를 통해 Jenkins 가 배치 서버로 접속할 수 있게 한다. 
    - Private Key 관리 : Jenkins에서 배치 서버에 접속할 때 사용할 SSH private key 는 Jenkins의 Credential 기능을 통해 안전하게 관리한다. 