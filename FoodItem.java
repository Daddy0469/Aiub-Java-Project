import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;


// Inheritance - FoodItem extends MenuItem
class FoodItem extends MenuItem {
    private String ingredients;
    private boolean isVegetarian;
    
    public FoodItem(String name, double price, String category, String ingredients, boolean isVegetarian) {
        super(name, price, category);
        this.ingredients = ingredients;
        this.isVegetarian = isVegetarian;
    }
    
    // Polymorphism - overriding abstract method
    @Override
    public String getDescription() {
        return name + " (" + category + ") - " + ingredients + 
               (isVegetarian ? " [Vegetarian]" : " [Non-Vegetarian]");
    }
    
    // Encapsulation
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    
    public boolean isVegetarian() { return isVegetarian; }
    public void setVegetarian(boolean vegetarian) { isVegetarian = vegetarian; }
}