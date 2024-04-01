
package DBapp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class product {

	public String productCode, productName, productLine, productScale, productVendor, productDescription;
	public int quantityInStock, year, orderNumber, quantityOrdered;
	public double buyPrice, MSRP, priceEach;
	public boolean hasRecord;
	
	public product () {
		productCode = "";
		productName = "";
		productLine = "";
		productScale = "";
		productVendor = "";
		productDescription = "";
		quantityInStock = 0;
		buyPrice = 0;
		MSRP = 0;
		hasRecord = false;
		year = 0;
		quantityOrdered = 0;
		priceEach = 0;
	}

	public void addProduct() {
		
		Scanner scanner = new Scanner(System.in);
		PreparedStatement statement;				
		try {
			System.out.println("Product details to be ADDED are:");
			System.out.println("Product Code: " + productCode);
			System.out.println("Product Name: " + productName);
			System.out.println("Product Line: " + productLine);
			System.out.println("Product Scale: " + productScale);
			System.out.println("Product Vendor: " + productVendor);
			System.out.println("Product Description: " + productDescription);
			System.out.println("Quantity In Stock: " + quantityInStock);
			System.out.println("Buy Price: " + buyPrice);
			System.out.println("MSRP: " + MSRP);
			System.out.println("Press ENTER to ADD");
			scanner.nextLine();
				
			dbconnect db = new dbconnect();	
			
			statement = db.conn.prepareStatement("INSERT INTO products (productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP) VALUES (?,?,?,?,?,?,?,?,?)");
			
			statement.setString(1, productCode);
			statement.setString(2, productName);
			statement.setString(3, productLine);
			statement.setString(4, productScale);
			statement.setString(5, productVendor);
			statement.setString(6, productDescription);
			statement.setInt(7, quantityInStock);
			statement.setDouble(8, buyPrice);
			statement.setDouble(9, MSRP);
			
			
			statement.executeUpdate();				
			statement.close();						
			
			System.out.println("Product has been saved to Database");			
			db.disconnect();					
			
		} catch (Exception e) {
	        System.out.println("An error occured while adding Product");
	        System.out.println(e.getMessage());	
		}
	}
	
	public void updateProduct() {

		Scanner scanner = new Scanner(System.in);
		PreparedStatement statement;
		try {
			System.out.println("Product details to be UPDATED are:");
			System.out.println("Product Name: " + productName);
			System.out.println("Product Line: " + productLine);
			System.out.println("Product Scale: " + productScale);
			System.out.println("Product Vendor: " + productVendor);
			System.out.println("Product Description: " + productDescription);
			System.out.println("Quantity In Stock: " + quantityInStock);
			System.out.println("Buy Price: " + buyPrice);
			System.out.println("MSRP: " + MSRP);
			System.out.println("Press ENTER to UPDATE");
			scanner.nextLine();
			
			dbconnect db = new dbconnect();		
			
			statement = db.conn.prepareStatement("UPDATE products SET productName=?, productLine=?, productScale=?, productVendor=?, productDescription=?, quantityInStock=?, buyPrice=?, MSRP=? WHERE productCode=?");
			
			statement.setString(1, productName);
			statement.setString(2, productLine);
			statement.setString(3, productScale);
			statement.setString(4, productVendor);
			statement.setString(5, productDescription);
			statement.setInt(6, quantityInStock);
			statement.setDouble(7, buyPrice);
			statement.setDouble(8, MSRP);
			statement.setString(9, productCode);
			
			statement.executeUpdate();				
			statement.close();						
			
			System.out.println("Product has been updated in the Database");			
			db.disconnect();					
			
		} catch (Exception e) {
	        System.out.println("An error occured while updating Product");
	        System.out.println(e.getMessage());	
		}
	}
	
	public void deleteProduct() {
		
		Scanner scanner = new Scanner(System.in);
		PreparedStatement statement;
		try {

			System.out.println("Press ENTER to DELETE");
			scanner.nextLine();
		
			dbconnect db = new dbconnect();			
			
			statement = db.conn.prepareStatement("DELETE FROM products WHERE productCode=?");
			statement.setString(1, productCode);
			
			statement.executeUpdate();												 
			statement.close();						
			
			System.out.println("Product has been DELETED");			
			db.disconnect();						
			
		} catch (Exception e) {
	        System.out.println("An error occured while deleting Product and cannot be deleted.");
	        System.out.println("Possible cause: record is being used by other records");
	        System.out.println(e.getMessage());	
		}
	}
	
	public void viewProduct() {

			PreparedStatement statement;
			ResultSet rset;						
			
			String query = "SELECT p.productCode, p.productName, o.orderNumber, od.quantityOrdered, od.priceEach, YEAR(o.orderDate) AS orderYear " +
		               "FROM products p " +
		               "INNER JOIN orderdetails od ON p.productCode = od.productCode " +
		               "INNER JOIN orders o ON od.orderNumber = o.orderNumber " +
		               "WHERE p.productCode = ? AND YEAR(o.orderDate) = ?";
	
			try {
				
				hasRecord = false;
				System.out.println("Retrieving the record of Product: " + productCode +" in Year: " + year);
			
				dbconnect db = new dbconnect();			
				
				statement = db.conn.prepareStatement(query);
				
				statement.setString(1, productCode);
				statement.setInt(2, year);
				rset = statement.executeQuery();				
				
				System.out.printf("%15s %25s %15s %16s %15s %15s%n", "Product Code", "Product Name", "Order Number", "Quantity Ordered", "Price Each", "Order Year");
				
				while (rset.next()) {								
					hasRecord = true;
					productCode = rset.getString("productCode");
					productName = rset.getString("productName");
					orderNumber = rset.getInt("orderNumber");
					quantityOrdered = rset.getInt("quantityOrdered");
					priceEach = rset.getDouble("priceEach");
					year = rset.getInt("orderYear");
					System.out.printf("%15s %25s %15s %15s %15s %15s%n", productCode, productName, orderNumber, quantityOrdered, priceEach, year);
				}
				
				rset.close();							
				statement.close();						
				db.disconnect();					
				
				if (hasRecord == false) {
					System.out.println("Product Code/Order not found.");
				} 
				else {
					 System.out.println ("######################################################");
					 System.out.println("Product successfully retrieved");
				}
			} catch (Exception e) {
		        System.out.println("An error occured while retrieving Product");
		        System.out.println(e.getMessage());					
			}	
		}
		
	public void viewRecord(String code) {
		
		PreparedStatement statement;
		
		ResultSet rset;
		
		try {
			
			hasRecord = false;
			dbconnect db = new dbconnect();
			statement = db.conn.prepareStatement("SELECT * FROM products WHERE productCode = ?");
			statement.setString(1, code);
			rset = statement.executeQuery();
			
			while (rset.next()) {
				hasRecord = true;
				productCode = rset.getString("productCode");
				productName = rset.getString("productName");
				productLine = rset.getString("productLine");
				productScale = rset.getString("productScale");
				productVendor = rset.getString("productVendor");
				productDescription = rset.getString("productDescription");
				quantityInStock = rset.getInt("quantityInStock");
				buyPrice = rset.getDouble("buyPrice");
				MSRP = rset.getDouble("MSRP");
				
				System.out.println("Product Code: " + rset.getString("productCode"));
				System.out.println("Product Name: " + rset.getString("productName"));
				System.out.println("Product Line: " + rset.getString("productLine"));
				System.out.println("Product Scale: " + rset.getString("productScale"));
				System.out.println("Product Vendor: " + rset.getString("productVendor"));
				System.out.println("Product Description: " + rset.getString("productDescription"));
				System.out.println("Quantity In Stock: " + rset.getInt("quantityInStock"));
				System.out.println("Buy Price: " + rset.getDouble("buyPrice"));
				System.out.println("MSRP: " + rset.getDouble("MSRP"));
			}
			
			rset.close();							
			statement.close();						
			db.disconnect();	
		}
		catch (Exception e) {
				System.out.println("An error occured while retrieving Record");
				System.out.println(e.getMessage());		
		}
		
	}
	
	
	public int process() {
		
		Scanner scanner = new Scanner(System.in);	
		
		System.out.println("######################################################");
		System.out.println("Select a function for PRODUCTS");
		System.out.println("[1] - Create a new Product");
		System.out.println("[2] - Update a record of a Product");
		System.out.println("[3] - Delete a record of a Product");
		System.out.println("[4] - View a record of a Product");
		System.out.println("[5] - Exit");
		System.out.println("######################################################");
		System.out.println("Enter number of choice: ");
		
		int choice = scanner.nextInt();
		
		if (choice == 1) {
			
			System.out.println("Creating new Product ");
			System.out.println("Enter Product Code: ");
			scanner.nextLine();
			productCode= scanner.nextLine();
			System.out.println("Enter Product Name: ");
			productName = scanner.nextLine();
			System.out.println("Enter Product Line: ");
			System.out.println("Must be one of the following: ");
			System.out.println("Classic Cars");
			System.out.println("Motorcycles");
			System.out.println("Planes");
			System.out.println("Ships");
			System.out.println("Trains");
			System.out.println("Trucks and Buses");
			System.out.println("Vintage Cars");
			productLine = scanner.nextLine();
			System.out.println("Enter Product Scale: ");
			productScale = scanner.nextLine();
			System.out.println("Enter Product Vendor: ");
			productVendor = scanner.nextLine();
			System.out.println("Enter Product Description: ");
			productDescription = scanner.nextLine();
			System.out.println("Enter Quantity In Stock: ");
			quantityInStock = scanner.nextInt();
			System.out.println("Enter Buy Price: ");
			buyPrice = scanner.nextDouble();
			System.out.println("Enter MSRP: ");
			MSRP = scanner.nextDouble();
			addProduct();
			
		} else if (choice == 2) {
			
			System.out.println("Enter Product Code to update: ");
			scanner.nextLine();
			productCode = scanner.nextLine();
			
			viewRecord(productCode);
			
			if(hasRecord == false) {
				System.out.println("There is no product with this code.");
			}
			else {
				System.out.println("New Product Name: ");
				productName = scanner.nextLine();
				System.out.println("New Product Line: ");
				System.out.println("Must be one of the following: ");
				System.out.println("Classic Cars");
				System.out.println("Motorcycles");
				System.out.println("Planes");
				System.out.println("Ships");
				System.out.println("Trains");
				System.out.println("Trucks and Buses");
				System.out.println("Vintage Cars");
				productLine = scanner.nextLine();
				System.out.println("New Product Scale: ");
				productScale = scanner.nextLine();
				System.out.println("New Product Vendor: ");
				productVendor = scanner.nextLine();
				System.out.println("New Product Description: ");
				productDescription = scanner.nextLine();
				System.out.println("New Quantity In Stock: ");
				quantityInStock = scanner.nextInt();
				System.out.println("New Buy Price: ");
				buyPrice = scanner.nextDouble();
				System.out.println("New MSRP: ");
				MSRP = scanner.nextDouble();
				
				updateProduct();
			}
			
			
		} else if (choice == 3) {
			
			System.out.println("Enter Product Code to delete: ");
			scanner.nextLine();
			productCode = scanner.nextLine();
			
			viewRecord(productCode);
			
			if(hasRecord == false) {
				System.out.println("There is no product with this code.");
			}
			else {
				deleteProduct();
			}
			
		} else if (choice == 4) {
			
			System.out.println("Enter Product Code to view: ");
			scanner.nextLine();
			productCode = scanner.nextLine();
			
			System.out.println("Enter Year to be viewed: ");
			year = scanner.nextInt();
			
			viewProduct();
			
		} else if (choice == 5) {
			System.out.println("Exiting Product Functions");
			System.out.println("Function terminated");
			return choice;
		} else {
			System.out.println("Invalid Choice");
		}
		
		System.out.println ("Press any key to return to Menu");
		scanner.nextLine();
		scanner.nextLine();
		
		return choice;
	}
	
}
