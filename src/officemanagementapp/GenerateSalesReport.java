package DBapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GenerateSalesReport {
    public void generateSalesReport(int year, int month) {
        try {
            dbconnect db = new dbconnect();
            Connection conn = db.conn;
            
            String sql = "SELECT od.productCode, p.productName, o.orderDate, SUM(od.quantityOrdered * od.priceEach) AS totalSales " +
                     "FROM orderdetails od " +
                     "JOIN orders o ON od.orderNumber = o.orderNumber " +
                     "JOIN products p ON od.productCode = p.productCode " +
                     "WHERE YEAR(o.orderDate) = ? AND MONTH(o.orderDate) = ? " +
                     "GROUP BY od.productCode, p.productName, o.orderDate";


            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Sales Report for " + year + "-" + month);
            System.out.println("-----------------------------------------------------");
            System.out.println("Product Code\tProduct Name\tOrder Date\tTotal Sales");
            System.out.println("-----------------------------------------------------");

            double totalSales = 0;

            while (rs.next()) {
                String productCode = rs.getString("productCode");
                String orderDate = rs.getString("orderDate");
                double salesAmount = rs.getDouble("totalSales");
                String productName = rs.getString("productName");

                 System.out.printf("%-15s\t%-15s\t%-10s\t$%.2f\n", productCode, productName, orderDate, salesAmount);
            totalSales += salesAmount;
            }

            System.out.println("-----------------------------------------------------");
            System.out.printf("Total Sales: $%.2f\n", totalSales);
            System.out.println("-----------------------------------------------------");

            pstmt.close();
            db.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int function() {
        int choice = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("Running Report Generator");
        System.out.print("Enter the year (YYYY): ");
        int year = sc.nextInt();
        System.out.print("Enter the month (MM): ");
        int month = sc.nextInt();
        generateSalesReport(year, month);
        System.out.println("[1]-Generate Again");
        System.out.println("[2]-Exit Report Generation");
        choice = sc.nextInt();

        return choice;
    }

    public static void main(String[] args) {
        GenerateSalesReport r = new GenerateSalesReport();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            choice = r.function();
            if (choice == 2) {
                System.out.println("Exiting report generation...");
            }
        } while (choice != 2);
        sc.close();
    }
}
