import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class InventoryManagement extends Application {
    
    private List<Item> inventory = new ArrayList<>();
    private ListView<String> inventoryListView;
    private TextField nameField, quantityField, priceField;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management System");

        
        nameField = new TextField();
        nameField.setPromptText("Item Name");
        quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        priceField = new TextField();
        priceField.setPromptText("Price");

        
        Button addButton = new Button("Add Item");
        Button updateButton = new Button("Update Item");
        Button deleteButton = new Button("Delete Item");
        addButton.setOnAction(e -> addItem());
        updateButton.setOnAction(e -> updateItem());
        deleteButton.setOnAction(e -> deleteItem());

        
        inventoryListView = new ListView<>();
        
        
        VBox inputBox = new VBox(10, nameField, quantityField, priceField);
        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        VBox layout = new VBox(10, inputBox, buttonBox, inventoryListView);

        primaryStage.setScene(new Scene(layout, 400, 400));
        primaryStage.show();
    }

    
    private void addItem() {
        try {
            String name = nameField.getText();
            if (name.isEmpty()) {
                showAlert("Error", "Name cannot be empty");
                return;
            }

            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            Item item = new Item(name, quantity, price);
            inventory.add(item);
            updateListView();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for quantity and price.");
        }
    }

    
    private void updateItem() {
        int selectedIdx = inventoryListView.getSelectionModel().getSelectedIndex();
        if (selectedIdx >= 0) {
            try {
                Item selectedItem = inventory.get(selectedIdx);
                selectedItem.setName(nameField.getText());
                selectedItem.setQuantity(Integer.parseInt(quantityField.getText()));
                selectedItem.setPrice(Double.parseDouble(priceField.getText()));
                updateListView();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter valid numbers for quantity and price.");
            }
        } else {
            showAlert("Error", "Please select an item to update.");
        }
    }

    
    private void deleteItem() {
        int selectedIdx = inventoryListView.getSelectionModel().getSelectedIndex();
        if (selectedIdx >= 0) {
            inventory.remove(selectedIdx);
            updateListView();
        } else {
            showAlert("Error", "Please select an item to delete.");
        }
    }

    
    private void updateListView() {
        inventoryListView.getItems().clear();
        for (Item item : inventory) {
            inventoryListView.getItems().add(item.toString());
        }
    }

    
    private void clearFields() {
        nameField.clear();
        quantityField.clear();
        priceField.clear();
    }

    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


class Item {
    private String name;
    private int quantity;
    private double price;

    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return name + " - Quantity: " + quantity + ", Price: $" + price;
    }
}
