# 🎶 Encorely (앵콜리)
<img width="70" height="70" alt="encorely logo" src="https://github.com/user-attachments/assets/a373f7cb-2998-4f53-af71-e04df831c394" />

> “공연의 여운을 기록하는 공간”  
> Encore(앙코르) + ly = 앙코르처럼 기록하다  

공연의 여운을 기록하고 팁을 공유하며,  
모두의 공연 생활을 응원하는 앱입니다.

---

## 🔧 기술 스택 (Tech Stack)

**Backend**
<div>
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
</div>

**Database**
<div>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
</div>

**Deployment**
<div>
<img src="https://img.shields.io/badge/Amazon%20AWS-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white" />
<img src="https://img.shields.io/badge/Amazon%20EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white" />
<img src="https://img.shields.io/badge/Amazon%20RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white" />
<img src="https://img.shields.io/badge/Amazon%20S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white" />
<img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white" />
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" />
</div>


**Version Control**
<div>
<img src="https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white" />
<img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white" />
</div>


---

## ☁️ AWS 서비스 구성도 (Architecture)
<img width="668" height="612" alt="aws 구성도" src="https://github.com/user-attachments/assets/a0f1b6ba-2518-446b-83f6-aee4de788f34" />

---

## 🤝 협업 규칙 (Collaboration Rules)

### 브랜치 전략 (Branching Strategy)
- `main` 브랜치에서 `develop` 브랜치를 분기
- 개발자들은 `develop` 브랜치에 자유롭게 커밋
- 신규 기능 구현 시 `develop`에서 `feature/*` 브랜치 분기
- 기능 개발 및 테스트 완료 후 `release` 브랜치를 `main`과 `develop`에 병합

---

## 📝 코드 컨벤션 (Code Convention)
- **패키지명**: 소문자로 시작 (`com.example.encorely`)
- **변수명/메서드명**: `camelCase` 사용 (예: `upperCase`)
- **클래스명**: 페이지명·기능명 직접 사용 지양
- **주석**: 앞뒤로 공백 추가 (`// 주석 내용`), 불필요한 주석 지양

---

## 📥 PR 규칙 (Pull Request Rules)
- **제목 형식**: `[타입] 내용` (예: `[Feature] 로그인 기능 구현`)
- **설명 템플릿**:
    ```markdown
    ## 작업 내용
    - 주요 기능 설명

    ## 테스트 결과
    - 테스트 방법 및 결과
    ```
- **리뷰어**: 최소 1명 이상 승인 필수
- **코멘트**: 문제점·개선 방향 명확히 기재

---

## 💬 커밋 메시지 (Commit Message)
- **형식**: `type: subject`
- **예시**: `feat: 로그인 API 엔드포인트 추가`

**Type 종류**
- `feat` : 새로운 기능 추가
- `fix` : 버그 수정
- `docs` : 문서 수정
- `style` : 코드 포맷팅/세미콜론 누락 등
- `refactor` : 코드 리팩토링
- `test` : 테스트 코드
- `chore` : 빌드·패키지 설정 변경
- `comment` : 주석 변경
- `add` : 파일 추가

---

## 👨‍👩‍👧‍👦 팀원
<table>
  <tbody>
    <tr>
      <td align="center">
        <a href="https://github.com/daeun088">
          <img src="https://avatars.githubusercontent.com/daeun088?s=150" width="120" alt="daeun088 프로필"/>
          <br />
          <sub><b>로기 / 김다은</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/keypang">
          <img src="https://github.com/keypang.png?size=150" width="120" alt="username2 프로필"/>
          <br />
          <sub><b>파울로 / 박시윤</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/sohyunlee0102">
          <img src="https://avatars.githubusercontent.com/sohyunlee0102?s=150" width="120" alt="sohyunlee0102 프로필"/>
          <br />
          <sub><b>안젤라 / 이소현</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/hyeeeeon">
          <img src="https://github.com/hyeeeeon.png?size=150" width="120" alt="hyeeeeon 프로필"/>
          <br />
          <sub><b>현 / 이승현</b></sub>
        </a>
      </td>
    </tr>
  </tbody>
</table>
