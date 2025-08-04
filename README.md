# Monolithic Bank Application

마이크로서비스 아키텍처에서 모놀리식 아키텍처로 마이그레이션된 은행 애플리케이션입니다.

## 프로젝트 개요

이 프로젝트는 기존의 8개 마이크로서비스를 하나의 모놀리식 애플리케이션으로 통합한 결과물입니다.

### 기존 마이크로서비스 구조
- `modernbank_ui`: Next.js 프론트엔드
- `modernbank_user`: Go 언어 인증/인가 서비스 (PostgreSQL)
- `modernbank_account`: 계정 관리 서비스 (Java/Spring Boot/PostgreSQL)
- `modernbank_customer`: 고객 정보 관리 서비스 (Java/Spring Boot/PostgreSQL)
- `modernbank_transfer`: 이체 관리 서비스 (Java/Spring Boot/PostgreSQL)
- `modernbank_b2bt`: 타행이체 서비스 (Java/Spring Boot)
- `modernbank_cqrs`: 조회용 서비스 (Java/Spring Boot/PostgreSQL)
- `modernbank_product`: 상품 관리 서비스 (Java/Spring Boot/DynamoDB)

### 모놀리식 통합 결과
- **단일 애플리케이션**: 모든 서비스가 하나의 WAR 파일로 통합
- **통합 데이터베이스**: PostgreSQL 단일 데이터베이스 사용
- **Kafka 제거**: Event-driven 아키텍처에서 직접 호출 방식으로 변경
- **CQRS 통합**: 별도 서비스에서 모놀리식 내부 로직으로 통합

## 기술 스택

- **Java**: 17
- **Spring Framework**: 5.3.30 (Spring MVC)
- **Tomcat**: 9.x 호환
- **Build Tool**: Gradle 8.11.1
- **Database**: PostgreSQL
- **ORM**: MyBatis 3.5.13
- **Authentication**: JWT
- **Logging**: Logback + SLF4J

## 패키지 구조

```
com.monolithicbank
├── account          # 계정 관리
├── customer         # 고객 정보 관리
├── transfer         # 이체 관리
├── product          # 상품 관리
├── user             # 사용자 인증/인가
├── b2bt             # 타행이체
├── common           # 공통 기능
│   ├── config       # 설정
│   ├── exception    # 예외 처리
│   ├── security     # 보안
│   ├── utils        # 유틸리티
│   └── interceptor  # 인터셉터
└── config           # 애플리케이션 설정
```

## 데이터베이스 스키마

주요 테이블:
- `TB_USER`: 사용자 인증 정보
- `TB_CSTM`: 고객 정보
- `TB_ACNT`: 계좌 정보
- `TB_TRNS_HST`: 거래 이력
- `TB_TRNF_HST`: 이체 이력
- `TB_TRNF_LMT`: 이체 한도
- `TB_PRODUCT`: 상품 정보

## 설치 및 실행

### 1. 사전 요구사항
- Java 17
- PostgreSQL 12+
- Tomcat 9.x

### 2. 데이터베이스 설정
```sql
-- PostgreSQL에서 데이터베이스 생성
CREATE DATABASE monolithicbank;

-- DDL 스크립트 실행
\i src/main/resources/sql/MONOLITHIC_BANK_DDL.sql
```

### 3. 애플리케이션 설정
`src/main/resources/application.properties` 파일에서 데이터베이스 연결 정보 수정:
```properties
spring.datasource.url=jdbc:log4jdbc:postgresql://localhost:5432/monolithicbank
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 4. 빌드 및 배포
```bash
# WAR 파일 빌드
./gradlew clean war

# Tomcat에 배포
cp build/libs/monolithic-bank.war $TOMCAT_HOME/webapps/
```

### 5. 애플리케이션 실행
```bash
# Tomcat 시작
$TOMCAT_HOME/bin/startup.sh

# 애플리케이션 접속
http://localhost:8080/monolithic-bank
```

## API 엔드포인트

### 사용자 관리
- `POST /user/signup` - 사용자 등록
- `POST /user/login` - 로그인
- `GET /user/{userId}` - 사용자 정보 조회

### 고객 관리
- `POST /customer/` - 고객 등록
- `GET /customer/{cstmId}` - 고객 정보 조회
- `GET /customer/{cstmId}/details` - 고객 상세 정보 조회

### 계좌 관리
- `POST /account/` - 계좌 생성
- `GET /account/{acntNo}` - 계좌 조회
- `GET /account/{acntNo}/balance` - 잔액 조회
- `POST /account/deposits/` - 입금
- `POST /account/withdrawals/` - 출금

### 이체 관리
- `POST /transfer/internal` - 내부 이체
- `POST /transfer/external` - 타행 이체
- `GET /transfer/history/{cstmId}` - 이체 내역 조회

### 상품 관리
- `POST /product/` - 상품 등록
- `GET /product/` - 전체 상품 조회
- `GET /product/{id}` - 상품 조회
- `PUT /product/{id}` - 상품 수정
- `DELETE /product/{id}` - 상품 삭제

## 주요 변경사항

### 1. 아키텍처 변경
- **마이크로서비스** → **모놀리식**: 서비스 간 네트워크 호출을 직접 메서드 호출로 변경
- **Event-driven** → **직접 호출**: Kafka 메시징을 제거하고 동기식 호출로 변경
- **다중 데이터베이스** → **단일 데이터베이스**: PostgreSQL로 통합

### 2. 기술 스택 변경
- **Spring Boot 3.3.6** → **Spring Framework 5.3.30**: 순수 Spring MVC 사용
- **DynamoDB** → **PostgreSQL**: NoSQL에서 관계형 데이터베이스로 변경
- **Go 언어** → **Java**: 사용자 서비스를 Java로 재작성

### 3. 배포 방식 변경
- **개별 JAR 파일** → **단일 WAR 파일**: Tomcat에 배포 가능한 웹 애플리케이션

## 개발 가이드

### 새로운 기능 추가
1. 해당 도메인 패키지에 엔티티, 서비스, 컨트롤러 추가
2. MyBatis XML 매퍼 파일 작성
3. 필요시 데이터베이스 스키마 업데이트

### 테스트
```bash
# 단위 테스트 실행
./gradlew test

# 통합 테스트 실행
./gradlew integrationTest
```

## 트러블슈팅

### 일반적인 문제들
1. **데이터베이스 연결 실패**: PostgreSQL 서비스 상태 및 연결 정보 확인
2. **JWT 토큰 오류**: `jwt.secret` 설정값 확인
3. **MyBatis 매핑 오류**: XML 매퍼 파일의 네임스페이스와 메서드명 확인

### 로그 확인
- 애플리케이션 로그: `logs/monolithic-bank.log`
- Tomcat 로그: `$TOMCAT_HOME/logs/catalina.out`

## 라이선스

이 프로젝트는 교육 목적으로 작성되었습니다."# monolithic-demo" 
