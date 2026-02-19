# 문제 개선
## 문제 1: 테스트 코드 재설계
### 문제
1. 테스트 코드의 자원이 의존성을 가지고 있었음
2. 테스트 코드가 출력 확인으로 처리하기 때문에 검증력이 약함.
### 원인 x
### 개선
- 테스트 클래스를 생성하여 처리
- assertEquals, assertThrows를 직접 구현해 봄.
- 함수를 Runnable로 만들어서 인자로 사용하여 BeforeTest 및 AfterTest 적용

# 새 개념
## comparator와 comparable의 차이
comparator와 comparable는 둘 다 인터페이스이지만 구현해야 하는 메서드가 다르다.
### Comparable
- -비교할 수 있는- 이라는 의미를 가지고 있는 만큼 다른 객체와 비교 가능하도록 만들어야 한다.
- 오버라이드 해야 하는 메서드는 compareTo(Object obj)로 다른 객체와의 비교를 진행한다.
- 보통은 compareTo(T other) 형태로 구현한다.
- Comparable을 상속받는 클래스는 Collections.sort(list) 사용이 가능하다.
- 즉, 비교 가능하므로 Collections가 이를 이용해 정렬을 진행해줄 수 있다.
### Comparator
- -비교자- 라는 의미를 가지고 있어 두 객체를 비교할 수 있다.
- 오버라이드 해야 하는 메서드는 compare(Object o1, Object o2)로 두 객체의 비교가 가능하다.
- 보통은 compare 메서드를 구현함으로써 Comparator를 상속받는 익명 객체를 생성하여 사용한다.
- 예시) 
```java
list.sort(new Comparator<MyObject>() {
    @Override
    public int compare(MyObject o1, MyObject o2) {
        return Integer.compare(o1.getA(), o2.getA());
    }
});
```
