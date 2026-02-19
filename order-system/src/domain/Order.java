package domain;

import java.util.Objects;

public final class Order implements Comparable<Order> {
    private final String orderId;
    private final String customerId;
    private final long amount;

    public Order(String orderId, String customerId, long amount) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("orderId");
        } else if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId");
        } else if (amount < 1L) {
            throw new IllegalArgumentException("amount");
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
    public int compareTo(Order other) {
        return this.getOrderId().compareTo(other.getOrderId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Order)) {
            return false;
        }
        Order order = (Order) obj;
        return this.getOrderId().equals(order.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.orderId);
    }
}