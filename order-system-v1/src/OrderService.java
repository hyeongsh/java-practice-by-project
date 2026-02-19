import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {

    private final Map<String, Order> orderMap = new HashMap<>();

    public void addOrder(Order order) {
        Order prev = orderMap.putIfAbsent(order.getOrderId(), order);
        if (prev != null) {
            throw new DuplicateOrderException("id가 이미 존재합니다.");
        }
    }

    public Order getOrder(String orderId) {
        Order order = this.orderMap.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException("주문이 존재하지 않습니다.");
        }
        return order;
    }

    public long getTotalAmountByCustomer(String customerId) {
        long amount = 0;
        for (Order order : orderMap.values()) {
            if (order.getCustomerId().equals(customerId)) {
                amount += order.getAmount();
            }
        }
        return amount;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderMap.values());
    }
}