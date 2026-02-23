
import domain.Order;
import exception.DuplicateOrderException;
import exception.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class JunitTest {

    private final OrderService orderService = new OrderService();

    @BeforeEach
    public void beforeEach() {
        orderService.clear();
        orderService.resetShutdownRequested();
        Order order1 = new Order("order1", "customer1", 20L);
        Order order2 = new Order("order2", "customer1", 40L);
        Order order3 = new Order("order3", "customer2", 50L);

        orderService.addOrder(order1);
        orderService.addOrder(order2);
        orderService.addOrder(order3);
    }

    @DisplayName("인덱스 기반 고백별 합계 계산 테스트")
    @Test
    public void 인덱스기반_고객별_합계_계산() {
        long customer1Amount = orderService.getTotalAmountByCustomer("customer1");
        long customer2Amount = orderService.getTotalAmountByCustomer("customer2");
        assertEquals(60L, customer1Amount);
        assertEquals(50L, customer2Amount);
    }


    @Test
    public void 없는_주문_조회_예외() {
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder("order5"));
    }

    @Test
    public void 중복_판단_확인() {
        Order order1 = new Order("order1", "customer1", 20L);
        assertTrue(orderService.containsOrder(order1));
        Order order5 = new Order("order5", "customer1", 40L);
        assertFalse(orderService.containsOrder(order5));
    }

    @DisplayName("멀티 스레드 동시성 테스트")
    @Test
    public void 멀티스레드_동시성_테스트() throws ExecutionException, InterruptedException {
        Order order4 = new Order("order4", "customer3", 100L);

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        ArrayList<Future<?>> futures = new ArrayList<>();

        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            Runnable runnable = () -> {
                countDownLatch.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                orderService.addOrder(order4);
            };
            futures.add(executorService.submit(runnable));
        }

        int exceptionCount = 0;
        for (int i = 0; i < 100; i++) {
            try {
                futures.get(i).get();
            } catch (ExecutionException ex) {
                if (DuplicateOrderException.class.equals(ex.getCause().getClass())) {
                    exceptionCount++;
                } else {
                    fail();
                }
            }
        }
        assertEquals(99, exceptionCount);

        Callable<List<Order>> callable = () -> orderService.getAllOrders();
        Future<List<Order>> orders = executorService.submit(callable);
        assertEquals(4, orders.get().size());

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS));
    }

    @DisplayName("volatile 가시성 테스트")
    @Test
    public void volatile_테스트() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CountDownLatch started = new CountDownLatch(1);

        Future<?> future = executorService.submit(() -> {
            started.countDown();
            while (!orderService.isShutdownRequested()) {
                Thread.onSpinWait();
            }
        });

        assertTrue(started.await(1, TimeUnit.SECONDS));
        orderService.requestShutdown();
        future.get(1, TimeUnit.SECONDS);
        assertTrue(orderService.isShutdownRequested());

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS));
    }
}
