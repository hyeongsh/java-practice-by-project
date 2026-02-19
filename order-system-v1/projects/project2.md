# 인덱스 도입으로 시간복잡도 바꾸기

## 요구사항
### 1. customerId -> 주문 목록 인덱스를 추가하라
- Map<String, List<Order>> ordersByCustomer

### 2. getTotalAmountByCustomer(customerId)
- 전체 순회 금지
- 인덱스 기반 계산

### 3. 주문 추가 시 두 자료구조가 항상 일관되도록 유지
- orderMap
- ordersByCustomer

### 4. getOrdersByCustomer(customerId) 메서드 추가
- 반환 리스트는 외부에서 수정해도 내부에 영향이 없도록 설계


