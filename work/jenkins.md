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

## Jenkins Pipeline 설정 
- 배포 스크립트 작성 
    - Jenkins Pipeline 이나 Freestyle Job을 이용해서 배포 스크립트를 작성한다. 
    - 빌드 단계, 배포단계로 나눠서 처리한다. 
        - 빌드는 보통 Maven, Gradle 등의 빌드 도구를 사용해 소스를 빌드한다. 
        - 배포는 scp, rsync, docker 배포 명령어를 실행한다. 

```sh
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                // Maven 
                sh 'mvn clean package'
            }
        }
        stage('Deploy to Batch Server') {
            steps {
                // SSH 접근 후 배포 스크립트 동작 
                sshagent(['batch-server-ssh-credential']) {
                    sh 'scp target/my-app.jar user@batch-server:/path/to/deploy/'
                    sh 'ssh user@batch-server "bash /path/to/deploy/deploy.sh"'
                }
            }
        }
    }
}
```

## 배치 서버 상에서의 스크립트 실행
- Jenkins가 SSH를 통해 배치 서버에 접속하여, 미리 작성해둔 배포 스크립트 .sh 실행하거나 빌드된 파일을 복사해서 서버에서 실행한다. 
- 배치 서버가 이미 Docker 환경을 사용중이라면, Jenkins 가 새로운 Docker 이미지를 빌드하고 이를 배포하는 방식으로 배포를 하기도 한다. 

## 빌드 및 배포 자동화
- Jenkins Job 을 트리거하기 위해 Git hook, Cron, 특정 조건이 발생했을 때 빌드와 배포를 자동으로 실행하도록 실행

## 결과 및 확인
- Jenkins 콘솔 로그를 통해 빌드 및 배포 상태를 실시간으로 확인 