import exception.ErrorMessages;

import java.util.Comparator;
import java.util.List;

public class GenericUtils<T extends Comparable<? super T>> {

    /**
     * src가 ? extends T인 이유는 T의 하위클래스 어떤 것이든 받을 수 있도록 하기 위해서다.
     * 즉 T가 가지고 있는 기능을 갖추고 있다면 사용해도 괜찮다는 뜻이다.
     * 반대로 dest의 경우 T의 상위클래스라면 받을 수 있도록 명시하여 어떤 값이 들어올지 모르는 경우 조금 더 상위 클래스로 명시해놓기 위해 사용한다.
     * List<Object> 와 List<String> 이 대체될 수 없는 이유는 제네릭 클래스에 한해서는 공변성을 지원해주지 않기 때문이다.
     * 이 공변성을 허용해주면 컴파일 단계에서 오류를 찾을 수가 없으므로 ? extends T 등으로 명시해주는 것이 좋다.
     */
    public void copy(List<? super T> dest, List<? extends T> src) {
        if (dest == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "dest"));
        } else if (src == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "src"));
        }
        dest.addAll(src);
    }

    public T max(List<? extends T> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "values"));
        }
        T maxValue = values.get(0);
        for (T value : values) {
            if (maxValue.compareTo(value) < 0) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    public boolean contains(List<?> values, Object target) {
        if (values == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "values"));
        } else if (target == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "target"));
        }
        for (Object value : values) {
            if (target.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public List<T> sortNatural(List<T> values) {
        return values.stream().sorted().toList();
    }

    public List<T> sortWith(List<T> values, Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "comparator"));
        }
        return values.stream().sorted(comparator).toList();
    }
}
