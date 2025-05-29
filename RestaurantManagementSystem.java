import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

// Main GUI class
public class RestaurantManagementSystem extends JFrame {
    private RestaurantDAO dao;
    private JTabbedPane tabbedPane;
    
    // Menu Management Components
    private JTable menuTable;
    private JTextField menuNameField, menuPriceField, menuCategoryField;
    private JButton insertMenuBtn, updateMenuBtn, deleteMenuBtn, getMenuBtn;
    
    // Customer Management Components
    private JTable customerTable;
    private JTextField customerNameField, customerPhoneField, customerEmailField;
    private JButton insertCustomerBtn, updateCustomerBtn, deleteCustomerBtn, getCustomerBtn;
    
    // Order Management Components
    private JTable orderTable;
    private JComboBox<Customer> customerCombo;
    private JComboBox<MenuItem> menuItemCombo;
    private JSpinner quantitySpinner;
    private JButton addToOrderBtn, placeOrderBtn, getOrdersBtn;
    private Order currentOrder;
    
    // ADD THESE MISSING COMPONENTS:
    private JTextArea orderArea;
    private JLabel totalLabel;

    
    public RestaurantManagementSystem() {
        dao = new RestaurantDAO();
        initializeGUI();
        layoutComponents();
        loadInitialData();
        setVisible(true);
    }
    
    private void layoutComponents() {
        add(tabbedPane, BorderLayout.CENTER);
        
        // Header
        JLabel headerLabel = new JLabel("Food Court", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBackground(new Color(50, 50, 50));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        add(headerLabel, BorderLayout.NORTH);
    }
    
    private void loadInitialData() {
        refreshMenuTable();
        refreshCustomerTable();
        refreshOrderTable();
    }
    
    private void initializeGUI() {
        setTitle("Restaurant Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        tabbedPane = new JTabbedPane();
        
        // Create tabs
        tabbedPane.addTab("Menu Management", createMenuPanel());
        tabbedPane.addTab("Customer Management", createCustomerPanel());
        tabbedPane.addTab("Order Management", createOrderPanel());
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Menu Item Details"));
        
        formPanel.add(new JLabel("Name:"));
        menuNameField = new JTextField();
        formPanel.add(menuNameField);
        
        formPanel.add(new JLabel("Price:"));
        menuPriceField = new JTextField();
        formPanel.add(menuPriceField);
        
        formPanel.add(new JLabel("Category:"));
        menuCategoryField = new JTextField();
        formPanel.add(menuCategoryField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        insertMenuBtn = new JButton("Insert");
        updateMenuBtn = new JButton("Update");
        deleteMenuBtn = new JButton("Delete");
        getMenuBtn = new JButton("Refresh");
        
        insertMenuBtn.addActionListener(e -> insertMenuItem());
        updateMenuBtn.addActionListener(e -> updateMenuItem());
        deleteMenuBtn.addActionListener(e -> deleteMenuItem());
        getMenuBtn.addActionListener(e -> refreshMenuTable());
        
        buttonPanel.add(insertMenuBtn);
        buttonPanel.add(updateMenuBtn);
        buttonPanel.add(deleteMenuBtn);
        buttonPanel.add(getMenuBtn);
        
        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);
        
        // Table
        String[] columns = {"Name", "Price", "Category"};
        menuTable = new JTable(new Object[0][0], columns);
        menuTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedMenuItem();
            }
        });
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(menuTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        
        formPanel.add(new JLabel("Name:"));
        customerNameField = new JTextField();
        formPanel.add(customerNameField);
        
        formPanel.add(new JLabel("Phone:"));
        customerPhoneField = new JTextField();
        formPanel.add(customerPhoneField);
        
        formPanel.add(new JLabel("Email:"));
        customerEmailField = new JTextField();
        formPanel.add(customerEmailField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        insertCustomerBtn = new JButton("Insert");
        updateCustomerBtn = new JButton("Update");
        deleteCustomerBtn = new JButton("Delete");
        getCustomerBtn = new JButton("Refresh");
        
        insertCustomerBtn.addActionListener(e -> insertCustomer());
        updateCustomerBtn.addActionListener(e -> updateCustomer());
        deleteCustomerBtn.addActionListener(e -> deleteCustomer());
        getCustomerBtn.addActionListener(e -> refreshCustomerTable());
        
        buttonPanel.add(insertCustomerBtn);
        buttonPanel.add(updateCustomerBtn);
        buttonPanel.add(deleteCustomerBtn);
        buttonPanel.add(getCustomerBtn);
        
        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);
        
        // Table
        String[] columns = {"Name", "Phone", "Email"};
        customerTable = new JTable(new Object[0][0], columns);
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCustomer();
            }
        });
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(customerTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        
        formPanel.add(new JLabel("Customer:"));
        customerCombo = new JComboBox<>();
        formPanel.add(customerCombo);
        
        formPanel.add(new JLabel("Menu Item:"));
        menuItemCombo = new JComboBox<>();
        formPanel.add(menuItemCombo);
        
        formPanel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        formPanel.add(quantitySpinner);
        
        // Main buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addToOrderBtn = new JButton("Add to Order");
        placeOrderBtn = new JButton("Place Order");
        getOrdersBtn = new JButton("Refresh Orders");
        JButton clearCurrentOrderBtn = new JButton("Clear Current Order");

        addToOrderBtn.addActionListener(e -> addToOrder());
        placeOrderBtn.addActionListener(e -> placeOrder());
        getOrdersBtn.addActionListener(e -> refreshOrderTable());
        clearCurrentOrderBtn.addActionListener(e -> {
            currentOrder = null;
            updateCurrentOrderDisplay();
            JOptionPane.showMessageDialog(this, "Current order cleared!");
        });

        buttonPanel.add(addToOrderBtn);
        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(getOrdersBtn);
        buttonPanel.add(clearCurrentOrderBtn);
        
        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);
        
        // DELETE BUTTONS
        JPanel deletePanel = new JPanel(new FlowLayout());
        JButton deleteSelectedBtn = new JButton("Delete Selected Order");
        JButton clearAllOrdersBtn = new JButton("Clear All Orders");
        
        deleteSelectedBtn.setBackground(new Color(200, 50, 50));
        deleteSelectedBtn.setForeground(Color.WHITE);
        clearAllOrdersBtn.setBackground(new Color(150, 30, 30));
        clearAllOrdersBtn.setForeground(Color.WHITE);
        
        deleteSelectedBtn.addActionListener(e -> deleteSelectedOrder());
        clearAllOrdersBtn.addActionListener(e -> clearAllOrders());
        
        deletePanel.add(deleteSelectedBtn);
        deletePanel.add(clearAllOrdersBtn);
        
        formPanel.add(new JLabel("Delete Options:"));
        formPanel.add(deletePanel);
        
        // CENTER PANEL - Current Order Display and Order History
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Current Order Panel
        JPanel currentOrderPanel = new JPanel(new BorderLayout());
        currentOrderPanel.setBorder(BorderFactory.createTitledBorder("Current Order"));
        
        orderArea = new JTextArea(8, 30);
        orderArea.setEditable(false);
        orderArea.setBackground(Color.LIGHT_GRAY);
        orderArea.setText("No current order");
        currentOrderPanel.add(new JScrollPane(orderArea), BorderLayout.CENTER);
        
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentOrderPanel.add(totalLabel, BorderLayout.SOUTH);
        
        // Order History Table
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Order History"));
        
        String[] columns = {"Order ID", "Customer", "Total Amount", "Date"};
        orderTable = new JTable(new Object[0][0], columns);
        historyPanel.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        
        // Split the center area
        centerPanel.add(currentOrderPanel, BorderLayout.NORTH);
        centerPanel.add(historyPanel, BorderLayout.CENTER);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateCurrentOrderDisplay() {
        if (currentOrder == null) {
            orderArea.setText("No current order");
            totalLabel.setText("Total: $0.00");
            return;
        }
        
        orderArea.setText("");
        List<MenuItem> items = currentOrder.getItems();
        List<Integer> quantities = currentOrder.getQuantities();
        
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            int qty = quantities.get(i);
            double itemTotal = item.getPrice() * qty;
            
            orderArea.append(item.getName() + " x " + qty + " = $" + 
                            String.format("%.2f", itemTotal) + "\n");
        }
        
        totalLabel.setText("Total: $" + String.format("%.2f", currentOrder.getTotalAmount()));
    }
    
    // Menu CRUD Operations
    private void insertMenuItem() {
        try {
            String name = menuNameField.getText().trim();
            double price = Double.parseDouble(menuPriceField.getText().trim());
            String category = menuCategoryField.getText().trim();
            
            if (name.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            
            // Polymorphism - creating different types of menu items
            MenuItem item;
            if (category.equals("Drinks")) {
                item = new DrinkItem(name, price, category, 330, true);
            } else {
                item = new FoodItem(name, price, category, "Various ingredients", false);
            }
            
            dao.insertMenuItem(item);
            refreshMenuTable();
            clearMenuFields();
            JOptionPane.showMessageDialog(this, "Menu item added successfully!");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price");
        }
    }
    
    private void updateMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a menu item to update");
            return;
        }
        
        try {
            String name = menuNameField.getText().trim();
            double price = Double.parseDouble(menuPriceField.getText().trim());
            String category = menuCategoryField.getText().trim();
            
            MenuItem item;
            if (category.equals("Drinks")) {
                item = new DrinkItem(name, price, category, 330, true);
            } else {
                item = new FoodItem(name, price, category, "Various ingredients", false);
            }
            
            dao.updateMenuItem(selectedRow, item);
            refreshMenuTable();
            clearMenuFields();
            JOptionPane.showMessageDialog(this, "Menu item updated successfully!");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price");
        }
    }
    
    private void deleteMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a menu item to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?");
        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteMenuItem(selectedRow);
            refreshMenuTable();
            clearMenuFields();
            JOptionPane.showMessageDialog(this, "Menu item deleted successfully!");
        }
    }
    
    private void refreshMenuTable() {
        List<MenuItem> items = dao.getMenuItems();
        Object[][] data = new Object[items.size()][3];
        
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            data[i][0] = item.getName();
            data[i][1] = "$" + String.format("%.2f", item.getPrice());
            data[i][2] = item.getCategory();
        }
        
        String[] columns = {"Name", "Price", "Category"};
        menuTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
        
        // Update combo box for orders
        menuItemCombo.removeAllItems();
        for (MenuItem item : items) {
            menuItemCombo.addItem(item);
        }
    }
    
    private void loadSelectedMenuItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            List<MenuItem> items = dao.getMenuItems();
            if (selectedRow < items.size()) {
                MenuItem item = items.get(selectedRow);
                menuNameField.setText(item.getName());
                menuPriceField.setText(String.valueOf(item.getPrice()));
                menuCategoryField.setText(item.getCategory());
            }
        }
    }
    
    private void clearMenuFields() {
        menuNameField.setText("");
        menuPriceField.setText("");
        menuCategoryField.setText("");
    }
    
    // Customer CRUD Operations
    private void insertCustomer() {
        String name = customerNameField.getText().trim();
        String phone = customerPhoneField.getText().trim();
        String email = customerEmailField.getText().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill name and phone fields");
            return;
        }
        
        Customer customer = new Customer(name, phone, email);
        dao.insertCustomer(customer);
        refreshCustomerTable();
        clearCustomerFields();
        JOptionPane.showMessageDialog(this, "Customer added successfully!");
    }
    
    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to update");
            return;
        }
        
        String name = customerNameField.getText().trim();
        String phone = customerPhoneField.getText().trim();
        String email = customerEmailField.getText().trim();
        
        Customer customer = new Customer(name, phone, email);
        dao.updateCustomer(selectedRow, customer);
        refreshCustomerTable();
        clearCustomerFields();
        JOptionPane.showMessageDialog(this, "Customer updated successfully!");
    }
    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?");
        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteCustomer(selectedRow);
            refreshCustomerTable();
            clearCustomerFields();
            JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
        }
    }
    
    private void refreshCustomerTable() {
        List<Customer> customers = dao.getCustomers();
        Object[][] data = new Object[customers.size()][3];
        
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            data[i][0] = customer.getName();
            data[i][1] = customer.getPhone();
            data[i][2] = customer.getEmail();
        }
        
        String[] columns = {"Name", "Phone", "Email"};
        customerTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
        
        // Update combo box for orders
        customerCombo.removeAllItems();
        for (Customer customer : customers) {
            customerCombo.addItem(customer);
        }
    }
    
    private void loadSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            List<Customer> customers = dao.getCustomers();
            if (selectedRow < customers.size()) {
                Customer customer = customers.get(selectedRow);
                customerNameField.setText(customer.getName());
                customerPhoneField.setText(customer.getPhone());
                customerEmailField.setText(customer.getEmail());
            }
        }
    }
    
    private void clearCustomerFields() {
        customerNameField.setText("");
        customerPhoneField.setText("");
        customerEmailField.setText("");
    }
    
    // Order Operations
    private void addToOrder() {
        Customer selectedCustomer = (Customer) customerCombo.getSelectedItem();
        MenuItem selectedItem = (MenuItem) menuItemCombo.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();

        if (selectedCustomer == null || selectedItem == null) {
            JOptionPane.showMessageDialog(this, "Please select customer and menu item");
            return;
        }

        if (currentOrder == null || !currentOrder.getCustomer().equals(selectedCustomer)) {
            currentOrder = new Order(selectedCustomer);
        }

        currentOrder.addItem(selectedItem, quantity);
        updateCurrentOrderDisplay();
        JOptionPane.showMessageDialog(this, "Item added to order!");
    }
    
    private void placeOrder() {
        if (currentOrder == null) {
            JOptionPane.showMessageDialog(this, "No items in current order");
            return;
        }

        dao.insertOrder(currentOrder);
        refreshOrderTable();
        currentOrder = null;
        updateCurrentOrderDisplay();
        JOptionPane.showMessageDialog(this, "Order placed successfully!");
    }
    
    private void deleteSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this order?");
        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteOrder(selectedRow);
            refreshOrderTable();
            JOptionPane.showMessageDialog(this, "Order deleted successfully!");
        }
    }

    private void clearAllOrders() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete ALL orders?");
        if (confirm == JOptionPane.YES_OPTION) {
            dao.clearAllOrders();
            refreshOrderTable();
            JOptionPane.showMessageDialog(this, "All orders cleared!");
        }
    }

    private void refreshOrderTable() {
        dao.reloadOrdersFromFile();
        
        List<Order> orders = dao.getOrders();
        Object[][] data = new Object[orders.size()][4];
        
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            data[i][0] = order.getOrderId();
            data[i][1] = order.getCustomer().getName();
            data[i][2] = "$" + String.format("%.2f", order.getTotalAmount());
            data[i][3] = order.getOrderDate().toString();
        }
        
        String[] columns = {"Order ID", "Customer", "Total Amount", "Date"};
        orderTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RestaurantManagementSystem();
        });
    }
}
