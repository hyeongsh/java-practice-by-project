import domain.Order;
import exception.DuplicateOrderException;
import exception.OrderNotFoundException;
import service.OrderService;

import java.util.Comparator;
import java.util.List;

public class Main {

    private static OrderServiceTest orderServiceTest;

    public static void main(String[] args) {
        orderServiceTest = new OrderServiceTest();
        orderServiceTest.executeAllTest();
    }
}
