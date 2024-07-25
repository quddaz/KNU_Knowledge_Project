<p align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=F0E6D6&height=250&section=header&text=💡Knowledge_In&fontSize=90&fontAlign=70&fontAlignY=40&fontColor=2F4F4F&animation=fadeIn" style="width: 100%; height: auto;" />
</p>

## ⏲️ 개발 기간 
- 2023.10.16(월) ~ 2024.12.18(월)
  
## 🧑‍🤝‍🧑 개발자  
- **이경훈** :  백엔드 작업, 웹페이지 개발, 테스트
- **김병웅** :  DB 설계, 백엔드 작업
- **강성민** :  페이지 CSS작업

## 💻 개발환경
- **Version** : Java 17
- **IDE** : IntelliJ
- **Framework** : SpringBoot 3.1.5
- **ORM** : JPA

## ⚙️ 기술 스택
- **Server** : Spring Security
- **DataBase** : MySQL, JPA, ERD
- **WAS** : Tomcat

## 📝 ERD
![1](https://github.com/quddaz/DB_Project/assets/127312774/ab261d13-d11b-4503-bddd-71c222522b5c)

## 🧷 구조

![1](https://private-user-images.githubusercontent.com/127312774/311494344-b357c4ec-ab8c-4b92-9efc-61af2bae8c09.PNG?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjE5MzIyODUsIm5iZiI6MTcyMTkzMTk4NSwicGF0aCI6Ii8xMjczMTI3NzQvMzExNDk0MzQ0LWIzNTdjNGVjLWFiOGMtNGI5Mi05ZWZjLTYxYWYyYmFlOGMwOS5QTkc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzI1JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDcyNVQxODI2MjVaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT02ZWViNDdjODc0YmM2ZjA4Y2RhZDk1MTFmODJlY2I3ZmM3ZGU0OGY3OGRkN2Y4YmU2YTRhZjMyNTcwNDliM2U0JlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.N9O35rZsPqZkTKKtiAy3nz3jLoUn_my7_6rKj0eZ2zk)


## 📌 주요 기능
- 로그인 관련 기능
  - Spring Security를 이용한 인증과 인가 작업
  - 로그인이 성공하면 세션에 등록한다
  - 권한이 없으면 로그인 페이지로 리다이렉트 한다.
- 게시글 기능
  - 회원은 게시글을 작성할 수 있으며, 작성된 댓글을 확인할 수 있다.
  - 게시글을 작성할 때 Web Speech API를 통해 음성으로 텍스트를 작성할 수 있다.
  - 이미지나 텍스트 파일을 첨부할 수 있다.
  - 게시글 작성자는 Chat Gpt로 부터 자신이 작성한 질문을 토대로 답변을 받을 수 있다.
  - 회원이 게시글을 들어갈 때마다 조회수를 업데이트 해야한다.
- 답변글 기능
  - 회원은 특정 게시글에 답변을 작성할 수 있으며 게시글 작성자로 부터 채택이 될 수 있다.
  - 회원이 작성한 댓글이 채택되면 내공을 획득할 수 있다.
  - 회원은 자신이 작성한 답변글을 수정할 수 있다.
- 회원 관련 기능
  - 회원은 자신의 정보를 확인할 수 있다.
  - 회원은 자신의 정보를 업데이트 할 수 있다.
- 퀘스트 기능
  - 회원은 하루에 3개의 퀘스트를 수행할 수 있다.
  - 회원이 게시글 작성, 답변글 작성, 자신의 답변글이 채택등과 같은 퀘스트를 수행하면 각각 내공을 획득한다.
  - 각 퀘스트를 수행하면 같은 날에는 더 이상 같은 퀘스트를 수행할 수 없으며 하루가 지나면 퀘스트가 초기화된다.

## 🚩 배운점
- 데이터베이스 프로젝트 였기에 발표, 기획 모두 데이터베이스 설계를 중점으로 하였습니다. 이 과정에서 스프링에서 어떻게하면 데이터베이스를 효율적으로 설계할 수 있는지 배웠고 반정규화에 대한 심도있는 고민을 하였습니다.
- 공부를 시작하고 처음으로 진행한 스프링 프로젝트였기에 스프링에 대한 기본적인 흐름을 깨우칠 수 있는 과정이였습니다. 또한 스프링의 각종 기술에 대한 배경지식을 확장할 수 있었던 프로젝트였습니다.
