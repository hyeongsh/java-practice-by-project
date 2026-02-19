# 문제

A. 언어/객체 기본 (6문항)
1.	Java는 call by value라고 하는데, 그 말이 “참조 타입”에서 실제로 무슨 의미인지 설명해줘.
2.	String이 immutable인 이유 2가지를 말해줘(실무적 이유 포함).
3.	아래 코드 출력 결과와 이유를 써줘.
```
Integer a = 127;
Integer b = 127;
Integer c = 128;
Integer d = 128;
System.out.println(a == b);
System.out.println(c == d);
```
	4.	final을 (a) 변수 (b) 객체 (c) 메서드 (d) 클래스에 붙였을 때 의미를 각각 설명해줘.
	5.	오버로딩과 오버라이딩 차이 + 각각이 결정되는 시점(컴파일/런타임)을 써줘.
	6.	static이 “메모리 상”에서 어떤 느낌으로 존재하는지(대략적으로) 설명해줘.

⸻

B. equals / hashCode / 컬렉션 (7문항)
7.	equals()와 hashCode()를 왜 같이 오버라이드해야 하는지, HashMap 관점에서 설명해줘.
8.	HashMap에서 키 조회 과정(대략)을 순서대로 설명해줘.
9.	ArrayList vs LinkedList를 “삽입/삭제/탐색” 관점에서 비교해줘(언제 뭘 쓰는지).
10.	HashSet에서 “중복” 판단이 어떤 순서로 일어나는지 설명해줘.
11.	Comparable vs Comparator 차이 + 언제 쓰는지 예시 1개씩.
12.	Map에서 “키로 객체를 쓰는 경우” 안전하게 쓰기 위한 조건(불변성/equals-hashCode 관련) 써줘.
13.	다음 중 HashMap 성능이 급격히 떨어질 수 있는 상황 2가지를 써줘.

⸻

C. 예외/자원/흐름 (5문항)
14.	Checked vs Unchecked: 각각 언제 쓰는 게 좋은지 기준을 말해줘.
15.	try-with-resources가 해결해주는 문제를 설명해줘.
16.	아래 코드의 문제점을 말하고, 더 좋은 방식(구조)을 한 줄로 제안해줘.
```
try {
  // 파일 읽고 파싱하고 비즈니스 처리까지 전부
} catch (Exception e) {
  System.out.println("error");
}
```
17.	예외를 “삼키는(swallow)” 게 왜 위험한지 2가지.
18.	커스텀 예외를 만들 때 RuntimeException으로 만드는 기준 1개.

⸻

D. JVM/메모리/GC 감각 (5문항)
19.	Stack/Heap/Metaspace에 각각 뭐가 들어가는지 써줘.
20.	Minor GC / Major(Full) GC 차이.
21.	Stop-the-world가 왜 생기고, 왜 문제인지.
22.	“메모리 누수”가 Java에서도 생기는 이유를 예로 설명해줘.
23.	StringBuilder를 쓰는 이유를 String과 비교해서 설명해줘.
⸻

E. 동시성/스레드 (5문항)
24.	synchronized가 보장하는 2가지를 말해줘(힌트: 원자성/가시성).
25.	volatile이 해결하는 것과 해결 못 하는 것을 각각 1개씩.
26.	ExecutorService.submit()과 execute() 차이.
27.	레이스 컨디션 예시 1개를 짧게.
28.	스레드풀에서 “큐가 무한정 쌓이는 문제”가 왜 위험한지.

⸻

F. 미니 코딩 (2문항)
29.	List<domain.Order>에서 customerId별로 합계를 구해 Map<String, Long>으로 만들기(스트림/반복문 아무거나).

- domain.Order: String customerId; long amount;

30.	HashMap 키로 쓸 User(id, name) 클래스를 안전하게 설계해봐(필드/생성자/equals-hashCode만).

# 답

1. call by value는 값을 복사해서 가져오는 방식이야. 이 경우 복사된 값은 변경해도 기존 값에 영향을 주지 못해.
2. 모르겠네.. String이 스택이나 힙에 저장되지 않고 데이터 영역에 저장되나?
3. true, true. 객체와 객체의 비교는 ==를 쓰면 동일 객체인지 판단하지만 Integer는 값의 비교로 오버라이딩 되어 있음
4. 변경불가능한 불변값이 된다.
5. 오버로딩은 특정 메서드의 인자와 반환값을 다양하게 사용하고 싶을 때 사용하고, 오버라이딩은 상위클래스의 메서드를 재정의하고 싶을 때 사용한다. 오버로딩은 런타임 단계에서, 오버라이딩은 컴파일 단계에서 결정된다.
6. 모르겠어..
7. HashMap은 해당 객체의 hashCode값을 equals로 비교하여 값을 찾기 때문에 오버라이드 되어 있어야 HashMap을 사용할 수 있다.
8. HashMap에서 해당 키를 hashCode 값으로 변환한다. -> hashCode 값에 해당하는 인덱스로 이동하여 값을 가져온다.
9. ArrayList는 삽입 삭제가 O(n)만큼 들고, 탐색이 O(1)만큼 소요된다. 반면에 LinkedList는 삽입, 삭제가 O(1)만큼 소요되고, 탐색이 O(n)만큼 소요된다.
10. hashCode값으로 변경 후 해당 인덱스에 값이 존재하면 중복으로 판단한다.
11. 모르겠어..
12. 모르겠어..!
13. HashMap이 허용할 수 있는 양보다 더 많이 넣을 경우.
14. 모르겠어.
15. 자원을 자동으로 해소해줘.
16. 파일을 읽는 과정에서 에러가 터지면 해소하지 못하는 자원이 생길 수도 있어. 그래서 try-with-resources를 쓰는게 좋아.
17. 예외를 삼키면 해당 예외가 왜 발생했는지, 어디서 발생했는지 알기 어려워.
18. 몰라..
19. stack에는 지역변수, 인자 등등 / Heap에는 객체가 할당된 값 등 / Metaspace에는 클래스 정보..?
20. Minor GC는 young에서 자주 삭제, Major GC는 Old에서 제대로 삭제
21. 몰라
22. 자원을 가리키고 있는 댕글링포인터가 존재해서
23. String은 불변값이므로 값을 변경할때는 StringBuilder를 사용한다.
    24.몰라
25. 몰라
26. 몰라
27. 동시 접근
28. 몰라
29. list.stream()....
30. class User { int id; String name; public User(int id, String name) { this.id = id; this.name = name; } public boolean equals(o1, o2) {} public String hashCode() { }