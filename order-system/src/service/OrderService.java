package service;

import domain.Order;
import exception.DuplicateOrderException;
import exception.OrderNotFoundException;

import java.util.*;

public class OrderService {

    private final Map<String, Order> orderMap = new HashMap<>();
    private final Set<Order> orderSet = new HashSet<>();
    private final Map<String, List<Order>> ordersByCustomer = new HashMap<>();

    public void addOrder(Order order) {
        Order prev = orderMap.putIfAbsent(order.getOrderId(), order);
        if (prev != null) {
            throw new DuplicateOrderException("id가 이미 존재합니다.");
        }
        List<Order> orders = ordersByCustomer.computeIfAbsent(order.getCustomerId(), k -> new ArrayList<>());
        orders.add(order);
        orderSet.add(order);
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
        List<Order> orders = ordersByCustomer.get(customerId);
        if (orders == null) {
            return amount;
        }
        for (Order order : orders) {
            amount += order.getAmount();
        }
        return amount;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderMap.values());
    }

    public boolean containsOrder(Order order) {
        return orderSet.contains(order);
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return new ArrayList<>(ordersByCustomer.getOrDefault(customerId, List.of()));
    }

    public List<Order> getOrdersSorted(Comparator<Order> c) {
        ArrayList<Order> orders = new ArrayList<>(orderMap.values());
        if (c == null) {
            Collections.sort(orders);
        } else {
            orders.sort(c);
        }
        return orders;
    }

    public void clear() {
        this.orderMap.clear();
        this.orderSet.clear();
        this.ordersByCustomer.clear();
    }

}