# project1 학습 기록 (제네릭 / 타입 시스템)

## 제네릭 와일드카드 정리

### `?` (와일드카드)
- `?`는 "아무 타입이나 가능"이라는 의미의 와일드카드이다.
- `List<?>`는 타입을 정확히 모르지만, 어떤 타입의 리스트라는 사실은 보장된다.
- 읽기 중심으로 사용할 때 유용하고, 원소 추가는 거의 불가능하다(`null` 제외).

### `? extends T` (Producer)
- `T` 또는 `T`의 하위 타입을 생산(꺼내기)하는 쪽에 사용한다.
- 예: `copy(List<? super T> dest, List<? extends T> src)`에서 `src`
- `src`에서 꺼낸 값은 최소한 `T`로 다룰 수 있으므로 안전하다.
- 대신 실제 타입이 `T`의 어떤 하위 타입인지 모르기 때문에 `add()`는 제한된다.

### `? super T` (Consumer)
- `T` 또는 `T`의 상위 타입을 소비(넣기)하는 쪽에 사용한다.
- 예: `copy(List<? super T> dest, List<? extends T> src)`에서 `dest`
- `dest`는 `T`를 안전하게 받을 수 있어 `add(T)`가 가능하다.
- 대신 꺼낼 때는 구체 타입을 확신할 수 없어서 보통 `Object`로 다루게 된다.

## PECS를 `copy()`에 적용한 이유
- `src`는 값을 꺼내는 쪽이므로 `? extends T`를 사용한다. (Producer Extends)
- `dest`는 값을 넣는 쪽이므로 `? super T`를 사용한다. (Consumer Super)
- 이 시그니처 덕분에 `List<Dog>` -> `List<Animal>`, `List<Cat>` -> `List<Animal>` 같은 복사가 가능해진다.

## 왜 `List<Object>`와 `List<String>`는 대체 관계가 아닌가?
- 제네릭은 기본적으로 공변(covariant)하지 않고, 불공변(invariant)이다.
- 만약 `List<String>`을 `List<Object>`로 대체 가능하게 허용하면, `List<Object>`로 받은 쪽에서 `Integer`를 넣을 수 있다.
- 그러면 실제 `List<String>` 안에 `Integer`가 들어가 타입 안정성이 깨진다.
- 그래서 Java는 이런 대체를 막고, 대신 `? extends`, `? super` 같은 와일드카드로 안전한 범위만 허용한다.

## raw type 실험과 위험성
- raw type은 제네릭 타입 정보를 생략한 사용 방식이다. 예: `List list = new ArrayList();`
- 컴파일러가 원소 타입을 추적하지 못하므로 서로 다른 타입(`Integer`, `String`, `Double`)이 섞여 들어갈 수 있다.
- 꺼낼 때 캐스팅 실수가 있어도 컴파일 단계에서 막지 못하고, 런타임에 `ClassCastException`이 발생할 수 있다.
- 그래서 raw type은 학습/호환성 목적 외에는 사용하지 않는 것이 좋다.

## raw type 경고가 뜨는 이유
- 제네릭 타입 검사를 건너뛰기 때문에 컴파일러가 타입 안정성을 보장할 수 없다.
- 이 때문에 `raw type warning`, `unchecked warning`이 발생한다.
- 경고를 억지로 숨기기보다, 제네릭 타입을 명시해서 경고 원인을 제거하는 것이 바람직하다.
