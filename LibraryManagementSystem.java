import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;


public class LibraryManagementSystem extends Application {

    private static final String DB_URL = "jdbc:sqlite:library.db";
    private Connection connection;

    private TextField usernameField, passwordField, bookTitleField, bookAuthorField, bookGenreField;
    private ListView<String> bookListView;
    private int currentUserId = -1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        connectToDatabase();

        primaryStage.setTitle("Library Management System");

        
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        usernameField = new TextField();
        passwordField = new PasswordField();
        Button registerButton = new Button("Register");
        Button loginButton = new Button("Login");

        registerButton.setOnAction(e -> registerUser());
        loginButton.setOnAction(e -> loginUser());

        HBox userBox = new HBox(10, usernameLabel, usernameField, passwordLabel, passwordField, registerButton, loginButton);

        
        Label bookTitleLabel = new Label("Book Title:");
        Label bookAuthorLabel = new Label("Author:");
        Label bookGenreLabel = new Label("Genre:");
        bookTitleField = new TextField();
        bookAuthorField = new TextField();
        bookGenreField = new TextField();
        Button addBookButton = new Button("Add Book");
        Button borrowBookButton = new Button("Borrow Book");

        addBookButton.setOnAction(e -> addBook());
        borrowBookButton.setOnAction(e -> borrowBook());

        HBox bookBox = new HBox(10, bookTitleLabel, bookTitleField, bookAuthorLabel, bookAuthorField, bookGenreLabel, bookGenreField, addBookButton, borrowBookButton);

        
        bookListView = new ListView<>();
        updateBookListView();

        VBox layout = new VBox(15, userBox, bookBox, bookListView);
        layout.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(layout, 600, 400));
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();

            // Create tables if they don't exist
            statement.execute("CREATE TABLE IF NOT EXISTS Users (userId INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS Books (bookId INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, author TEXT, genre TEXT, isAvailable BOOLEAN DEFAULT 1)");
            statement.execute("CREATE TABLE IF NOT EXISTS BorrowedBooks (borrowId INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, bookId INTEGER, borrowDate DATE DEFAULT CURRENT_DATE, FOREIGN KEY (userId) REFERENCES Users(userId), FOREIGN KEY (bookId) REFERENCES Books(bookId))");

            System.out.println("Database connected and tables created if they didn't exist.");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Username already exists or invalid data.");
        }
    }

    private void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String sql = "SELECT userId FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                currentUserId = rs.getInt("userId");
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while logging in.");
        }
    }

    private void addBook() {
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        String genre = bookGenreField.getText();

        String sql = "INSERT INTO Books (title, author, genre, isAvailable) VALUES (?, ?, ?, 1)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Book Added", "Book added to the library successfully!");
            updateBookListView();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Add Book Failed", "Could not add book to the library.");
        }
    }

    private void borrowBook() {
        String selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to borrow.");
            return;
        }

        String[] bookInfo = selectedBook.split(" - ");
        int bookId = Integer.parseInt(bookInfo[0].substring(4));  // Extract bookId from display string

        String sql = "INSERT INTO BorrowedBooks (userId, bookId) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, currentUserId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
            updateBookAvailability(bookId, false);
            showAlert(Alert.AlertType.INFORMATION, "Book Borrowed", "You have successfully borrowed the book!");
            updateBookListView();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Borrow Failed", "An error occurred while borrowing the book.");
        }
    }

    private void updateBookAvailability(int bookId, boolean isAvailable) {
        String sql = "UPDATE Books SET isAvailable = ? WHERE bookId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Could not update book availability: " + e.getMessage());
        }
    }

    private void updateBookListView() {
        bookListView.getItems().clear();
        String sql = "SELECT * FROM Books WHERE isAvailable = 1";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int bookId = rs.getInt("bookId");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                bookListView.getItems().add("ID: " + bookId + " - " + title + " by " + author + " (" + genre + ")");
            }
        } catch (SQLException e) {
            System.out.println("Could not retrieve books: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        try {
            if (connection != null) connection.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println("Could not close database connection: " + e.getMessage());
        }
    }
}
