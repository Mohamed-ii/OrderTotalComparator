import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Store {

    private final List<Product> productList = new ArrayList<>();
    private final Map<Integer, Product> productById = new HashMap<>();
    private final Set<String> categories = new LinkedHashSet<>();

    private final Map<Integer, Order> allOrders = new LinkedHashMap<>();
    private final Deque<Order> shippingQueue = new ArrayDeque<>();
    private final Map<Integer, Order> deliveredOrders = new LinkedHashMap<>();

    private final List<Review> reviews = new ArrayList<>();

    private void deleteProductEverywhere(int id) {
        Product removed = productById.remove(id);
        if (removed != null) {
            productList.remove(removed);
        }
    }

    public void addProduct(int id, String name, double price, String category, int stockQuantity) {
        if (productById.containsKey(id)) {
            System.out.println("A product with ID " + id + " already exists. Product IDs must be unique.");
            return;
        }
        Product product = new Product(id, name, price, category, stockQuantity);
        productList.add(product);
        productById.put(id, product);
        categories.add(category);
        System.out.println("Product added: " + product);
    }

    public void removeProduct(int id) {
        if (!productById.containsKey(id)) {
            System.out.println("No product found with ID " + id + ".");
            return;
        }
        deleteProductEverywhere(id);
        System.out.println("Product " + id + " removed.");
    }

    public void displayAllProducts() {
        if (productList.isEmpty()) {
            System.out.println("No products in the shop.");
            return;
        }
        for (Product p : productList) {
            System.out.println(p);
        }
    }

    public void searchProductById(int id) {
        Product p = productById.get(id);
        if (p == null) {
            System.out.println("No product found with ID " + id + ".");
        } else {
            System.out.println(p);
        }
    }

    public void showAllCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories registered yet.");
            return;
        }
        for (String c : categories) {
            System.out.println("- " + c);
        }
    }

    public void displayProductsOrderedByPrice() {
        List<Product> copy = new ArrayList<>(productList);
        Collections.sort(copy);
        for (Product p : copy) {
            System.out.println(p);
        }
    }


    public Product findProductById(int id) {
        return productById.get(id);
    }

    public void removeOutOfStockProducts() {
        int countBefore = productList.size();
        Iterator<Product> it = productList.iterator();
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getStockQuantity() == 0) {
                it.remove();
                productById.remove(p.getId());
            }
        }
        int removed = countBefore - productList.size();
        System.out.println(removed + " out-of-stock product(s) removed.");
    }


    public void createOrder(int orderId, String customerName) {
        if (allOrders.containsKey(orderId)) {
            System.out.println("An order with ID " + orderId + " already exists. Order IDs must be unique.");
            return;
        }
        Order order = new Order(orderId, customerName);
        allOrders.put(orderId, order);
        System.out.println("Order created: " + order);
    }

    public void addItemToOrder(int orderId, int productId, int quantity) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        Product product = productById.get(productId);
        if (product == null) {
            System.out.println("No product found with ID " + productId + ". Item not added.");
            return;
        }
        try {
            order.addItem(product, quantity);
            System.out.println("Item added. New total: $" + String.format("%.2f", order.getTotal()));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeItemFromOrder(int orderId, int productId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        try {
            boolean removed = order.removeItem(productId);
            if (removed) {
                System.out.println("Item removed. New total: $" + String.format("%.2f", order.getTotal()));
            } else {
                System.out.println("That product is not in this order.");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayOrder(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        order.displayOrder();
    }

    public void addOrderToShippingList(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            System.out.println("Only a Pending order can be added to the shipping list (this one is " + order.getStatus() + ").");
            return;
        }
        if (order.getItems().isEmpty()) {
            System.out.println("An order with no items cannot be placed in the shipping list.");
            return;
        }
        shippingQueue.offer(order);
        order.updateStatus(OrderStatus.SHIPPED);
        System.out.println("Order " + orderId + " added to the shipping list.");
    }

    public void shipNextOrder() {
        if (shippingQueue.isEmpty()) {
            System.out.println("The shipping list is empty.");
            return;
        }
        Order next = shippingQueue.peek();
        if (next.getItems().isEmpty()) {
            System.out.println("This order has no items and cannot be shipped.");
            return;
        }
        shippingQueue.poll();
        next.updateStatus(OrderStatus.DELIVERED);
        deliveredOrders.put(next.getOrderId(), next);
        System.out.println("Order " + next.getOrderId() + " delivered.");
    }

    public void cancelOrder(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
            return;
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            System.out.println("A delivered order cannot be cancelled.");
            return;
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println("This order is already cancelled.");
            return;
        }
        if (order.getStatus() == OrderStatus.SHIPPED) {
            shippingQueue.remove(order);
        }

        order.updateStatus(OrderStatus.CANCELLED);
        System.out.println("Order " + orderId + " cancelled.");
    }

    public void searchOrderById(int orderId) {
        Order order = allOrders.get(orderId);
        if (order == null) {
            System.out.println("No order found with ID " + orderId + ".");
        } else {
            System.out.println(order);
        }
    }

    public void displayOrdersOrderedByTotal() {
        List<Order> copy = new ArrayList<>(allOrders.values());
        copy.sort(new OrderTotalComparator());
        for (Order o : copy) {
            System.out.println(o);
        }
    }


    public void addReview(int productId, String customerName, String comment) {
        if (!productById.containsKey(productId)) {
            System.out.println("No product found with ID " + productId + ". Review not added.");
            return;
        }
        reviews.add(new Review(productId, customerName, comment));
        System.out.println("Review added.");
    }

    public void showAllReviewsForProduct(int productId) {
        boolean found = false;
        for (Review r : reviews) {
            if (r.getProductId() == productId) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reviews found for product " + productId + ".");
        }
    }
}
