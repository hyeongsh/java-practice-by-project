# 주문 관리 시스템 v4 (동시성 + 테스트)

## 목표
- synchronized/volatile 역할 차이 이해
- 멀티스레드 환경에서 레이스 컨디션 체험
- ExecutorService 사용
- JUnit 기반 검증 추가

## 요구사항
### 1. 동시성 문제 재현 테스트 만들기
- 여러 스레드가 동시에 `OrderService`를 호출하는 테스트 시나리오 작성
- 대상 메서드 예시:
  - `addOrder(Order order)`
  - `getOrder(String orderId)`
  - `getTotalAmountByCustomer(String customerId)`
- 재현 목적:
  - 데이터 불일치(누락/합계 불일치)
  - 예외 발생 시점 확인
  - 컬렉션 동시 접근 위험 체감

### 2. OrderService 동기화 적용
- `OrderService`의 내부 상태(`orderMap`, `orderSet`, `ordersByCustomer`)가 멀티스레드에서도 일관되도록 수정
- 최소 1가지 방식으로 먼저 해결:
  - 메서드 단위 `synchronized`
  - 또는 임계구역(block) `synchronized`
- 수정 후에도 기존 예외 정책 유지
  - 중복 주문 → `DuplicateOrderException`
  - 잘못된 입력 → `IllegalArgumentException`
  - 없는 주문 조회 → `OrderNotFoundException`

### 3. volatile 사용 예시 추가
- `volatile` 필드 1개를 도입해 "가시성" 용도로만 사용해보기
  - 예: 종료 플래그, 로딩 상태 플래그
- 아래 내용을 코드/주석/기록으로 명확히 남길 것:
  - `volatile`은 가시성 보장
  - 복합 연산의 원자성은 보장하지 않음
  - 컬렉션 동기화를 대신할 수 없음

### 4. ExecutorService 기반 실행
```
ExecutorService executor = ...
```
#### 조건:
- 직접 `new Thread()` 남발 금지 (학습용 1회 실험 제외)
- `submit()` 또는 `execute()`를 사용해 작업 실행
- 테스트 종료 시 `shutdown()` 호출
- 가능하면 `awaitTermination()` 또는 `Future#get()`으로 작업 완료 확인

### 5. JUnit 테스트 추가
- 기존 수동 테스트(`main.java.OrderServiceTest`)를 참고해 JUnit 테스트 클래스를 추가
- 최소 검증 케이스:
  - 단일 스레드 기존 동작 회귀 테스트 2개 이상
  - 멀티스레드 동시 추가 후 총 주문 수/합계 일관성 테스트
  - 중복 주문 동시 추가 시 예외 정책 확인
- 테스트는 "재현 가능"하게 작성
  - 스레드 수/작업 수를 상수로 두기
  - 실행 순서 의존 assert 최소화

## 규칙
- if문 중첩 3단 이상 금지
- service는 입력/출력 책임 가지지 않기 (System.out 금지)
- 테스트 코드에서만 출력 가능
- 동시성 테스트에서 `Thread.sleep()` 의존 최소화

## 오늘의 학습 포인트
#### 코드 짜다 보면 반드시 막히는 부분:
- `synchronized`를 어디까지 잡아야 안전하고 과하지 않을까?
- `volatile`이 되는 것/안 되는 것을 어떻게 구분하지?
- 멀티스레드 테스트를 어떻게 "운 좋게 통과"가 아니라 검증으로 만들지?
- `submit()`과 `execute()`를 언제 구분해서 쓰지?
