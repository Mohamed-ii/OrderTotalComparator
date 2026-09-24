import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Order {
    private int orderId;
    private String customerName;
    private List<CartItem> items;
    private double total;
    private OrderStatus status;

    public Order(int orderId, String customerName) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.status = OrderStatus.PENDING;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void addItem(Product product, int quantity) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify an order that is " + status + ".");
        }
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.increaseQuantity(quantity);
                calculateTotal();
                return;
            }
        }
        items.add(new CartItem(product, quantity));
        calculateTotal();
    }


    public boolean removeItem(int productId) {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot modify an order that is " + status + ".");
        }
        Iterator<CartItem> it = items.iterator();
        while (it.hasNext()) {
            CartItem item = it.next();
            if (item.getProduct().getId() == productId) {
                it.remove();
                calculateTotal();
                return true;
            }
        }
        return false;
    }

    private void calculateTotal() {
        double sum = 0.0;
        for (CartItem item : items) {
            sum += item.calculateSubtotal();
        }
        this.total = sum;
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public void displayOrder() {
        System.out.println("Order #" + orderId + " | Customer: " + customerName + " | Status: " + status);
        if (items.isEmpty()) {
            System.out.println("  (no items)");
        } else {
            for (CartItem item : items) {
                System.out.println(item);
            }
        }
        System.out.printf("  Total: $%.2f%n", total);
    }

    @Override
    public String toString() {
        return String.format("Order #%d | Customer: %s | Status: %s | Total: $%.2f",
                orderId, customerName, status, total);
    }
}
