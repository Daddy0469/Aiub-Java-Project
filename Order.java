import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


// Order class demonstrating Encapsulation
class Order {
    private static int orderCounter = 1;
    private int orderId;
    private Customer customer;
    private List<MenuItem> items;
    private List<Integer> quantities;
    private double totalAmount;
    private Date orderDate;
	
	// ADD this constructor to Order class (keep both constructors):
	public Order(Customer customer) {
		this.orderId = orderCounter++;
		this.customer = customer;
		this.items = new ArrayList<>();
		this.quantities = new ArrayList<>();
		this.orderDate = new Date();
		calculateTotal();
	}

    
    // ADD THIS CONSTRUCTOR for loading from file
public Order(int orderId, Customer customer, double totalAmount, Date orderDate) {
    this.orderId = orderId;
    this.customer = customer;
    this.items = new ArrayList<>();
    this.quantities = new ArrayList<>();
    this.totalAmount = totalAmount;
    this.orderDate = orderDate;
}

	
	
    
    public void addItem(MenuItem item, int quantity) {
        int index = items.indexOf(item);
        if (index >= 0) {
            quantities.set(index, quantities.get(index) + quantity);
        } else {
            items.add(item);
            quantities.add(quantity);
        }
        calculateTotal();
    }
    
    public void removeItem(MenuItem item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            items.remove(index);
            quantities.remove(index);
            calculateTotal();
        }
    }
    
    private void calculateTotal() {
        totalAmount = 0;
        for (int i = 0; i < items.size(); i++) {
            totalAmount += items.get(i).getPrice() * quantities.get(i);
        }
    }
	
	// ADD THIS METHOD to set total manually
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

    
    // Encapsulation - getters
    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<MenuItem> getItems() { return new ArrayList<>(items); }
    public List<Integer> getQuantities() { return new ArrayList<>(quantities); }
    public double getTotalAmount() { return totalAmount; }
    public Date getOrderDate() { return orderDate; }
    
    @Override
    public String toString() {
        return "Order #" + orderId + " - " + customer.getName() + " - $" + String.format("%.2f", totalAmount);
    }
}