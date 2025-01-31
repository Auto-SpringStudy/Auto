# Spring Study 서기 자동화 프로젝트

## 프로젝트 시작 방법
### 1. 프로젝트 클론 (다운로드)
- 프로젝트 다운하려는 디렉토리로 이동 후 아래 명령 실행
```bash
git clone https://github.com/Auto-SpringStudy/Auto.git
cd Auto
```
- 만약 `git clone` 거부되면 저장소의 `Collaborator(협업자)`로 추가되었는지 확인 필요

### 2. 새로운 브랜치 생성 및 이동
- 기본적으로 main 브랜치가 체크아웃되므로, 새 브랜치를 만들고 이동 필요
```bash
git checkout -b feature/{branch-name}
```

### 3. IDEA 로 프로젝트 오픈, 코드 수정하기.
```bash
git add .
git commit -m "Add user authentication feature"
```

### 4. main 브랜치 코드 받아오기
```bash
git checkout main  # main 브랜치로 이동
git pull origin main  # 최신 코드 가져오기
git checkout feature/your-branch-name  # 다시 작업 중인 브랜치로 이동
git merge main  # 최신 코드 반영
```

---
## Docker 로 디비 연결하는 방법
- 이번 프로젝트는 Docker 를 활용해 개발 참여자가 디비 환경 셋팅을 편리하게 할 수 있도록 설정했습니다.

### 1. Docker, Docker Compose 다운로드
- Homebrew 를 사용해 다운받기
```bash
brew install --cask docker
docker --version # 설치 됬는지 확인
open -a Docker # 도커 실행
brew install docker-compose # Docker Compose 설치
docker-compose --version # 설치 됬는지 확인
```

### 2. Docker 컨테이너 띄우기
- 현재 프로젝트에는 compose.yaml 파일이 있어 아래 명령어로 프로젝트에서 사용하는것과 동일한 데이터베이스를 컨테이너에 띄워준다.
- 수행 전 Docker 를 실행해야하므로 `docker ps` 로 Docker 가 실행중인지 확인한다
```bash
docker ps # Docker 실행중인지 확인
docker-compose up -d
```

### 3. Docker Container 확인하기
- 정상적으로 컨테이너가 생성되었는지 확인한다
```bash
 docker ps
```

- 아래처럼 출력이 나온다면 정상적으로 실행된것.
```bash
CONTAINER ID   IMAGE       STATUS          PORTS                  NAMES
5c123abcde     mysql:8.0   Up 30 seconds   3306/tcp, 0.0.0.0:3306->3306/tcp   my-mysql-container
```

- `SpringstudyApplicationTests` 에서 `connectTest()` 으로 테스트 가능

---
## Docker Container 를 DBeaver 로 연결하는 방법
1. New Connection 으로 새로운 커넥션 생성
2. 컨테이너 DB 연결 정보 입력

| 설정 | 값           |
|---|-------------|
| **Host** | localhost 또는 127.0.0.1 |
| **Port** | 3306        |
| **Database** | spring_study (연결할 데이터베이스명) |
| **Username** | sa          |
| **Password** | sa          |



