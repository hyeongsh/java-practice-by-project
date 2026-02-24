import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GenericTest {

    static class Animal implements Comparable<Animal> {
        Integer age;

        public Animal(Integer age) {
            this.age = age;
        }

        @Override
        public int compareTo(Animal o) {
            return Integer.compare(this.age, o.age);
        }
    }
    static class Dog extends Animal {
        public Dog(Integer age) {
            super(age);
        }
    }
    static class Cat extends Animal {
        public Cat(Integer age) {
            super(age);
        }
    }

    @Test
    public void copyTest() {
        GenericUtils<Animal> animalGenericUtils = new GenericUtils<>();
        Dog dog1 = new Dog(3);
        Dog dog2 = new Dog(4);
        Dog dog3 = new Dog(6);
        Cat cat1 = new Cat(1);
        Cat cat2 = new Cat(2);
        Cat cat3 = new Cat(8);
        List<Dog> dogs = List.of(dog1, dog2, dog3);
        List<Cat> cats = List.of(cat1, cat2, cat3);

        List<Animal> animals = new ArrayList<>();
        animalGenericUtils.copy(animals, dogs);
        animalGenericUtils.copy(animals, cats);

        assertEquals(6, animals.size());
    }

    @Test
    public void maxTest() {
        GenericUtils<Animal> animalGenericUtils = new GenericUtils<>();
        List<Animal> animals = List.of(new Dog(3), new Dog(4), new Dog(6), new Cat(1), new Cat(2), new Cat(8));
        Animal max = animalGenericUtils.max(animals);
        assertEquals(8, max.age);
    }

    @Test
    public void maxFailTest() {
        GenericUtils<Animal> animalGenericUtils = new GenericUtils<>();
        List<Animal> animals = List.of();
        assertThrows(IllegalArgumentException.class, () -> animalGenericUtils.max(animals));
    }

    @Test
    public void containsTest() {
        GenericUtils<Animal> animalGenericUtils = new GenericUtils<>();
        Dog myDog = new Dog(12);
        List<Animal> animals = List.of(new Dog(3), new Dog(4), new Dog(6), new Cat(1), new Cat(2), new Cat(8), myDog);
        assertTrue(animalGenericUtils.contains(animals, myDog));
    }

    @Test
    public void sortTest() {
        GenericUtils<Animal> animalGenericUtils = new GenericUtils<>();
        List<Animal> animals = List.of(new Dog(3), new Dog(4), new Dog(6), new Cat(1), new Cat(2), new Cat(8));
        List<Animal> animals1 = animalGenericUtils.sortNatural(animals);
        List<Animal> animals2 = animalGenericUtils.sortWith(animals, Comparator.comparingInt(o -> o.age));
        assertEquals(1, animals1.get(0).age);
        assertEquals(1, animals2.get(0).age);
    }

    @Test
    public void sortFailTest() {
        GenericUtils<Animal> animalGenericUtils = new GenericUtils<>();
        List<Animal> animals = List.of(new Dog(3), new Dog(4), new Dog(6), new Cat(1), new Cat(2), new Cat(8));
        assertThrows(IllegalArgumentException.class, () -> animalGenericUtils.sortWith(animals, null));
    }

    // list에 String이 들어있음에도 Integer 캐스팅을 컴파일 단계에서 막지 않음
    // 즉, list에 무엇이 추가되는지 쉽게 찾기 어려운 경우 디버깅에 문제를 겪을 수 있음
    @Test
    public void rawTest() {
        List list = new ArrayList();
        list.add(123);
        list.add("abc");
        list.add(48.9);
        assertThrows(ClassCastException.class, () -> {
            int i = (Integer) list.get(1);
        });
    }
}
