# 주문 관리 시스템 v2 (컬렉션 심화 + 정렬)

## 목표
- 인덱스 구조 설계로 시간복잡도 개선
- Comparable/Comparator 이해
- 방어적 복사 적용
- 컬렉션 기반 설계 감각 확장

## 요구사항
### 1. customerId 인덱스 추가
- `Map<String, List<Order>> ordersByCustomer` 도입
- addOrder 시 orderMap과 ordersByCustomer가 항상 일관되게 유지

### 2. getTotalAmountByCustomer 최적화
- 전체 순회 금지
- `ordersByCustomer` 기반으로 계산

### 3. 고객별 주문 조회 추가
```
getOrdersByCustomer(String customerId)
```
#### 조건:
- 반환 리스트는 외부에서 수정해도 내부에 영향 없도록 설계

### 4. 정렬 기능 추가
```
getOrdersSorted(Comparator<Order> c)
```
#### 조건:
- Order는 Comparable 구현 (기본 정렬: orderId 오름차순)
- Comparator로 amount 내림차순, customerId 오름차순 정렬을 테스트

### 5. 메인 테스트 추가
#### 아래 케이스 직접 테스트:
- 고객별 주문 리스트 조회
- 고객별 합계 계산(인덱스 기반)
- 정렬 결과 확인 (기본 정렬 + Comparator 정렬)
- 반환 리스트 수정이 내부에 영향 없는지 확인

## 규칙
- if문 중첩 3단 이상 금지
- service는 입력/출력 책임 가지지 않기 (System.out 금지)
- 테스트 코드에서만 출력 가능

## 오늘의 학습 포인트
#### 코드 짜다 보면 반드시 막히는 부분:
- 인덱스 구조의 장단점은 뭐지?
- Comparable과 Comparator는 어떻게 나뉘지?
- 방어적 복사는 어디까지 해야 하지?
