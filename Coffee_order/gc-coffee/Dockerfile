FROM openjdk:16-jdk-alpine

# 컨테이너 내부에 tmp 디렉토리를 만들고 이것을 로컬(도커설정파일 내부)에 마운트
VOLUME /tmp

# 로컬 디렉토리의 jar파일경로를 변수에 저장 (maven일때의 경로임)
ARG JAR_FILE=target/*.jar

# 저장한 경로의 jar 파일을 -> 컨테이너 내 app.jar로 카피
COPY ${JAR_FILE} app.jar

# 컨테이너 내부에 8080 포트 열어주기
EXPOSE 8080

# jdbc 난수 설정을 제거하고 컨테이너 내부 jar 파일 실행
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


# * 이미 빌드완료된 jar파일을 구동시켜주는 것 뿐임

# 빌드
# docker build . -t spring_8080

# 실행
# docker run -d --name con_spring_8080 -p 8080:8080 spring_8080