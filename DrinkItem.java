import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


// Inheritance - DrinkItem extends MenuItem
class DrinkItem extends MenuItem {
    private int sizeML;
    private boolean isCold;
    
    public DrinkItem(String name, double price, String category, int sizeML, boolean isCold) {
        super(name, price, category);
        this.sizeML = sizeML;
        this.isCold = isCold;
    }
    
    // Polymorphism - overriding abstract method
    @Override
    public String getDescription() {
        return name + " (" + sizeML + "ml) - " + (isCold ? "Cold" : "Hot") + " " + category;
    }
    
    // Encapsulation
    public int getSizeML() { return sizeML; }
    public void setSizeML(int sizeML) { this.sizeML = sizeML; }
    
    public boolean isCold() { return isCold; }
    public void setCold(boolean cold) { isCold = cold; }
}