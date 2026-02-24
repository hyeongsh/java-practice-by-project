# 자바 중급 개념 Outline (v1)

대상:
- 자바 기초 문법/OOP/컬렉션/예외/테스트/동시성 입문을 끝낸 학습자
- "동작"을 넘어서 "왜 이렇게 설계/검증해야 하는지"를 이해하고 싶은 경우

목표:
- 자바 표준 라이브러리와 언어 기능을 더 정확하게 사용하기
- 동시성/테스트/설계 판단의 품질을 높이기
- 이후 백엔드/Spring 학습에서 자바 자체 때문에 막히지 않기

---

## 전체 구성 (권장 8주)

1. 제네릭과 타입 시스템
2. 컬렉션 심화 + equals/hashCode 계약
3. 함수형 스타일 (Lambda/Stream/Optional)
4. 예외 설계와 API 설계 판단
5. 테스트 설계 심화 (재현성/경계값/예외/동시성 테스트)
6. 동시성 중급 1 (JMM, volatile, synchronized, Atomic)
7. 동시성 중급 2 (ExecutorService, concurrent collections, CompletableFuture)
8. I/O/NIO + 시간 API + 종합 미니 프로젝트

---

## 1. 제네릭과 타입 시스템

핵심 질문:
- 왜 `List<Object>`와 `List<String>`는 대체 관계가 아닌가?
- `? extends T`와 `? super T`는 언제 쓰는가?

학습 내용:
- 제네릭 기본 (`T`, `E`, `K`, `V`)
- 제네릭 메서드
- bounded type parameter (`<T extends ...>`)
- 와일드카드 (`?`, `? extends`, `? super`)
- PECS 원칙 (Producer Extends, Consumer Super)
- raw type 위험성

연습:
- 정렬/필터 유틸 메서드를 제네릭으로 리팩터링
- `Comparator`/`Comparable`와 제네릭 연결

---

## 2. 컬렉션 심화 + equals/hashCode 계약

핵심 질문:
- `HashSet`에서 중복 판단이 왜 깨지는가?
- `equals`만 구현하고 `hashCode` 안 하면 왜 문제인가?

학습 내용:
- `List`, `Set`, `Map` 선택 기준
- `ArrayList` vs `LinkedList` (실사용 관점)
- `HashMap`/`HashSet` 동작 감각 (버킷, 해시 충돌 개념)
- `equals/hashCode` 계약
- `Comparable` vs `Comparator`
- 불변 객체와 컬렉션 안정성

연습:
- `Order`/`User` 도메인 객체에 대해 동일성 기준 설계
- 의도적으로 잘못된 `equals/hashCode` 작성 후 테스트로 깨보기

---

## 3. 함수형 스타일 (Lambda / Stream / Optional)

핵심 질문:
- 반복문으로 푸는 코드와 Stream 코드의 trade-off는?
- `Optional`은 어디까지 써야 좋은가?

학습 내용:
- 함수형 인터페이스 (`Predicate`, `Function`, `Consumer`, `Supplier`)
- 람다/메서드 레퍼런스
- Stream 기본 연산 (`map`, `filter`, `sorted`, `distinct`, `collect`)
- reduce / grouping / partitioning
- `Optional`의 올바른 사용
- 부작용(side effect) 주의점

연습:
- 주문 목록 집계 로직을 Stream으로 재작성
- `customerId`별 합계/건수/최대금액 집계

---

## 4. 예외 설계와 API 설계 판단

핵심 질문:
- 입력 오류와 도메인 상태 오류를 어떻게 구분할까?
- 조회 결과 없음은 예외로 던질까, 빈 결과로 줄까?

학습 내용:
- checked vs unchecked 예외 사용 기준
- `IllegalArgumentException`, `IllegalStateException` 역할
- 도메인 예외 설계 (`OrderNotFoundException` 등)
- 메시지 설계 (디버깅 가능성)
- 메서드 계약(contract) 설계
- 반환 전략: `null` vs 빈 컬렉션 vs `Optional`
- 복사본 반환 / 불변 뷰 / 내부 컬렉션 노출 금지

이번 점검 약점 반영:
- "입력 오류 / 조회 없음 / 정책상 허용된 빈 결과" 구분 훈련 강화

연습:
- `OrderService` 메서드별 예외/반환 정책 표 작성
- 정책 변경 시 테스트 영향 분석

---

## 5. 테스트 설계 심화 (JUnit)

핵심 질문:
- 테스트가 "운 좋게 통과"하는지 어떻게 구분할까?
- 무엇을 assert 해야 재현 가능한 테스트가 되는가?

학습 내용:
- 테스트 이름/구조 (given-when-then)
- 단위 테스트 vs 통합 테스트
- 경계값 테스트
- 예외 테스트 (`assertThrows`)
- 픽스처 정리 (`@BeforeEach`)
- 파라미터화 테스트(선택)
- 재현 가능한 동시성 테스트 설계 원칙

이번 점검 약점 반영:
- `CountDownLatch`, `CyclicBarrier` 사용 패턴
- `Future.get()`로 예외 수집
- 예상 외 예외 fail 처리
- invariant 중심 assert

연습:
- 중복 주문 동시 추가 테스트 리팩터링
- 합계/주문수/예외 개수 invariant 테스트 추가

---

## 6. 동시성 중급 1 (JMM / volatile / synchronized / Atomic)

핵심 질문:
- 왜 `volatile` 없이도 테스트가 통과할 수 있는데도 잘못된 코드인가?
- `synchronized`와 `AtomicInteger`는 언제 각각 쓰는가?

학습 내용:
- Java Memory Model(JMM) 기초
- happens-before 개념
- visibility / atomicity / ordering 구분
- `volatile`의 의미와 한계
- `synchronized` 락 범위 설계
- 락 객체 선택 (전용 lock object)
- `AtomicInteger`, `AtomicLong`, `AtomicReference`

이번 점검 약점 반영:
- "보장" vs "우연히 통과" 구분
- `count++` 문제를 테스트/이론으로 동시에 이해하기

연습:
- `volatile` 플래그 예시 + timeout 테스트
- `CounterService`를 `AtomicInteger`로 수정하고 비교

---

## 7. 동시성 중급 2 (ExecutorService / Concurrent Collections / CompletableFuture)

핵심 질문:
- `execute()`와 `submit()`은 언제 구분해야 할까?
- 모든 동시성 문제를 `synchronized`로 풀어야 할까?

학습 내용:
- `ExecutorService` 라이프사이클
  - `execute`, `submit`, `shutdown`, `shutdownNow`, `awaitTermination`
  - `Future`, `ExecutionException`, timeout
  - `invokeAll`, `invokeAny`
- `ConcurrentHashMap`, `CopyOnWriteArrayList` 사용 기준
- 스레드풀 크기/큐 개념 (개요 수준)
- `CompletableFuture` 기초

이번 점검 약점 반영:
- `submit()` vs `execute()` 차이와 선택 기준 집중 학습

연습:
- 동일 작업을 `execute` 버전 / `submit` 버전으로 비교 구현
- 예외 수집/집계 테스트 만들기

---

## 8. I/O / NIO / java.time + 종합 미니 프로젝트

핵심 질문:
- 파일 읽기/쓰기 코드를 어떻게 더 안전하고 테스트 가능하게 만들까?
- 날짜/시간 처리를 왜 `java.time`으로 해야 할까?

학습 내용:
- `Path`, `Files`, 문자셋, 예외 처리
- 리소스 로딩 (`resources`)
- `LocalDate`, `LocalDateTime`, `Instant`, `ZoneId`
- 포맷팅/파싱
- 시간 테스트 가능한 코드 설계(개요)

종합 미니 프로젝트 (예시):
- "주문 정산/리포트 생성기"
- 요구사항:
  - CSV 입력 읽기
  - 유효성 검사 및 예외 정책 분리
  - 고객별 집계
  - 정렬/필터
  - JUnit 테스트
  - 일부 동시성 작업(선택)

---

## 운영 방식 (학습 루프)

각 주차마다 반복:
1. 개념 요약 정리 (핵심 정의 + 주의할 오해)
2. 작은 코드 실습 2개
3. 테스트 작성
4. 회고 (헷갈린 점 / 잘못 이해했던 점 기록)

---

## 시작 전 체크포인트 (현재 상태 기준)

이미 강한 부분:
- `synchronized`로 컬렉션 일관성 보호 감각
- `volatile` 가시성 vs 원자성 구분
- 멀티스레드 테스트에서 invariant 중심 사고

먼저 보완하면 좋은 부분:
- `ExecutorService` API 차이 (`submit` vs `execute`)
- 동시성 테스트 배리어 패턴 정확도 (`CountDownLatch.await()`)
- 서비스 예외/반환 정책 문장화 능력

