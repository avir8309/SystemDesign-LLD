// Design a Food Delivery Application

// Functional Requirements
// - Users should be able to search for restaurants based on their location.
// - Users should be able to browse restaurant menus and food items.
// - Users should be able to add food items to their cart.
// - Users should be able to place orders from their cart.
// - Users should be able to make payments using supported payment methods.
// - Users should receive notifications regarding order status updates.

// Core Flow
// User -> Restaurant Discovery -> Food Item Selection -> Cart
// -> Order Placement -> Payment Processing -> Order Confirmation
// -> Notification Delivery

// Business Entities
// User
// Restaurant
// FoodItem
// Cart
// Order
// Payment
// Notification


// User visits the platform and can view multiple restaurants based on their location.
// After selecting a restaurant, the user can browse its menu and available food items.
// The user can add one or more food items to the cart.
// Once the cart is finalized, the user can place an order by choosing either:
//    - Home Delivery
//    - Store Pickup
//
// After order placement, the user proceeds to payment.
// Payment will be handled through a third-party payment service/provider.
// During the design discussion, we can either:
//    - Integrate an external payment gateway, or
//    - Define a Payment Service abstraction and its interactions.
//
// Scope Clarification:
// We will focus primarily on the User's perspective and the order placement flow.
// Delivery partner assignment, routing, and logistics are considered out of scope for the initial design.
//
// High-Level Flow:
// User -> Restaurant Discovery -> Menu Browsing -> Add Items to Cart
// -> Order Placement (Delivery/Pickup) -> Payment Processing
// -> Order Confirmation -> Notification
//
// Once the order is successfully placed, the user receives notifications
// regarding order confirmation and subsequent order status updates.


// class Restaurant:
//     id, name , address, vector<menuItem>
// Menu item composition relation with Restaurant
// 1---*
// class menuItem:
//     code, name, price, model class
// Restuarant Manager -( aggregation with restuarant )(1---*)(Loosely coupled as Manager do loose coupling)
//   vector<Restaurant>, addRestuarant(), searchByLocation()
// Manager always singleTon, if multiple will store different attribute values throughout the application

// Class User-(model)
//     id, name, email, address, cart(1 to 1 relationship bw user and cart, also cart can'tt exist without user so composition)(Model class)

// ( HotTake can also use cartManager)
// class cart-
//     Restaurant res, vector<menuItem>, total, addToCart, clear()

// class Payment-
// (strategy design pattern)( paymentStrategy )->(upi,cc,netbanking,cod)
// class order -
// id Restuarant, menuItems, User, paymentStrategy, getType()
// orderFactory->createOrder and orderManager->toHandleOrder
// Restuarant can also do the same
// orderFactory->(interface->create order) will be implemented by( now, schedule)
// orderManager->(aggregation with order)
// vector<order> , addOrder, 
// NotificationService ->( notify(order))


// Model
public class Cart{
    private Restaurant restuarant;
    private List<MenuIntems> menuItems = new ArrayList<>();

    public Cart(){
        restaurant=null;
    }
    public void addItem(MenuItem item){
        if(!restaurant){
            System.err.println("Cart: Set a restaurant before adding items.");
            return;
        }
        menuItems.add(item)
    }
    public double getTotalCost() {
        double sum = 0;
        for (MenuItem it : items) {
            sum += it.getPrice();
        }
        return sum;
    }

    public boolean isEmpty() {
        return restaurant == null || items.isEmpty();
    }

    public void clear() {
        items.clear();
        restaurant = null;
    }

    public void setRestaurant(Restaurant r) {
        restaurant = r;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<MenuItem> getItems() {
        return items;
    }
}

// User
class User{
    private int userId;
    private String name;
    private String address;
    private Cart cart;

    public User(int userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.cart = new Cart();
    }
    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String a) {
        address = a;
    }

    public Cart getCart() {
        return cart;
    }
}

// Restuarant
public class Restuarant{
    private List<MenuItem> menu=new ArrayList<>();
    private static int nextRestaurantId = 0; // auto increment purpose
    private int restaurantId;
    private String name;
    private String location;

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
        this.restaurantId = ++nextRestaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String loc) {
        location = loc;
    }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    public List<MenuItem> getMenu() {
        return menu;
    }
}

// MenuItem
public class MenuItem {
    private String code;
    private String name;
    private int price;

    public MenuItem(String code, String name, int price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String c) {
        code = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int p) {
        price = p;
    }
}


// order
public abstract class Order {
    private static int nextOrderId = 0;

    protected int orderId; // as abstract class that's why protected
    protected User user;
    protected Restaurant restaurant;
    protected List<MenuItem> items;
    protected PaymentStrategy paymentStrategy;
    protected double total;
    protected String scheduled;

    public Order() {
        this.user = null;
        this.restaurant = null;
        this.paymentStrategy = null;
        this.total = 0.0;
        this.scheduled = "";
        this.orderId = ++nextOrderId;
    }

    public boolean processPayment() {
        if (paymentStrategy != null) {
            paymentStrategy.pay(total);
            return true;
        } else {
            System.out.println("Please choose a payment mode first");
            return false;
        }
    }

    public abstract String getType();

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setUser(User u) {
        user = u;
    }

    public User getUser() {
        return user;
    }

    public void setRestaurant(Restaurant r) {
        restaurant = r;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setItems(List<MenuItem> its) {
        items = its;
        total = 0;
        for (MenuItem i : items) {
            total += i.getPrice();
        }
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setPaymentStrategy(PaymentStrategy p) {
        paymentStrategy = p;
    }

    public void setScheduled(String s) {
        scheduled = s;
    }

    public String getScheduled() {
        return scheduled;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

public class DeliveryOrder extends Order {
    private String userAddress;

    public DeliveryOrder() {
        userAddress = "";
    }

    @Override
    public String getType() {
        return "Delivery";
    }

    public void setUserAddress(String addr) {
        userAddress = addr;
    }

    public String getUserAddress() {
        return userAddress;
    }

    // Implement remaining Order methods with actual fields
}
public class PickupOrder extends Order {
    private String restaurantAddress;

    public PickupOrder() {
        restaurantAddress = "";
    }

    @Override
    public String getType() {
        return "Pickup";
    }

    public void setRestaurantAddress(String addr) {
        restaurantAddress = addr;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    // Implement remaining Order methods with actual fields
}

// Managers

class orderManager{
    // singleton
    private List<Order> orders = new ArrayList<>();
    private static OrderManager instance = null;
    // This prevents others to create obj
    private OrderManager() {
        // Private Constructor
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }
    public void addOrder(Order order) {
        orders.add(order);
    }

    public void listOrders() {
        System.out.println("\n--- All Orders ---");
        for (Order order : orders) {
            System.out.println(order.getType() + " order for " + order.getUser().getName()
                    + " | Total: ₹" + order.getTotal()
                    + " | At: " + order.getScheduled());
        }
    }
}

class RestuarantManager{
    private List<Restuarant> restaurants = new ArrayList<>();
    private static RestaurantManager instance = null;

    private RestaurantManager(){

    }

    // Why static?

    // Because you need to call getInstance() without creating an object first.
    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public void addRestaurant(Restaurant r) {
        restaurants.add(r);
    }

    public List<Restaurant> searchByLocation(String loc) {
        List<Restaurant> result = new ArrayList<>();
        loc = loc.toLowerCase();
        for (Restaurant r : restaurants) {
            String rl = r.getLocation().toLowerCase();
            if (rl.equals(loc)) {
                result.add(r);
            }
        }
        return result;
    }
}

// Order Factory
public interface OrderFactory {
    Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
                      PaymentStrategy paymentStrategy, double totalCost, String orderType);
}

public class ScheduledOrderFactory implements OrderFactory{
    rivate String scheduleTime;

    public ScheduledOrderFactory(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }
    @Override
    public Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
                             PaymentStrategy paymentStrategy, double totalCost, String orderType) {
        Order order = null;

        if (orderType.equals("Delivery")) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserAddress(user.getAddress());
            order = deliveryOrder;
        } else {
            PickupOrder pickupOrder = new PickupOrder();
            pickupOrder.setRestaurantAddress(restaurant.getLocation());
            order = pickupOrder;
        }

        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setItems(menuItems);
        order.setPaymentStrategy(paymentStrategy);
        order.setScheduled(scheduleTime);
        order.setTotal(totalCost);
        return order;
    }
}

public class NowOrderFactory implements OrderFactory {
    @Override
    public Order createOrder(User user, Cart cart, Restaurant restaurant, List<MenuItem> menuItems,
                             PaymentStrategy paymentStrategy, double totalCost, String orderType) {
        Order order = null;

        if (orderType.equals("Delivery")) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserAddress(user.getAddress());
            order = deliveryOrder;
        } else {
            PickupOrder pickupOrder = new PickupOrder();
            pickupOrder.setRestaurantAddress(restaurant.getLocation());
            order = pickupOrder;
        }

        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setItems(menuItems);
        order.setPaymentStrategy(paymentStrategy);
        order.setScheduled(TimeUtils.getCurrentTime());
        order.setTotal(totalCost);
        return order;
    }
}


// strategies for payment

public interface PaymentStrategy {
    void pay(double amount);
}

public class CreditCardPaymentStrategy implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPaymentStrategy(String card) {
        this.cardNumber = card;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Credit Card (" + cardNumber + ")");
    }
}

public class UpiPaymentStrategy implements PaymentStrategy {
    private String mobile;

    public UpiPaymentStrategy(String mob) {
        this.mobile = mob;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using UPI (" + mobile + ")");
    }
}

// Notification service

public class NotificationService {
    public static void notify(Order order) {
        System.out.println("\nNotification: New " + order.getType() + " order placed!");
        System.out.println("---------------------------------------------");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Customer: " + order.getUser().getName());
        System.out.println("Restaurant: " + order.getRestaurant().getName());
        System.out.println("Items Ordered:");

        List<MenuItem> items = order.getItems();
        for (MenuItem item : items) {
            System.out.println("   - " + item.getName() + " (₹" + item.getPrice() + ")");
        }

        System.out.println("Total: ₹" + order.getTotal());
        System.out.println("Scheduled For: " + order.getScheduled());
        System.out.println("Payment: Done");
        System.out.println("---------------------------------------------");
    }
}




























