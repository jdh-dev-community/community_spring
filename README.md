# Coconut. (코코넛.)

### 프로젝트 개요

- 프로젝트 이름: Coconut.
- 프로젝트 목표: 개발자와 개발자가 되고 싶은 사람들이 정보를 공유하는 곳!

### 기술 스택

- 인프라: AWS EC2, RDS, Elastic Cache, SNS, Lamda, CloudWatch
- 애플리케이션 개발: Spring boot 2.7.18, JPA
- 데이터베이스: MySQL, Redis
- 배포 및 문서화: Docker, Github Action, Swagger
- 테스트: Junit

### 설계 문서

- 시스템 구성도

  - v0.1

    ![Screenshot 2024-03-04 at 10.33.18 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9db0eb15-1711-4ec3-8f30-0a1c9b382218/3965bc45-9c0d-44d4-bafc-be42d882156a/Screenshot_2024-03-04_at_10.33.18_PM.png)

- 데이터베이스 설계
  - v0.1
    ![Screenshot 2024-03-12 at 9.42.37 PM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9db0eb15-1711-4ec3-8f30-0a1c9b382218/8d56484f-8b00-47fc-b419-f52c87577281/Screenshot_2024-03-12_at_9.42.37_PM.png)
- 시퀀스 다이어그램
  - 게시글 수정
    ![게시물 수정.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9db0eb15-1711-4ec3-8f30-0a1c9b382218/77e2dee5-0b47-4427-82c9-41d5e7eb4152/%E1%84%80%E1%85%A6%E1%84%89%E1%85%B5%E1%84%86%E1%85%AE%E1%86%AF_%E1%84%89%E1%85%AE%E1%84%8C%E1%85%A5%E1%86%BC.png)
  - 게시글 상세 조회
    ![게시물 상세.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9db0eb15-1711-4ec3-8f30-0a1c9b382218/86fbc633-223f-4953-ba30-b0737e0433e2/%E1%84%80%E1%85%A6%E1%84%89%E1%85%B5%E1%84%86%E1%85%AE%E1%86%AF_%E1%84%89%E1%85%A1%E1%86%BC%E1%84%89%E1%85%A6.png)
  - 게시글 목록 조회
    ![게시물 조회.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9db0eb15-1711-4ec3-8f30-0a1c9b382218/bb1f5133-6936-4160-97e1-5544f8b8d845/%E1%84%80%E1%85%A6%E1%84%89%E1%85%B5%E1%84%86%E1%85%AE%E1%86%AF_%E1%84%8C%E1%85%A9%E1%84%92%E1%85%AC.png)
- rest api 명세

  ![Screenshot 2024-03-05 at 9.13.44 AM.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/9db0eb15-1711-4ec3-8f30-0a1c9b382218/3ec57805-7821-440c-8bf6-70cc1374721f/Screenshot_2024-03-05_at_9.13.44_AM.png)

- **스프린트1 - mvp 출시**
  [스프린트1 일정](https://www.notion.so/b02de284048149d299d1f044559c4923?pvs=21)
  - 버전: 0.1 (mvp)
  - 목표: 게시글과 댓글 작성이 가능한 웹 페이지 배포
  - 기획: https://www.figma.com/file/CAXf9vgxHMXZF4iKUqU16b/jdh-team's-team-library?type=design&node-id=2333%3A3&mode=design&t=5MpKQOmsVi1qS9zA-1
  - 디자인 링크: https://www.figma.com/file/CAXf9vgxHMXZF4iKUqU16b/jdh-team's-team-library?type=design&node-id=2333%3A2&mode=design&t=5MpKQOmsVi1qS9zA-1
