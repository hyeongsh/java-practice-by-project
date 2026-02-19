public class Main {

    private static OrderService orderService;

    public static void main(String[] args) {
        orderService = new OrderService();

        Order order1 = new Order("order1", "customer1", 20L);
        Order order2 = new Order("order2", "customer1", 40L);
        Order order3 = new Order("order3", "customer2", 50L);

        // test1 : 주문 3개 추가
        orderService.addOrder(order1);
        orderService.addOrder(order2);
        orderService.addOrder(order3);

        // test2 : 고객별 합계 출력
        long customer1Amount = orderService.getTotalAmountByCustomer("customer1");
        long customer2Amount = orderService.getTotalAmountByCustomer("customer2");
        System.out.println("customer1Amount: " + customer1Amount);
        System.out.println("customer2Amount: " + customer2Amount);

        // test3 : 중복 주문 추가 시도 -> 예외 확인
        Order order4 = new Order("order1", "customer2", 10L);
        try {
            orderService.addOrder(order4);
        } catch (DuplicateOrderException ex) {
            System.out.println(ex.getMessage());
        }

        // test4 : 없는 주문 조회 → 예외 확인
        try {
            orderService.getOrder("order5");
        } catch (OrderNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
