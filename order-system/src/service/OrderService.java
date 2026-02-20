package service;

import domain.Order;
import exception.DuplicateOrderException;
import exception.ErrorMessages;
import exception.OrderNotFoundException;
import exception.OrderParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class OrderService {

    private final Map<String, Order> orderMap = new HashMap<>();
    private final Set<Order> orderSet = new HashSet<>();
    private final Map<String, List<Order>> ordersByCustomer = new HashMap<>();

    public void loadOrders(Path path) throws OrderParseException, IOException {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();
            int i = 1;
            while (line != null) {
                String[] strs = line.split(",", -1);
                if (strs.length != 3) {
                    throw new OrderParseException(String.format(ErrorMessages.PARSE_ERROR, i));
                }
                String orderId = strs[0];
                String customerId = strs[1];
                try {
                    long amount = Long.parseLong(strs[2]);
                    this.addOrder(new Order(orderId, customerId, amount));
                } catch (IllegalArgumentException ex) {
                    throw new OrderParseException(String.format(ErrorMessages.PARSE_ERROR, i));
                }
                line = br.readLine();
                i++;
            }
        }
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "order"));
        }
        Order prev = orderMap.putIfAbsent(order.getOrderId(), order);
        if (prev != null) {
            throw new DuplicateOrderException(String.format(ErrorMessages.DUPLICATE_ORDER, prev.getOrderId()));
        }
        List<Order> orders = ordersByCustomer.computeIfAbsent(order.getCustomerId(), k -> new ArrayList<>());
        orders.add(order);
        orderSet.add(order);
    }

    public Order getOrder(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "orderId"));
        }
        Order order = this.orderMap.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderId));
        }
        return order;
    }

    public long getTotalAmountByCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "customerId"));
        }
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
        if (order == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "order"));
        }
        return orderSet.contains(order);
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "customerId"));
        }
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