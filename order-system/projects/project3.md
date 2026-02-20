# 주문 관리 시스템 v3 (예외/자원/파일 처리)

## 목표
- checked/unchecked 예외 기준 이해
- try-with-resources 사용
- 예외 메시지 정책 통일
- 파일 입력 기반 기능 추가

## 요구사항
### 1. 예외 설계
- 파싱 오류용 체크 예외 생성
  - 예: OrderParseException extends Exception
- 예외 메시지 정책을 정해서 코드에 반영
  - 상수 클래스 또는 규칙을 주석으로 명시

### 2. CSV 로더 추가
```
loadOrders(Path path)
```
#### 조건:
- 입력 포맷: orderId,customerId,amount
- 파일 I/O는 try-with-resources 사용
- 파싱 오류(필드 부족, amount 숫자 아님 등)는 OrderParseException으로 감싸서 던질 것
- 중복 주문은 기존 정책(DuplicateOrderException) 그대로 유지

### 3. 입력 검증 강화
- OrderService public 메서드에서 null/blank 입력 검증
- 잘못된 입력은 IllegalArgumentException

### 4. 메인 테스트 추가
#### 아래 케이스 직접 테스트:
- 정상 CSV 로드 성공
- 파싱 오류 시 체크 예외 발생
- 중복 주문 포함된 CSV → DuplicateOrderException 확인

## 규칙
- if문 중첩 3단 이상 금지
- service는 입력/출력 책임 가지지 않기 (System.out 금지)
- 테스트 코드에서만 출력 가능

## 오늘의 학습 포인트
#### 코드 짜다 보면 반드시 막히는 부분:
- checked/unchecked 경계를 어디에 둘지?
- 예외를 감싸는 기준은 뭐지?
- 파일 파싱에서 어느 계층이 책임져야 하지?
