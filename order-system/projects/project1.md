# 주문 관리 시스템 v1 (객체/동등성/컬렉션 기본)

## 목표
- 불변 객체 설계
- equals/hashCode 직접 구현
- HashMap/HashSet 동작 이해
- 고객별 합계 계산

## 요구사항
### 1. Order 클래스 설계
```
orderId (String)
customerId (String)
amount (long)
```
#### 조건:
- Order는 불변 객체
- 모든 필드 null/blank/비정상 값 검증
- equals/hashCode 직접 구현
- 기준은 orderId

### 2. User 클래스 설계
```
id (String)
name (String)
```
#### 조건:
- User는 불변 객체
- equals/hashCode 직접 구현
- 기준은 id + name

### 3. OrderService 만들기
```
addOrder(Order order)
getOrder(String orderId)
getTotalAmountByCustomer(String customerId)
getAllOrders()
containsOrder(Order order)
```
#### 조건:
- 내부 저장소는 HashMap<String, Order>
- 중복 orderId 들어오면 예외 발생
- 존재하지 않는 주문 조회 시 예외 발생
- containsOrder는 HashSet을 사용해 중복 판단 과정을 직접 체감

### 4. 메인에서 테스트 코드 작성
#### 아래 케이스 직접 테스트:
- 주문 3개 추가
- 고객별 합계 출력
- 중복 주문 추가 시도 → 예외 확인
- 없는 주문 조회 → 예외 확인
- containsOrder로 중복 판단 결과 확인

## 규칙
- if문 중첩 3단 이상 금지
- getter만 있는 빈 껍데기 클래스 금지
- service는 입력/출력 책임 가지지 않기 (System.out 금지)

## 오늘의 학습 포인트
#### 코드 짜다 보면 반드시 막히는 부분:
- equals/hashCode를 왜 직접 구현해야 하지?
- HashMap/HashSet은 어떤 순서로 비교하지?
- 불변 객체 검증 범위는 어디까지?
