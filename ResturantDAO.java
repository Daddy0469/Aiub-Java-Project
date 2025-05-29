import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


// Data Access Object - demonstrating Encapsulation and Abstraction
class RestaurantDAO {
    private List<MenuItem> menuItems;
    private List<Customer> customers;
    private List<Order> orders;
    private final String MENU_FILE = "menu.txt";
    private final String CUSTOMERS_FILE = "customers.txt";
    private final String ORDERS_FILE = "orders.txt";
    
    public RestaurantDAO() {
		menuItems = new ArrayList<>();
		customers = new ArrayList<>();
		orders = new ArrayList<>();
		initializeDefaultData();
		loadOrdersFromFile(); // ADD THIS LINE
	}

    
    private void initializeDefaultData() {
        // Initialize with some default menu items
        menuItems.add(new FoodItem("Chicken Biriyani", 8.0, "Main Course", "Rice, Chicken, Spices", false));
        menuItems.add(new FoodItem("Kacchi Biriyani", 10.0, "Main Course", "Rice, Mutton, Spices", false));
        menuItems.add(new FoodItem("Vegetable Curry", 5.0, "Main Course", "Mixed Vegetables, Curry", true));
        menuItems.add(new DrinkItem("Coke", 2.0, "Drinks", 330, true));
        menuItems.add(new DrinkItem("Tea", 1.5, "Drinks", 200, false));
        menuItems.add(new FoodItem("Kabab", 6.0, "Starter", "Beef, Spices", false));
    }
    
    // CRUD Operations for Menu Items
    public void insertMenuItem(MenuItem item) {
        menuItems.add(item);
        saveMenuData();
    }
    
    public void updateMenuItem(int index, MenuItem item) {
        if (index >= 0 && index < menuItems.size()) {
            menuItems.set(index, item);
            saveMenuData();
        }
    }
    
    public void deleteMenuItem(int index) {
        if (index >= 0 && index < menuItems.size()) {
            menuItems.remove(index);
            saveMenuData();
        }
    }
    
    public List<MenuItem> getMenuItems() {
        return new ArrayList<>(menuItems);
    }
    
    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItems.stream()
                .filter(item -> item.getCategory().equals(category))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    // CRUD Operations for Customers
    public void insertCustomer(Customer customer) {
        customers.add(customer);
        saveCustomerData();
    }
    
    public void updateCustomer(int index, Customer customer) {
        if (index >= 0 && index < customers.size()) {
            customers.set(index, customer);
            saveCustomerData();
        }
    }
    
    public void deleteCustomer(int index) {
        if (index >= 0 && index < customers.size()) {
            customers.remove(index);
            saveCustomerData();
        }
    }
    
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }
    
    // CRUD Operations for Orders
    public void insertOrder(Order order) {
        orders.add(order);
        saveOrderData();
    }
    
    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
    
    public List<Order> getOrdersByCustomer(String customerName) {
        return orders.stream()
                .filter(order -> order.getCustomer().getName().toLowerCase().contains(customerName.toLowerCase()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    private void loadData() {
        // Implementation for loading data from files
    }
    
    private void saveMenuData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MENU_FILE))) {
            for (MenuItem item : menuItems) {
                writer.println(item.getName() + "," + item.getPrice() + "," + item.getCategory());
            }
        } catch (IOException e) {
            System.err.println("Error saving menu data: " + e.getMessage());
        }
    }
    
    private void saveCustomerData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers) {
                writer.println(customer.getName() + "," + customer.getPhone() + "," + customer.getEmail());
            }
        } catch (IOException e) {
            System.err.println("Error saving customer data: " + e.getMessage());
        }
    }
    
    private void saveOrderData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : orders) {
                writer.println("Order #" + order.getOrderId() + " - " + order.getCustomer().getName() + 
                              " - Total: $" + String.format("%.2f", order.getTotalAmount()));
            }
        } catch (IOException e) {
            System.err.println("Error saving order data: " + e.getMessage());
        }
    }
	
	public void reloadMenuFromFile() {
		menuItems.clear();
		loadMenuFromFile();
	}

	private void loadMenuFromFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length >= 3) {
					String name = parts[0].trim();
					double price = Double.parseDouble(parts[1].trim());
					String category = parts[2].trim();
					
					MenuItem item;
					if (category.equals("Drinks")) {
						item = new DrinkItem(name, price, category, 330, true);
					} else {
						item = new FoodItem(name, price, category, "Various ingredients", false);
					}
					menuItems.add(item);
				}
			}
		} catch (IOException | NumberFormatException e) {
			System.err.println("Error loading menu from file: " + e.getMessage());
			// Keep existing default data if file read fails
		}
	}
	
	
public void deleteOrder(int index) {
    if (index >= 0 && index < orders.size()) {
        orders.remove(index);
        saveOrderData();
    }
}

public void clearAllOrders() {
    orders.clear();
    saveOrderData();
}

public void reloadOrdersFromFile() {
    orders.clear();
    loadOrdersFromFile();
}

private void loadOrdersFromFile() {
    try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
        String line;
        Customer currentCustomer = null;
        String phone = "";
        double total = 0.0;
        int orderId = 1;
        
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Customer: ")) {
                String customerName = line.substring(10);
                currentCustomer = new Customer(customerName, "", "");
            } else if (line.startsWith("Phone: ")) {
                phone = line.substring(7);
                if (currentCustomer != null) {
                    currentCustomer.setPhone(phone);
                }
            } else if (line.startsWith("Total: $")) {
                String totalStr = line.substring(8);
                try {
                    total = Double.parseDouble(totalStr);
                    if (currentCustomer != null) {
                        Order order = new Order(orderId++, currentCustomer, total, new Date());
                        orders.add(order);
                        currentCustomer = null; // Reset for next order
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing total: " + totalStr);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error loading orders from file: " + e.getMessage());
    }
}



}


