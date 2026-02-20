# 문제 개선
## 문제 1: checked exception 메서드의 테스트
### 문제
- unchecked exception의 경우 반드시 처리할 필요가 없었으므로 기존 구현 방식에 throw를 던지는 방식을 추가하는 것 만으로도 처리가 가능했음
- 하지만 checked exception을 추가하니 throws로 받거나, try-catch문을 반드시 사용해야 해서 기존 코드가 변경되어야 하는 문제가 발생하였음.
### 원인 x
### 개선
- ThrowingRunnable 인터페이스를 구현하여 run() throws Exception을 오버라이드 하였음
- 이 과정에서 Single Abstract Method 인터페이스에 대해 알 수 있었음 (여기에 대해서는 새 개념 파트에서 설명)
- 이를 통해 assertThrows의 경우 checked, unchecked 여부와 상관없이 예외를 던지고 AssertionError로 보낼 수 있게 되었음

## 문제 2: exception의 처리 방식
### 문제
- exception 을 처리하는 단계에서 IOException, OrderParseException, DuplicateOrderException 등 다양한 예외가 하나의 메서드에서 발생
- 이를 전체로 묶어서 throws Exception을 할 경우 상위 메서드에선 해당 예외가 정확히 어떤 예외인지 확인하는 절차를 한번 더 거쳐야했음
### 원인 x
### 개선
- RuntimeException 계열은 그냥 올리고, throws에는 구체적인 클래스를 명시하도록 설계
- 이를 통해 상위 메서드에서 어떤 문제로 예외가 발생하였는지 확인할 수 있도록 설계하였음.

# 새 개념
## SAM 인터페이스
SAM 인터페이스란 Single Abstract Method(추상 메서드 1개)를 가진 인터페이스를 말한다.
이런 인터페이스는 람다로 구현할 수 있다. (추상 메서드가 1개일 때만 가능)
자주 쓰는 예시는 아래와 같다.

```java
ArrayList<MyObject> list = new ArrayList<>();
list.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));

// 위와 같은 경우, 사실 정확한 구현은 다음과 같다.

list.sort(new Comparator<MyObject>() {
    @Override
    public int compare(MyObject o1, MyObject o2) {
        return Integer.compare(o1.getId(), o2.getId());
    }
});
```
이렇게 단 하나뿐인 메서드를 오버라이드하여 인터페이스를 상속받고자 할 때, 간단히 생성할 수 있도록 해준다.
그동안 sort를 사용하며 첫번째 방식을 많이 사용했는데, 왜 가능한지 모르고 사용했으나 이번에 알게 되어 굉장히 유익한 경험이었다.
