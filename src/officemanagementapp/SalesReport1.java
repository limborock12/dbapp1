package DBapp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class SalesReport1 {
	public SalesReport1() {}
	
	public void generateReport() {
		
		int 	year = 0;
		int 	month = 0;
		int 	recordcount = 0;
		String 	status;
		String 	orderDate;
		double 	dailySales = 0.0;
		double	totalSales = 0.0;
		Scanner sc = new Scanner(System.in);
		PreparedStatement pstmt;
		ResultSet rs;
		
		try {
			
			System.out.println("Provide the parameters for the report");
			System.out.println("Enter the year (YYYY)");
			year = sc.nextInt();
			System.out.println("Enter the month (MM)");
			month = sc.nextInt();
			
			System.out.println("Preparing the Report...");
			dbconnect db = new dbconnect();
			
			pstmt = db.conn.prepareStatement("SELECT o.orderDate, o.status, SUM(od.quantityOrdered * od.priceEach) AS dailySales " +
				    						 "FROM orders o " +
				    						 "JOIN orderdetails od ON o.orderNumber = od.orderNumber " +
				    						 "WHERE YEAR(o.orderDate) = ? AND MONTH(o.orderDate) = ? " +
				    						 "GROUP BY o.orderDate, o.status"
				);
				pstmt.setInt(1, year);
				pstmt.setInt(2, month);

			
			rs = pstmt.executeQuery();
			
			recordcount = 0;
			while(rs.next()) {
				
				
				recordcount++;
				if(recordcount == 1) {
					System.out.println("Sales Report for " + year + "-" + month + " based on status");
					System.out.println("---------------------------------------------------");
					System.out.println("Status          Order Date              Daily Sales");
					System.out.println("---------------------------------------------------");
				}
					status = rs.getString("status");
					orderDate = rs.getString("orderDate");
					dailySales = rs.getDouble("dailySales");
					totalSales += dailySales;
					
					System.out.printf("%-10s\t%-12s\t$%.2f\n", status, orderDate, dailySales);
					
				
				
			}
			if(recordcount == 0) {
				System.out.println("Report is empty");
			} else {
				System.out.println("---------------------------------------------------");
				System.out.printf("Total Sales: $%.2f\n", totalSales);
				System.out.println("---------------------------------------------------");
			}
			rs.close();
			pstmt.close();
			db.disconnect();
		} catch (Exception e) {
			System.out.println("An error occurred in generating the report");
			System.out.println(e.getMessage());
		
		}
	}


public int function() {
	int choice = 0;
	Scanner sc = new Scanner(System.in);

	System.out.println("Running Report Generator");
	generateReport();
	System.out.println("[1]-Generate Again");
	System.out.println("[2]-Exit Report Generation");
	choice = sc.nextInt();
	
	return choice;
}

public static void main(String[] args) {
	SalesReport1 r = new SalesReport1();
	Scanner sc = new Scanner(System.in);
	int choice;
	
	do {
		choice = r.function();
		if(choice == 2) {
			 System.out.println("Exiting report generation...");
		}
	} while(choice != 2);
	sc.close();
}

}