# 자바 중급 개념 v1 (제네릭/타입 시스템)

## 목표
- 제네릭의 필요성(타입 안정성) 이해
- raw type의 위험 체감
- `? extends` / `? super` 기본 사용법 익히기
- `Comparable` / `Comparator`와 제네릭 연결
- 테스트로 제네릭 유틸 메서드 검증하기

## 요구사항

### 1. 제네릭 유틸 클래스 만들기
`GenericUtils` 클래스를 만들고 아래 메서드를 구현한다.

```java
copy(List<? super T> dest, List<? extends T> src)
max(List<? extends T> values)   // T extends Comparable<? super T>
contains(List<?> values, Object target)
```

#### 조건
- `null` 입력 검증 (`IllegalArgumentException`)
- `copy()`는 `src` 순서를 유지해서 `dest`에 추가
- `max()`는 빈 리스트면 `IllegalArgumentException`
- `contains()`는 와일드카드(`List<?>`) 사용

### 2. 정렬 유틸 메서드 만들기
`Order` 또는 별도 연습용 도메인(`Product`, `ScoreRecord` 등) 중 1개를 골라 아래 메서드를 구현한다.

```java
sortNatural(List<T> values)               // T extends Comparable<? super T>
sortWith(List<T> values, Comparator<? super T> comparator)
```

#### 조건
- 내부 리스트를 직접 정렬하지 말고 복사본을 만들어 반환
- `comparator == null`이면 `IllegalArgumentException`
- 반환 타입은 `List<T>`

### 3. PECS 설명 주석/기록 남기기
코드 또는 별도 기록 파일에 아래를 짧게 정리한다.

- 왜 `copy()`에서 `src`는 `? extends T` 인가?
- 왜 `dest`는 `? super T` 인가?
- `List<Object>`와 `List<String>`가 왜 대체 관계가 아닌가?

### 4. raw type 실험 (학습용)
raw type을 사용한 간단한 예시를 작성하고, 왜 위험한지 기록한다.

예시 방향:
- `List`(raw)에 서로 다른 타입 추가
- 꺼낼 때 캐스팅 문제 발생 가능성 설명

#### 조건
- 학습용 예시로만 작성 (실제 서비스 코드에는 사용 금지)
- 가능하면 경고(`unchecked`/raw type warning)가 왜 뜨는지 설명

### 5. JUnit 테스트 추가
최소 테스트 케이스:
- `copy()` 정상 동작 (타입 업캐스팅 포함)
- `max()` 정상 동작
- `max()` 빈 리스트 예외
- `sortWith()` 정렬 결과 검증
- 입력 검증 예외 2개 이상

## 권장 구현 예시 (도메인 선택)
아래 중 하나 선택:

1. `ScoreRecord(name, score)`
- 점수 기준 자연 정렬(오름차순 또는 내림차순 명시)
- 이름 기준 Comparator 정렬 추가

2. `Product(id, price)`
- 가격 기준 Comparator
- id 기준 자연 정렬

3. 기존 `Order` 재사용
- `orderId` 자연 정렬
- `amount`, `customerId` Comparator 정렬

## 규칙
- raw type은 실험 코드 외에는 금지
- `@SuppressWarnings` 남발 금지 (필요하면 이유 주석)
- null 반환 금지 (빈 컬렉션 또는 예외)
- 내부 컬렉션 직접 노출 금지
- 테스트 코드에서 "왜 이 타입 시그니처가 필요한지"가 드러나게 작성

## 오늘의 학습 포인트
#### 반드시 막히는 지점
- `? extends T`인데 왜 `add()`가 안 되지?
- `? super T`는 왜 읽으면 `Object`처럼 느껴지지?
- `Comparable<T>` vs `Comparator<T>`는 언제 각각 써야 하지?
- 제네릭이 문법이 아니라 "API 설계 도구"라는 게 무슨 의미지?

## 제출 체크리스트
- `GenericUtils` 구현 완료
- 선택 도메인 + 정렬 유틸 구현 완료
- JUnit 테스트 5개 이상 통과
- PECS/타입 안정성 설명 기록 완료
- raw type 위험성 실험 기록 완료

