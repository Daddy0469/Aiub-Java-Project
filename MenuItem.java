import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


// Abstract base class demonstrating Abstraction
abstract class MenuItem {
    protected String name;
    protected double price;
    protected String category;
    
    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract String getDescription();
    
    // Encapsulation - getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return name + " - $" + String.format("%.2f", price);
    }
}