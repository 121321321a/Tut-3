public class Main {
    public static void main(String[] args) {
        ComputerStore store = new ComputerStore();

        Product product1 = new Product("Laptop", "Laptop", 999.99, 10);
        Product product2 = new Product("Mouse", "Mouse", 49.99, 15);
        Product product3 = new Product("Monitor", "Monitor", 129.99, 5);

        store.addProduct(product1);
        store.addProduct(product2);
        store.addProduct(product3);

        Customer customer1 = new Customer(1, true);
        Customer customer2 = new Customer(2, false);

        store.addCustomer(customer1);
        store.addCustomer(customer2);

        Product[] orderProducts1 = {product1, product2};
        int[] orderQuantities1 = {1, 3};
        Order order1 = store.createOrder(customer1, orderProducts1, orderQuantities1);
        order1.applyDiscount();
        store.updateStockAfterOrder(order1);
        order1.displayDetails();

        Product[] orderProducts2 = {product3, product2};
        int[] orderQuantities2 = {2, 1};
        Order order2 = store.createOrder(customer2, orderProducts2, orderQuantities2);
        store.updateStockAfterOrder(order2);

        store.changeOrderStatus(1, "Completed");
        System.out.println("Order status after update:");
        order1.displayDetails();

        System.out.println("Orders of customer 1:");
        store.displayCustomerOrders(1);

        System.out.println("Mouse category products:");
        store.displayProductsInCategory("Mouse");
    }
}

class Product {
    private final String name;
    private final String category;
    private double price;
    private int stockQuantity;

    public Product(String name, String category, double price, int stockQuantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }

    public void setPrice(double price) { this.price = price; }
    public void setStockQuantity(int quantity) { this.stockQuantity = quantity; }

    public void displayInformation() {
        System.out.println(name + " - " + category + " - " + price);
    }
}

record Customer(int id, boolean isLoyalCustomer) {
}

class Order {
    private int id;
    private Customer customer;
    private Product[] products;
    private int[] quantities;
    private String status;

    public void setId(int id) { this.id = id; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setProducts(Product[] products) { this.products = products; }
    public void setQuantities(int[] quantities) { this.quantities = quantities; }
    public void setStatus(String status) { this.status = status; }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Product[] getProducts() { return products; }
    public int[] getQuantities() { return quantities; }

    public void applyDiscount() {
        if (customer.isLoyalCustomer()) {
            for (Product p : products) {
                p.setPrice(p.getPrice() * 0.9);
            }
        }
    }

    public void displayDetails() {
        System.out.println("Order " + id + " - " + status);
        for (int i = 0; i < products.length; i++) {
            System.out.println(products[i].getName() + " x " + quantities[i]);
        }
    }
}

class ComputerStore {
    private final Product[] products = new Product[100];
    private final Customer[] customers = new Customer[100];
    private final Order[] orders = new Order[100];
    private int productCount = 0;
    private int customerCount = 0;
    private int orderCount = 0;

    public void addProduct(Product product) {
        products[productCount++] = product;
    }

    public void addCustomer(Customer customer) {
        customers[customerCount++] = customer;
    }

    public Order createOrder(Customer customer, Product[] products, int[] quantities) {
        Order order = new Order();
        order.setId(orderCount + 1);
        order.setCustomer(customer);
        order.setProducts(products);
        order.setQuantities(quantities);
        order.setStatus("New");
        orders[orderCount++] = order;
        return order;
    }

    public void updateStockAfterOrder(Order order) {
        for (int i = 0; i < order.getProducts().length; i++) {
            Product p = order.getProducts()[i];
            int newStock = p.getStockQuantity() - order.getQuantities()[i];
            p.setStockQuantity(newStock);
        }
    }

    public void changeOrderStatus(int orderId, String newStatus) {
        for (Order o : orders) {
            if (o != null && o.getId() == orderId) {
                o.setStatus(newStatus);
            }
        }
    }

    public void displayProductsInCategory(String category) {
        for (Product p : products) {
            if (p != null && p.getCategory().equals(category)) {
                p.displayInformation();
            }
        }
    }

    public void displayCustomerOrders(int customerId) {
        for (Order o : orders) {
            if (o != null && o.getCustomer().id() == customerId) {
                o.displayDetails();
            }
        }
    }
}
