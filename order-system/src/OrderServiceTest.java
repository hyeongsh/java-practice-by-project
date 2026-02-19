import domain.Order;
import exception.DuplicateOrderException;
import exception.OrderNotFoundException;
import service.OrderService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class OrderServiceTest {

    private final OrderService orderService = new OrderService();

    public OrderServiceTest() { }

    public void executeAllTest() {
        run("인덱스기반_고객별_합계_계산", this::인덱스기반_고객별_합계_계산);
        run("중복주문_추가시도_예외", this::중복주문_추가시도_예외);
        run("없는_주문_조회_예외", this::없는_주문_조회_예외);
        run("중복_판단_확인", this::중복_판단_확인);
        run("고객별_주문_리스트_조회", this::고객별_주문_리스트_조회);
        run("정렬_결과_확인", this::정렬_결과_확인);
        run("반환_리스트_수정_영향_확인", this::반환_리스트_수정_영향_확인);
    }

    public void run(String testName, Runnable testTicket) {
        주문_3개_추가();
        try {
            testTicket.run();
            System.out.println(testName + " OK");
        } catch (AssertionError ex) {
            ex.printStackTrace();
            System.out.println(testName + " FAIL");
        }
    }

    public void 주문_3개_추가() {
        orderService.clear();
        Order order1 = new Order("order1", "customer1", 20L);
        Order order2 = new Order("order2", "customer1", 40L);
        Order order3 = new Order("order3", "customer2", 50L);

        // test1 : 주문 3개 추가
        orderService.addOrder(order1);
        orderService.addOrder(order2);
        orderService.addOrder(order3);
    }

    public void 인덱스기반_고객별_합계_계산() {
        long customer1Amount = orderService.getTotalAmountByCustomer("customer1");
        long customer2Amount = orderService.getTotalAmountByCustomer("customer2");
        assertEquals(62L, customer1Amount);
        assertEquals(50L, customer2Amount);
    }

    public void 중복주문_추가시도_예외() {
        Order order4 = new Order("order1", "customer2", 10L);
        assertThrows(DuplicateOrderException.class, () -> orderService.addOrder(order4));
    }

    public void 없는_주문_조회_예외() {
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder("order5"));
    }

    public void 중복_판단_확인() {
        Order order1 = new Order("order1", "customer1", 20L);
        assertEquals(true, orderService.containsOrder(order1));
        Order order5 = new Order("order5", "customer1", 40L);
        assertEquals(false, orderService.containsOrder(order5));
    }

    public void 고객별_주문_리스트_조회() {
        List<Order> customer2orders = orderService.getOrdersByCustomer("customer2");
        assertEquals(1, customer2orders.size());
    }

    public void 정렬_결과_확인() {
        List<Order> sortedOrder1 = orderService.getOrdersSorted(null);
        List<Order> sortedOrder2 = orderService.getOrdersSorted(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Long.compare(o2.getAmount(), o1.getAmount());
            }
        });
        List<Order> sortedOrder3 = orderService.getOrdersSorted(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o1.getCustomerId().compareTo(o2.getCustomerId());
            }
        });
        assertEquals("order1", sortedOrder1.get(0).getOrderId());
        assertEquals(50L, sortedOrder2.get(0).getAmount());
        assertEquals("customer1", sortedOrder3.get(0).getCustomerId());
    }

    public void 반환_리스트_수정_영향_확인() {
        List<Order> allOrders = orderService.getAllOrders();
        allOrders.clear();
        allOrders = orderService.getAllOrders();
        assertEquals(3, allOrders.size());
    }

    private void assertThrows(Class<? extends Throwable> type, Runnable action) {
        try {
            action.run();
            throw new AssertionError("예외가 발생해야 합니다: " + type.getSimpleName());
        } catch (Throwable t) {
            if (!type.isInstance(t)) {
                throw new AssertionError("expected=" + type.getSimpleName() + ", actual=" + t.getClass().getSimpleName());
            }
        }
    }

    private void assertEquals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected=" + expected + ", actual=" + actual);
        }
    }
}
