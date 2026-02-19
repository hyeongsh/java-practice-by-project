# 학습 프로젝트 로드맵 (3시간 x 4개)

## 핵심 학습 영역
- 언어 기본: call by value(참조), String immutability, Integer 캐싱, final/static, 오버로딩/오버라이딩
- 컬렉션/동등성: equals/hashCode, HashMap/HashSet 동작, Comparable/Comparator, 키 객체 설계
- 예외/자원: checked vs unchecked, try-with-resources, 예외 설계/메시지 정책
- JVM/메모리/GC: Stack/Heap/Metaspace, GC 차이, STW, 메모리 누수
- 동시성: synchronized/volatile, Executor, 레이스 컨디션, 스레드풀 큐 문제
- 미니 코딩: 그룹 합계, 안전한 키 객체 구현

## Project 1: 객체/동등성/컬렉션 기본
- domain.Order/User 불변 설계
- equals/hashCode 직접 구현
- HashMap/HashSet 동작 이해
- getTotalAmountByCustomer 구현

## Project 2: 컬렉션 심화 + 정렬
- 인덱스(Map<Customer, List>) 추가
- Comparable/Comparator 정렬
- 방어적 복사

## Project 3: 예외/자원/파일 처리
- checked/unchecked 예외 설계
- CSV 로딩 + try-with-resources
- 예외 메시지 정책 통일

## Project 4: 동시성 + 테스트
- synchronized/volatile 체험
- 멀티스레드 add/조회
- ExecutorService 사용
- JUnit으로 검증
