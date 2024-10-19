## git 브랜치간 충돌
- 오늘 fatal: Need to specify how to reconcile divergent branches 요런 이슈가 발생했는데 찾아보니 두 개의 브랜치 간에 충돌 이나 이상이 발생할 때 나타나는 메시지라고 한다. 
- git pull 을 사용하여 origin branch에서 최신 커밋 코드를 받아오려고 했으나 git 이 로컬 브랜치와 원격 브랜치의 변경 사항에 어떻게 병합해야 할지 명확하지 않기 때문에 발생했다. 
- 이건 보통 로컬 브랜치와 원격 브랜치간 서로 다른 커밋을 포함하고 있어서 Git 이 이것을 병합할 방법을 모를 때 해당 오류가 발생한다. divergence 가 발생했다고 한다. git 이 알아서 병합해주지는 않는다. 

## 해결방법
- 리베이스 방법 
    - 리베이스를 통해 로컬 변경 사항을 다시 적용하여 병합 충돌을 피할 수 있다. 
    ```java
    git pull --rebase 
    ```
- git config 명령어
    - git config pull.rebase false # 병합 방식을 기본으로 사용 
    - git config pull.rebase true # 리베이스 방식을 기본으로 사용 

## 적용방법 (순서대로)
- 처음에는 `--ff-only` fase-forward 병합을 허용해서 처리하려고 했다. 하지만 `git pull           
fatal: Not possible to fast-forward, aborting. ` 이런 에러가 발생했고 원격 브랜치과 로컬 브랜치가 서로 다른 커밋을 가지고 있어서 fast-forward 로 병합을 하기가 어려웠다. 
- 그래서 git pull --no-ff 옵션을 이용해 fase-forward 병합이 불가능할 때 자동으로 병합 커밋을 생성해 병합을 수행해줬다. 
- 가급적 rebase 를 사용하지 않았다. 나만 사용하는 브랜치가 아니여서 그렇다. 