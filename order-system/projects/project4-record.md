# 문제 개선
## 문제 1: 동시성 테스트 반영 방식
### 문제
- 동시성을 잘 지키고 있는지 확인하는 테스트의 방식이 재현성이 없었음
### 원인
- 처음 사용해보는 `synchronized` 블록
- `Runnable` 사용 시에도 `ExecutorService.submit()`이 `Future` 객체를 반환한다는 사실을 알지 못했음
### 개선
- 동시성 과정에서 생기는 예외를 `Future.get()`을 통해 받아올 수 있다는 것을 알고 이를 반영하였음
- 처음엔 동시성이 지켜지지 않으면 `getAllOrders()` 수행 도중 `addOrder()`가 끼어들어 결과가 달라지는 상황을 다루고자 하였음
- 하지만 이는 동시성 버그라기보다 실행 순서 차이만으로도 달라질 수 있는 테스트였음 (조회가 먼저/추가가 먼저 실행될 수 있음)
- 따라서 같은 `Order`를 동시에 넣도록 `CountDownLatch`로 시작 시점을 맞추고, 이 과정에서 정확히 1건만 성공하고 99건은 `DuplicateOrderException`이 발생하는지 검증하도록 변경하였음
- 또한 `Future.get()`으로 각 작업의 완료를 기다린 뒤 최종 주문 수를 검증하여 테스트 재현성을 높였음

# 새 개념
## JUnit 테스트를 위한 환경 구축
- JUnit 클래스를 사용해 본 적은 있으나 Spring Boot 프로젝트를 만드는 과정에서 자동으로 포함되어 사용된 터라, 직접 환경을 구축하는 방법을 알지 못했음
- 파일 > 프로젝트 구조로 들어가면 필요한 라이브러리를 추가할 수 있는데, 이 과정에서는 아쉽게도 내가 원하는 라이브러리를 찾을 수가 없었음
- 프로젝트가 `Gradle` 기반일 때는 IntelliJ의 라이브러리 수동 추가보다 `build.gradle`에 의존성을 추가하는 방식이 더 안정적이라는 점을 알게 되었음
- `Gradle` 프로젝트 기반으로 변경하여 설정을 쉽게 하도록 변경하였음
### 변경 방식
1. `build.gradle` 생성
2. `settings.gradle` 생성
3. 소스 폴더 구조를 Gradle 표준으로 변경 (`src/main/java`, `src/test/java`)
4. IntelliJ에서 Gradle refresh
5. 라이브러리를 `build.gradle`에 추가
6. JUnit5 사용 시 `useJUnitPlatform()` 설정

`plugins { id 'java' }`를 넣어야 Java 프로젝트로 구성된다.

## synchronized 메서드와 블록
`synchronized`를 처음 사용해보았는데, 어디에 붙이느냐에 따라 락 대상이 달라진다는 점을 이해하게 되었음.

1. `synchronized` 인스턴스 메서드
- 해당 인스턴스(`this`)의 모니터 락을 사용함
- 같은 인스턴스의 `synchronized` 메서드들끼리는 서로 락을 공유함
- 다른 인스턴스끼리는 락을 공유하지 않음

2. `static synchronized` 메서드
- 인스턴스가 아니라 클래스 객체(`Class`)의 모니터 락을 사용함
- 인스턴스 메서드 락(`this`)과는 별개임

3. `synchronized` 블록
- `synchronized(lockObject)`처럼 명시한 객체의 락을 사용함
- 블록 단위로 임계구역을 최소화할 수 있음

4. 참고
- `synchronized static block`이라는 별도 문법이 있는 것은 아님
- 필요하면 `synchronized (SomeClass.class)`처럼 클래스 객체를 락으로 사용하는 블록을 만들 수 있음

이 과정에서 `synchronized` 블록을 사용했는데, 컬렉션 객체 자체(`orderMap` 등)를 락으로 쓰기보다 락 전용 객체를 따로 생성하는 편이 더 안전하다는 점을 배웠음.

## volatile 사용 방식과 그 의미
처음에는 `volatile`이 원자성까지 보장해준다고 생각하고 `volatile` 변수에 `++`를 사용했음. 하지만 `++`는 읽기 -> 계산 -> 쓰기의 복합 연산이므로 원자적이지 않음.

`volatile`은 원자성을 보장하는 것이 아니라 **가시성(visibility)** 을 보장한다.

- 한 스레드가 쓴 값을 다른 스레드가 읽을 때 최신 값을 볼 수 있도록 함
- `volatile` 쓰기와 이후 다른 스레드의 `volatile` 읽기 사이에 **happens-before** 관계가 성립하여, 쓰기 결과의 관찰 가능성을 보장함
- 하지만 `++`, `+=` 같은 복합 연산을 안전하게 만들어주지는 않음
- 컬렉션의 일관성(`orderMap`, `orderSet`, `ordersByCustomer`)을 보호하는 용도로는 사용할 수 없음
- 또한 `volatile`은 해당 변수 주변의 메모리 연산에 대해 재정렬(reordering)을 제한하는 효과가 있어, 최적화/재배치로 인해 의도와 다르게 동작할 가능성을 줄여줌

이번 과제에서는 `volatile`을 카운터가 아니라 종료 플래그(`shutdownRequested`)에 적용하는 방식이 더 적절하다는 점을 배웠음.

또한 `volatile` 없이도 테스트가 우연히 통과할 수는 있지만, 이는 "이번 실행에서 문제를 재현하지 못한 것"일 뿐이며, Java 메모리 모델 관점에서 가시성이 **보장**되는 코드는 아니라는 점을 이해하게 되었음.
