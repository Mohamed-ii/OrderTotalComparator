import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Store store = new Store();

    public static void main(String[] args) {
        loadExampleData();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1: doAddProduct(); break;
                case 2: doRemoveProduct(); break;
                case 3: store.displayAllProducts(); break;
                case 4: doSearchProductById(); break;
                case 5: store.showAllCategories(); break;
                case 6: store.displayProductsOrderedByPrice(); break;
                case 7: doCreateOrder(); break;
                case 8: doAddItemToOrder(); break;
                case 9: doRemoveItemFromOrder(); break;
                case 10: doDisplayOrder(); break;
                case 11: doAddOrderToShippingList(); break;
                case 12: store.shipNextOrder(); break;
                case 13: doCancelOrder(); break;
                case 14: doSearchOrderById(); break;
                case 15: doAddReview(); break;
                case 16: doShowAllReviews(); break;
                case 17: store.removeOutOfStockProducts(); break;
                case 18: store.displayOrdersOrderedByTotal(); break;
                case 19: running = false; System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid option. Please choose 1-19.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println(" E-Commerce Order & Inventory Manager ");
        System.out.println(" 1. Add Product");
        System.out.println(" 2. Remove Product");
        System.out.println(" 3. Display All Products");
        System.out.println(" 4. Search Product by ID");
        System.out.println(" 5. Show All Categories");
        System.out.println(" 6. Display Products Ordered by Price");
        System.out.println(" 7. Create Order");
        System.out.println(" 8. Add Item to Order");
        System.out.println(" 9. Remove Item from Order");
        System.out.println("10. Display Order");
        System.out.println("11. Add Order to the Shipping List");
        System.out.println("12. Ship Next Order");
        System.out.println("13. Cancel Order");
        System.out.println("14. Search Order by ID");
        System.out.println("15. Add Review to a Product");
        System.out.println("16. Show All Reviews for a Product");
        System.out.println("17. Remove Out-of-Stock Products");
        System.out.println("18. Display Orders Ordered by Total");
        System.out.println("19. Exit");

    }


    private static void doAddProduct() {
        int id = readInt("Product ID: ");
        String name = readLine("Name: ");
        double price = readDouble("Price: ");
        String category = readLine("Category: ");
        int stock = readInt("Stock quantity: ");
        store.addProduct(id, name, price, category, stock);
    }

    private static void doRemoveProduct() {
        int id = readInt("Product ID to remove: ");
        store.removeProduct(id);
    }

    private static void doSearchProductById() {
        int id = readInt("Product ID: ");
        store.searchProductById(id);
    }

    private static void doCreateOrder() {
        int id = readInt("New order ID: ");
        String name = readLine("Customer name: ");
        store.createOrder(id, name);
    }

    private static void doAddItemToOrder() {
        int orderId = readInt("Order ID: ");
        int productId = readInt("Product ID: ");
        int quantity = readInt("Quantity: ");
        store.addItemToOrder(orderId, productId, quantity);
    }

    private static void doRemoveItemFromOrder() {
        int orderId = readInt("Order ID: ");
        int productId = readInt("Product ID to remove: ");
        store.removeItemFromOrder(orderId, productId);
    }

    private static void doDisplayOrder() {
        int orderId = readInt("Order ID: ");
        store.displayOrder(orderId);
    }

    private static void doAddOrderToShippingList() {
        int orderId = readInt("Order ID: ");
        store.addOrderToShippingList(orderId);
    }

    private static void doCancelOrder() {
        int orderId = readInt("Order ID to cancel: ");
        store.cancelOrder(orderId);
    }

    private static void doSearchOrderById() {
        int orderId = readInt("Order ID: ");
        store.searchOrderById(orderId);
    }

    private static void doAddReview() {
        int productId = readInt("Product ID: ");
        String name = readLine("Customer name: ");
        String comment = readLine("Comment: ");
        store.addReview(productId, name, comment);
    }

    private static void doShowAllReviews() {
        int productId = readInt("Product ID: ");
        store.showAllReviewsForProduct(productId);
    }


    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }


    private static void loadExampleData() {
        store.addProduct(1, "Wireless Mouse", 250, "Electronics", 15);
        store.addProduct(2, "Notebook", 30, "Stationery", 0);
        store.addProduct(3, "Desk Lamp", 180, "Home", 8);
        store.addProduct(4, "USB Cable", 60, "Electronics", 20);

        store.createOrder(501, "Sara");
        store.addItemToOrder(501, 1, 1);
        store.addItemToOrder(501, 4, 2);
        System.out.println();
    }
}
