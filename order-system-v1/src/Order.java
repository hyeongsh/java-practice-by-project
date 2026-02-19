import java.util.Objects;

public final class Order {
    private final String orderId;
    private final String customerId;
    private final long amount;

    public Order(String orderId, String customerId, long amount) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId");
        } else if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId");
        }
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return this.getOrderId().equals(order.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.orderId);
    }
}