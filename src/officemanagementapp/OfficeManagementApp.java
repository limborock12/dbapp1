/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package DBapp;

/**
 *
 * @author Lance
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class OfficeManagementApp {

    private Connection conn;

    public OfficeManagementApp() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=12345");
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int addOffice(String officeCode, String city, String phone, String addressLine1, String addressLine2, String state, String country, String postalCode, String territory) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO offices VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, officeCode);
            pstmt.setString(2, city);
            pstmt.setString(3, phone);
            pstmt.setString(4, addressLine1);
            pstmt.setString(5, addressLine2);
            pstmt.setString(6, state);
            pstmt.setString(7, country);
            pstmt.setString(8, postalCode);
            pstmt.setString(9, territory);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateOffice(String officeCode, String newCity, String newPhone, String newAddressLine1, String newAddressLine2, String newState, String newCountry, String newPostalCode, String newTerritory) {
        try {
            
            PreparedStatement selectStmt = conn.prepareStatement("SELECT * FROM offices WHERE officeCode = ?");
            selectStmt.setString(1, officeCode);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Old Office Information:");
                System.out.println("City: " + rs.getString("city"));
                

                
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE offices SET city = ?, phone = ?, addressLine1 = ?, addressLine2 = ?, state = ?, country = ?, postalCode = ?, territory = ? WHERE officeCode = ?");
                updateStmt.setString(1, newCity);
                updateStmt.setString(2, newPhone);
                updateStmt.setString(3, newAddressLine1);
                updateStmt.setString(4, newAddressLine2);
                updateStmt.setString(5, newState);
                updateStmt.setString(6, newCountry);
                updateStmt.setString(7, newPostalCode);
                updateStmt.setString(8, newTerritory);
                updateStmt.setString(9, officeCode);
                return updateStmt.executeUpdate();
            } else {
                System.out.println("Office not found.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteOffice(String officeCode) {
        try {
            
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM employees WHERE officeCode = ?");
            checkStmt.setString(1, officeCode);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Error: Cannot delete office because it is referenced by employees.");
                return 0;
            }

            
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM offices WHERE officeCode = ?");
            deleteStmt.setString(1, officeCode);
            return deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void viewOffice(String officeCode) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM offices WHERE officeCode = ?");
            pstmt.setString(1, officeCode);
            ResultSet rs = pstmt.executeQuery();

            // Display office information
            if (rs.next()) {
                System.out.println("Office Information:");
                System.out.println("Office Code: " + rs.getString("officeCode"));
                System.out.println("City: " + rs.getString("city"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("Address Line 1: " + rs.getString("addressLine1"));
                System.out.println("Address Line 2: " + rs.getString("addressLine2"));
                System.out.println("State: " + rs.getString("state"));
                System.out.println("Country: " + rs.getString("country"));
                System.out.println("Postal Code: " + rs.getString("postalCode"));
                System.out.println("Territory: " + rs.getString("territory"));
                
            } else {
                System.out.println("Office not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void generateSalesReport(int year, int month) {
        try {
            
            String sql = "SELECT productCode, orderDate, SUM(quantityOrdered * priceEach) AS totalSales " +
                         "FROM orderdetails " +
                         "JOIN orders ON orderdetails.orderNumber = orders.orderNumber " +
                         "WHERE YEAR(orderDate) = ? AND MONTH(orderDate) = ? " +
                         "GROUP BY productCode, orderDate";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            ResultSet rs = pstmt.executeQuery();

            
            System.out.println("Sales Report for " + year + "-" + month);
            System.out.println("-----------------------------------------------------");
            System.out.println("Product Code\tOrder Date\tTotal Sales");
            System.out.println("-----------------------------------------------------");

            double totalSales = 0;

            
            while (rs.next()) {
                String productCode = rs.getString("productCode");
                String orderDate = rs.getString("orderDate");
                double salesAmount = rs.getDouble("totalSales");

                System.out.printf("%-15s\t%-10s\t$%.2f\n", productCode, orderDate, salesAmount);
                totalSales += salesAmount;
            }

            
            System.out.println("-----------------------------------------------------");
            System.out.printf("Total Sales: $%.2f\n", totalSales);
            System.out.println("-----------------------------------------------------");

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main() {
         OfficeManagementApp app = new OfficeManagementApp();
        Scanner scanner = new Scanner(System.in);
        int choice;
        
        do {
            System.out.println("Office Management Application");
            System.out.println("1. Add Office");
            System.out.println("2. Update Office");
            System.out.println("3. Delete Office");
            System.out.println("4. View Office");
            System.out.println("5. Generate sales report");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            
            switch (choice) {
                case 1:
                    // Add Office
                    System.out.println("Enter details for the new office:");
                    System.out.print("Office Code: ");
                    String newOfficeCode = scanner.next();
                    System.out.print("City: ");
                    String city = scanner.next();
                    System.out.print("Phone: ");
                    String phone = scanner.next();
                    System.out.print("Address Line 1: ");
                    String addressLine1 = scanner.next();
                    System.out.print("Address Line 2 (optional): ");
                    String addressLine2 = scanner.next();
                    System.out.print("State: ");
                    String state = scanner.next();
                    System.out.print("Country: ");
                    String country = scanner.next();
                    System.out.print("Postal Code: ");
                    String postalCode = scanner.next();
                    System.out.print("Territory: ");
                    String territory = scanner.next();
    
                    int result = app.addOffice(newOfficeCode, city, phone, addressLine1, addressLine2, state, country, postalCode, territory);
                        if (result > 0) {
                            System.out.println("Office added successfully.");
                         } else {
                            System.out.println("Failed to add office.");
                    }
                    break;
                case 2:
                    // Update Office
                    System.out.print("Enter the office code to update: ");
                    String officeCodeToUpdate = scanner.next();
                    System.out.println("Enter new details for the office:");
                    System.out.print("New City: ");
                    String newCity = scanner.next();
                    System.out.print("New Phone: ");
                    String newPhone = scanner.next();
                    System.out.print("New Address Line 1: ");
                    String newAddressLine1 = scanner.next();
                    System.out.print("New Address Line 2 (optional): ");
                    String newAddressLine2 = scanner.next();
                    System.out.print("New State: ");
                    String newState = scanner.next();
                    System.out.print("New Country: ");
                    String newCountry = scanner.next();
                    System.out.print("New Postal Code: ");
                    String newPostalCode = scanner.next();
                    System.out.print("New Territory: ");
                    String newTerritory = scanner.next();
    
                    int updateResult = app.updateOffice(officeCodeToUpdate, newCity, newPhone, newAddressLine1, newAddressLine2, newState, newCountry, newPostalCode, newTerritory);
                        if (updateResult > 0) {
                             System.out.println("Office updated successfully.");
                        } else {
                            System.out.println("Failed to update office.");
                        }
                    break;

                case 3:
                    // Delete Office
                    System.out.print("Enter the office code to delete: ");
                    String officeCodeToDelete = scanner.next();
                    int deleteResult = app.deleteOffice(officeCodeToDelete);
                    if (deleteResult > 0) {
                        System.out.println("Office deleted successfully.");
                    } else {
                        System.out.println("Failed to delete office.");
                    }
                    break;
                case 4:
                    // View Office
                    System.out.print("Enter the office code to view: ");
                    String officeCodeToView = scanner.next();
                    app.viewOffice(officeCodeToView);
                    break;
                    
                case 5:
                       System.out.print("Enter the year (YYYY): ");
                    int year = scanner.nextInt();
                    System.out.print("Enter the month (MM): ");
                    int month = scanner.nextInt();
                    app.generateSalesReport(year, month);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    Main.main(new String[0]);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    break;
            }
            
        } while (choice != 5);
        
        scanner.close();
    
    }
}
