# 주문 관리 시스템 v1 (싱글 스레드)

## 목표
- 객체 설계
- equals/hashCode
- HashMap 사용
- 예외 설계
- 책임 분리

## 요구사항
### 1. Order 클래스 설계
```
orderId (String)
customerId (String)
amount (long)
```
#### 조건:
- orderId는 절대 중복되면 안 됨
- Order는 불변 객체로 만들 것
- equals/hashCode 직접 구현
- 기준은 orderId

### 2. OrderService 만들기
```
addOrder(Order order)
getOrder(String orderId)
getTotalAmountByCustomer(String customerId)
getAllOrders()
```
#### 조건:
- 내부 저장소는 HashMap<String, Order>
- 중복 orderId 들어오면 예외 발생
- 존재하지 않는 주문 조회 시 예외 발생

### 3. 커스텀 예외 만들기
```
DuplicateOrderException
OrderNotFoundException
```
- RuntimeException 상속으로 구현

### 4. 메인에서 테스트 코드 작성
#### 아래 케이스 직접 테스트:
- 주문 3개 추가
- 고객별 합계 출력
- 중복 주문 추가 시도 → 예외 확인
- 없는 주문 조회 → 예외 확인

### 5. 규칙
- if문 중첩 3단 이상 금지
- getter만 있는 빈 껍데기 클래스 금지
- service는 입력/출력 책임 가지지 않기 (System.out 금지)

### 6. 오늘의 학습 포인트
#### 코드 짜다 보면 반드시 막히는 부분:
- equals/hashCode 어떻게 구현하지?
- 불변 객체는 어떻게 만들지?
- 예외는 어디서 던지지?
- Map을 어떻게 써야 하지?


