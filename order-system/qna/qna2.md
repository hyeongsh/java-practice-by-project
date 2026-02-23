# Java 혼합 모의테스트 (개념 + 코드/설계)

목표:
- 자바 기초 ~ 중급 입문(컬렉션/예외/테스트/동시성 기초) 이해도 점검
- 암기보다 "설명 가능성"과 "검증 관점" 확인

진행 방식:
- 각 문항 아래 `내 답안:`에 직접 작성
- 코드 문항은 컴파일되는 완성 코드가 아니어도 됨 (핵심 로직/이유 설명 우선)
- 답안 작성 후 내가 채점 + 약점 분석 + 다음 학습 방향 제안

---

## 1. 개념 (짧은 서술)

### Q1. `synchronized`와 `volatile`의 차이
다음을 각각 1~2문장으로 설명하시오.

1. `synchronized`가 보장하는 것
2. `volatile`이 보장하는 것
3. `volatile`로 해결되지 않는 대표 사례 1개

내 답안:
1. synchronized는 block 단위로 놓을 수도 있고, 메서드 단위로도 놓을 수도 있는데, 공통적으로 보장하는 건 원자성이 보장된다는 것이다. synchronized로 보장된 영역은 unlock 될 때까지 다른 쓰레드가 침범할 수 없다.
2. volatile은 가시성을 보장한다. JMM의 happens-before을 보장하여 변경된 값을 반드시 읽을 수 있도록 보장해준다.
3. volatile은 원자성을 보장해주지 않으므로 volatile 변수의 ++ 연산은 보장받을 수 없다. 보기엔 하나의 작업처럼 보이지만 읽은 후에 해당 값에 +1을 해서 값을 쓰므로 단일 작업이 아니다.

---

### Q2. `ExecutorService.submit()` vs `execute()`
둘의 차이를 설명하고, 각각 언제 쓰면 좋은지 예시를 1개씩 쓰시오.

내 답안:
ExecutorService의 submit만 안다..


---

### Q3. `equals()` / `hashCode()`가 중요한 이유
`HashSet<Order>` 또는 `HashMap<String, Order>` 같은 컬렉션과 연결해서 설명하시오.

내 답안:
Order는 커스텀 객체이므로 equals()가 오버라이딩 되어 있지 않다면 Object의 기본 구현인 주소값의 비교를 진행하므로 set과 map의 예상치 못한 동작이 생길 수 있다.
또한 hashCode가 오버라이딩 되어 있지 않다면 주소값 기준으로 해시값을 생성한 정수를 반환하기 때문에, 같은 값을 생성해서 비교하더라도 주소값이 다르다면 원하는 결과를 얻을 수 없게 된다.

---

### Q4. 예외 설계
아래 상황에서 왜 `IllegalArgumentException`과 도메인 예외(예: `OrderNotFoundException`)를 구분하는지 설명하시오.

- `getOrder(null)`
- `getOrder("없는주문ID")`

내 답안:
예외 처리는 언제나 알아볼 수 있게 되어야 한다.
만약 두 상황을 같은 예외로 처리할 경우 저장공간에 존재하지 않는 것인지, 메서드로 넘어오기 전에 오류가 발생한 것인지 알 수 없다.
따라서 상황마다 다르게 예외를 발생시켜 오류를 쉽게 찾을 수 있도록 해야 한다.

---

### Q5. 테스트 관점
"재현 가능한 멀티스레드 테스트"를 만들 때 왜 실행 순서 자체를 고정하려고 하기보다, 최종 상태/예외 개수 같은 불변식(invariant)을 검증해야 하는지 설명하시오.

내 답안:
멀티스레드 방식은 컨텍스트 스위칭 상태에 따라 실행 순서를 임의로 고정할 수가 없다.
즉, A -> B 순서일 경우 특정 결과가 나온다고 테스트해서는 안되고, A, B를 실행시켰을 때 최종 결과가 어떤지를 검증해야만 예상치 못한 오류를 막을 수 있다.

---

## 2. 코드 읽기 / 버그 찾기

### Q6. 동시성 버그 찾기
아래 코드의 문제점을 2개 이상 찾고, 왜 문제가 되는지 설명하시오.

```java
public class CounterService {
    private volatile int count = 0;

    public void increase() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
```

내 답안:
1. 동시성 프로그램을 구성한다면 read, write를 하는 메서드에 원자성을 보장하기 위해 synchronized 키워드를 사용하는 것이 옳다.
2. count++은 여러 연산이 포함되어 있으나 volatile은 원자성을 보장하지 않는다. volatile은 flag 역할로써 사용하는 것이 옳다.

---

### Q7. 컬렉션 일관성 버그 찾기
아래 코드에서 멀티스레드 환경 기준으로 어떤 문제가 생길 수 있는지 설명하고, 락을 어디까지 묶어야 하는지 쓰시오.

```java
public void addOrder(Order order) {
    synchronized (orderMap) {
        if (orderMap.containsKey(order.getOrderId())) {
            throw new DuplicateOrderException(...);
        }
        orderMap.put(order.getOrderId(), order);
    }

    ordersByCustomer
        .computeIfAbsent(order.getCustomerId(), k -> new ArrayList<>())
        .add(order);

    orderSet.add(order);
}
```

내 답안:
orderMap과 ordersByCustomer, orderSet은 같은 데이터를 다루고 있으므로 정합성이 보장되어야 한다.
즉, 모든 객체가 원자적으로 돌아야 하지만, 지금 방식으로 orderMap에 값을 넣은 후에 락을 회수하므로 ordersByCustomer에 넣는 과정에서 ordersByCustomer를 읽으면 값이 들어가기 전에 읽힐 수 있다.
따라서 ordersByCustomer와 orderSet에 추가할 때도 락을 같이 묶어야 한다.

---

## 3. 코드/설계 작성 (핵심 로직 위주)

### Q8. 중복 주문 동시 추가 테스트 설계
요구사항:
- 같은 `orderId`를 20개 스레드가 동시에 `addOrder()` 호출
- 기대 결과:
  - 성공 1회
  - `DuplicateOrderException` 19회
  - 최종 주문 수 1개 증가

질문:
- `ExecutorService`, `CountDownLatch`, `Future`를 어떻게 사용할지 테스트 흐름을 단계별로 설명하시오.
- 가능하면 의사코드(pseudocode)도 함께 작성하시오.

내 답안:
1. 우선 countDownLatch의 개수를 20으로 설정한 후, countDown() 메서드를 통해 addOrder를 못하도록 막는다.
2. countdown이 끝나면 동시에 실행한 후 그 결과를 futures에 담는다.
3. futures를 돌면서 예외 발생 시 DuplicateOrderException인지 확인하고 개수를 체크한다.
4. 성공 1회, 실패 19회, 기존 order보다 하나 더 늘었는지 확인한다.
```
countDownLatch = new countDownLatch(20);
List<Future<?>> futures = new ArrayList<>();
for (int i = 0; i < 20; i++) {
  futures.add(executorService.submit(() -> {
    countDownLatch.countDown();
    orderService.addOrder(order);
  }));
}

int orderCount = orderService.getAllOrders().size();
int addCount = 0;
int exceptionCount = 0;
for (int i = 0; i < 20; i++) {
  try {
    futures.get(i).get();
    addCount++;
  } catch (ExecutionException ex) {
    if (DuplicateOrderException.class.equals(ex.getCause().getClass())) {
      exceptionCount++;
    } else {
      fail();
    }
  }
}
assertEquals(1, addCount);
assertEquals(19, exceptionCount);
assertEquals(orderCount + 1, orderService.getAllOrders().size());
```




---

### Q9. `volatile` 가시성 예시 설계
`shutdownRequested` 같은 플래그를 사용해서 "가시성"을 보여주는 예시를 설계하시오.

포함할 것:
- 어떤 스레드가 값을 쓰는지
- 어떤 스레드가 값을 읽는지
- 왜 `volatile`이 필요한지
- 왜 이걸로 원자성을 설명하면 안 되는지

내 답안:
메인 스레드가 값을 쓰고, 자식 스레드가 값을 읽는다.
volatile를 사용하지 않을 경우 변수를 지역변수로 최적화하여 무한루프가 돌 수도 있으며, 메인 스레드에서 변경한 값이 자식 스레드에서 반영이 안될 수도 있다.
이는 가시성과 관련된 내용으로 값에 1을 더하는 식으로 여러 연산을 진행할 때 다른 쓰레드가 끼어드는 걸 막아주지는 못한다.
```
class Shutdown {
  volatile boolean shutdownRequested = false;

  void requestShutdown() {
    shutdownRequested = true;
  }
  
  boolean isShutdown() {
    return shutdownRequested;
  }
}

Future<?> future = executorService.submit(() -> {
  while (!shutdown.isShutdown()) {
    continue ;
  }
});

shutdown.requestShutdown();
future.get();

```

---

### Q10. API/서비스 설계 판단
`OrderService`의 메서드 중 하나를 골라 아래를 설명하시오.

- 입력 검증은 어디서/왜 하는지
- 예외를 어떤 기준으로 던질지
- 반환 타입을 왜 그렇게 선택했는지 (예: 내부 리스트 그대로 반환 vs 복사본 반환)
- 멀티스레드 환경에서 읽기 메서드도 락이 필요한지 여부

대상 메서드 예시:
- `getOrdersByCustomer(String customerId)`
- `getAllOrders()`
- `getOrder(String orderId)`

내 답안:
getOrdersByCustomer
입력검증은 제일 처음에 진행하며 customerId가 null이거나 빈값이 들어올 경우, 메서드 로직의 문제가 아닌 입력의 문제임을 명시하기 위해 추가한다.
예외는 입력이 잘못될 경우, 해당 customerId에 맞는 Order가 존재하지 않는 경우 예외를 발생시킨다.
반환 타입은 내부 Map 객체를 리스트화 하여 반환하며, 해당 값들을 복사하지는 않는다. Order는 불변 객체이기 때문에 객체를 수정할 수 없으며 외부 리스트에서 넣고 빼는 작업이 내부 Map에 영향을 주지 않기 때문이다.
멀티스레드 환경에서 읽기 메서드에도 락이 필요하다. 읽기 시작한 시점에서는 10개가 들어있었는데, 읽는 중에 쓰기 작업을 통해 11개로 늘어날 가능성을 배제할 수 없기 때문이다.

---

## 채점 기준 (참고)
- 정확성: 개념을 틀리지 않게 설명했는가
- 이유 설명: "왜"를 말했는가
- 테스트 관점: 재현성/검증 포인트를 잡았는가
- 동시성 관점: 원자성/가시성/일관성을 구분했는가

---

# 채점 및 피드백 (Codex)

## 총평
- 전체적으로 **동시성 기초(`synchronized`, `volatile`, 테스트 관점)** 이해가 좋음
- 특히 `volatile`의 가시성 vs 원자성 구분, 컬렉션 일관성 문제 인식은 잘 잡음
- 약점은 `ExecutorService API 차이`, 테스트 배리어 사용 디테일, 서비스 예외 정책/반환 정책의 설계 기준 명확화 쪽

## 대략 점수 (100점 만점)
- **78/100**

## 문항별 코멘트
- Q1: 좋음. `volatile`의 한계(`++`)까지 정확함. `synchronized`는 원자성뿐 아니라 가시성도 보장한다는 점까지 쓰면 더 좋음.
- Q2: 미응답. 이번 점검에서 가장 약한 부분.
- Q3: 좋음. `equals/hashCode`와 해시 컬렉션 동작 연결이 잘 됨.
- Q4: 좋음. 다만 "입력 오류 vs 도메인 상태(없음)"를 더 명확히 분리하면 더 좋음.
- Q5: 좋음. 실행 순서 고정보다 invariant 검증이라는 핵심을 잘 이해함.
- Q6: 대체로 맞음. 다만 해결책을 `synchronized`로만 고정하기보다 `AtomicInteger`도 대표 해법으로 제시하면 더 좋음.
- Q7: 좋음. 다만 "각 컬렉션별 락"보다 "공통 락 1개"가 더 적절하다는 점을 명시하면 더 좋음.
- Q8: 방향은 좋음. 하지만 현재 의사코드는 `CountDownLatch.await()`가 빠져 있어 동시 시작 보장이 약함.
- Q9: 좋음. 가시성/최적화/무한루프 가능성 설명이 괜찮음. `future.get(timeout)`까지 쓰면 테스트 안전성이 올라감.
- Q10: 대부분 좋음. 다만 `getOrdersByCustomer`에서 "해당 고객 주문 없음"을 예외로 볼지 빈 리스트로 볼지 정책 선택을 명확히 해야 함(현재 구현은 보통 빈 리스트 반환 정책이 더 자연스러움).

## 약한 부분 요약 (다음 학습에 반영)
- `ExecutorService` 핵심 API 차이 (`submit`, `execute`, `invokeAll`, `shutdown`, `awaitTermination`)
- 멀티스레드 테스트 설계 디테일 (`CountDownLatch`, `CyclicBarrier`, `Future` 예외 수집)
- 서비스 설계 정책 구분 (입력 오류 / 도메인 상태 / 조회 결과 없음의 반환 전략)
- 동시성 해결책 선택지 비교 (`synchronized` vs `Atomic*` vs concurrent collections)

---

# 모범답안 (참고)

주의:
- 정답은 하나가 아니며, 아래는 "과제/실무 기준으로 안전한 답안 예시"임

## Q1 모범답안
1. `synchronized`는 임계구역에 대한 **상호배제(원자성에 가까운 실행 보장)** 와 **가시성**을 보장한다. 같은 락을 사용하는 코드끼리는 한 번에 한 스레드만 진입할 수 있다.
2. `volatile`은 해당 변수에 대한 **가시성**을 보장한다. 한 스레드의 `volatile` 쓰기는 다른 스레드의 `volatile` 읽기에서 관찰 가능하며(`happens-before`), 최신 값을 보게 한다.
3. `volatile`로 해결되지 않는 대표 사례는 `count++` 같은 복합 연산이다. 읽기-계산-쓰기로 나뉘므로 원자성이 보장되지 않는다.

## Q2 모범답안
- `execute(Runnable)`
  - 반환값 없음 (`void`)
  - 작업 결과를 받을 필요가 없고, "그냥 실행"이 목적일 때 사용
  - 예: 로그 전송, 캐시 정리 트리거
- `submit(...)`
  - `Future` 반환
  - 결과값/예외/완료 대기가 필요할 때 사용
  - 예: 비동기 계산 결과 받아오기, 테스트에서 각 작업 예외 수집

추가:
- `submit(Runnable)`도 `Future<?>`를 반환하며, 예외는 `Future.get()` 시 `ExecutionException`으로 확인 가능

## Q3 모범답안
`HashSet`/`HashMap`은 해시 기반 컬렉션이므로, "같은 객체로 취급할 기준"이 `equals()`와 `hashCode()` 계약에 맞아야 한다.

- `equals()`만 바꾸고 `hashCode()`를 안 바꾸면 같은 값인데 다른 버킷에 들어갈 수 있어 컬렉션 동작이 깨질 수 있다.
- 예를 들어 `HashSet<Order>`에서 `orderId`가 같은 주문을 중복으로 막고 싶다면, `equals()`와 `hashCode()` 모두 같은 기준(`orderId`)으로 구현해야 한다.

## Q4 모범답안
- `getOrder(null)`은 메서드 호출자의 **입력 자체가 잘못된 경우**이므로 `IllegalArgumentException`
- `getOrder("없는주문ID")`는 입력 형식은 맞지만 **도메인 상태상 데이터가 없는 경우**이므로 `OrderNotFoundException`

이렇게 구분하면:
- 호출자 버그(입력 검증 실패)와
- 비즈니스 상태(조회 결과 없음)
를 다르게 처리/로그/복구할 수 있다.

## Q5 모범답안
멀티스레드에서는 실행 순서를 완전히 고정하기 어렵고, 고정하려고 하면 테스트가 구현 세부사항에 과하게 의존하게 된다. 대신 **최종 상태, 예외 개수, 일관성 규칙(invariant)** 을 검증하면 스케줄링이 달라도 테스트가 재현 가능하고 의미 있는 검증이 된다.

예:
- 성공 1회 / 중복 예외 19회
- 최종 주문 수 정확성
- 고객별 합계 일관성

## Q6 모범답안
문제점(2개 이상):
1. `count++`는 원자적이지 않다.
- `volatile`이 있어도 여러 스레드가 동시에 증가시키면 증가분이 유실될 수 있다.
2. `volatile`은 가시성만 보장한다.
- 이 코드는 "최신 값 읽기"는 일부 보조하지만 "정확한 카운트"를 보장하지 못한다.
3. 동시성 카운터 의도라면 설계가 부적절하다.
- 정확한 증가가 필요하면 `AtomicInteger.incrementAndGet()` 또는 `synchronized`가 필요하다.

개선 예시:
- `private final AtomicInteger count = new AtomicInteger();`
- `count.incrementAndGet();`
- `count.get();`

## Q7 모범답안
문제:
- `orderMap`만 락으로 보호하고, `ordersByCustomer`와 `orderSet` 갱신은 락 밖에서 수행하므로 컬렉션 간 일관성이 깨질 수 있다.
- 예: 어떤 스레드는 `orderMap`에는 주문이 보이는데 `orderSet`에는 아직 없는 중간 상태를 읽을 수 있다.

핵심:
- 이 3개 컬렉션은 하나의 논리 상태를 구성하므로 **공통 락 1개**로 함께 보호해야 한다.

권장 방식:
- `private final Object orderLock = new Object();`
- `synchronized (orderLock) { ... orderMap / ordersByCustomer / orderSet 모두 갱신 ... }`

## Q8 모범답안
테스트 흐름(단계별):
1. `ExecutorService` 생성 (예: 고정 스레드풀 20)
2. `CountDownLatch startLatch = new CountDownLatch(20)` 생성
3. 각 작업에서:
- `startLatch.countDown()`
- `startLatch.await()`로 모든 스레드가 준비될 때까지 대기
- 같은 `orderId`로 `addOrder()` 호출
4. 각 `submit()` 반환 `Future`를 리스트에 저장
5. `Future.get()`를 모두 호출하면서:
- 성공 카운트 증가
- `ExecutionException`의 cause가 `DuplicateOrderException`이면 중복 카운트 증가
- 예상 외 예외는 테스트 실패
6. 최종 상태 검증
- 성공 1회
- 중복 19회
- 최종 주문 수 1개 증가
7. `shutdown()` + `awaitTermination()`

의사코드 예시:
```java
int threads = 20;
ExecutorService es = Executors.newFixedThreadPool(threads);
CountDownLatch startLatch = new CountDownLatch(threads);
List<Future<?>> futures = new ArrayList<>();

for (int i = 0; i < threads; i++) {
    futures.add(es.submit(() -> {
        startLatch.countDown();
        startLatch.await();
        orderService.addOrder(sameOrder);
        return null;
    }));
}

int success = 0;
int duplicate = 0;
for (Future<?> f : futures) {
    try {
        f.get();
        success++;
    } catch (ExecutionException ex) {
        if (ex.getCause() instanceof DuplicateOrderException) {
            duplicate++;
        } else {
            fail("unexpected", ex.getCause());
        }
    }
}

assertEquals(1, success);
assertEquals(19, duplicate);
assertEquals(before + 1, orderService.getAllOrders().size());

es.shutdown();
assertTrue(es.awaitTermination(1, TimeUnit.SECONDS));
```

## Q9 모범답안
설계:
- 메인 스레드: `requestShutdown()`으로 `shutdownRequested = true` 쓰기
- worker 스레드: `while (!shutdownRequested)` 루프에서 읽기

왜 `volatile` 필요?
- 메인 스레드의 변경값이 worker 스레드에서 보이도록 가시성을 보장해야 하기 때문
- `volatile` 쓰기/읽기 사이 `happens-before` 관계가 생겨 상태 전파를 보장한다

왜 원자성 설명에 쓰면 안 되나?
- 이 예시는 단순 플래그 읽기/쓰기(단일 연산)라서 원자성 문제가 핵심이 아니다
- `++`, `+=` 같은 복합 연산은 `volatile`만으로 안전하지 않다

테스트 보완 포인트:
- `Future.get(timeout)` 또는 `awaitTermination()`으로 무한 대기 방지

## Q10 모범답안 (`getOrdersByCustomer`)
- 입력 검증
  - 메서드 시작 시 `customerId`가 `null`/blank인지 검사
  - 입력 오류는 로직 수행 전에 즉시 실패시키는 것이 책임 분리에 좋음
- 예외 기준
  - `null`/blank: `IllegalArgumentException`
  - "해당 고객 주문 없음": 정책에 따라 **빈 리스트 반환**(조회 API에서 흔한 선택)
- 반환 타입 선택
  - 내부 컬렉션 그대로 반환하지 않고 **복사본 리스트** 반환
  - 외부에서 `add/remove` 하더라도 내부 상태가 오염되지 않음
  - `Order`가 불변이면 요소 자체 변경 위험은 낮지만, 컬렉션 구조 보호는 여전히 필요
- 읽기 메서드 락 필요 여부
  - 멀티스레드에서 쓰기와 동시에 읽을 수 있으므로, 일관성/중간상태 노출 방지를 위해 읽기에도 같은 락 정책을 적용하는 것이 안전함

