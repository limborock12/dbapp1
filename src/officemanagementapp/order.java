
package dbsales;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class order {

	public int productCount, orderNumber, quantity, csNumber, counter;
	public String productCodes[], productName, reqDate, comment;
	public double priceEach;
	public boolean isComplete, bool;
	
	public order () {
		productCount = 0;
        orderNumber = 0;
        quantity = 0;
        csNumber = 0;
        counter = 0;
        productName = "";
        reqDate = "";
        comment = "";
        priceEach = 0.0;
        isComplete = false;
        bool = false;
	}
	
	public void getProductName(String code) {
		dbconnection db = new dbconnection();
		PreparedStatement statement;
		ResultSet rset;					
		
		try {
			statement = db.conn.prepareStatement("SELECT productName FROM products WHERE productCode LIKE ?");
			statement.setString(1, code);
			rset = statement.executeQuery();
			
			if(rset.next()) {
				productName = rset.getString("productName");
			}
			else {
				productName = "Does not exist.";
			}
			
			rset.close();							
			statement.close();						
			db.disconnect();
			
		}
		catch (Exception e) {
			System.out.println("An error occured while retrieving Product");
	        System.out.println(e.getMessage());	
		}
		
		
	}
	
	public void inputOrderDetails() {
		dbconnection db = new dbconnection();
		PreparedStatement detailsstatement;
		ResultSet rset;	
		Scanner scanner = new Scanner(System.in);
		String choice;
		int i;
		try {  
	           detailsstatement = db.conn.prepareStatement("SELECT MAX(orderNumber) AS last_number FROM orders");
	           PreparedStatement statement = null;
	           rset = detailsstatement.executeQuery();
	           if(rset.next()) {
	        	   orderNumber = rset.getInt("last_number");
		           orderNumber++;
	           }
	           System.out.println("Enter Customer Number: ");
	           csNumber = scanner.nextInt();
	           
	           db.conn.setAutoCommit(false);
	           
	           for(i = 0; i < productCount; i++) {
	        	   
	        	    reqDate = "";
	        	    quantity = 0;
	        	    priceEach = 0;
	        	   
					System.out.println("PRODUCT "+ (i+1));
					scanner.nextLine();
					System.out.println("What is the Product Code? ");
					
					this.productCodes[i] = scanner.nextLine();
					System.out.println("Is this the correct Product?");
					getProductName(this.productCodes[i]);
					
					System.out.println("Product Name: " + productName);	
					
					if (!productName.equalsIgnoreCase("Does not exist.")) {
					System.out.println("'Y' for Yes || 'N' for No.");
					choice = scanner.nextLine();
					
						if (choice.equalsIgnoreCase("Y")) {
							
							System.out.println("Enter Required Date (YYYY-MM-DD): ");
							reqDate = scanner.nextLine();
							
							System.out.println("Enter Quantity: ");
							quantity = scanner.nextInt();
							
							System.out.println("Enter Price Each: ");
							priceEach = scanner.nextDouble();
						
							System.out.println("Enter Comment: ");
							scanner.nextLine();
							comment = scanner.nextLine();
							
							statement = db.conn.prepareStatement("INSERT INTO orders (orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (?, CURRENT_DATE(), ?, NULL, ?, ?, ?)");
							statement.setInt(1, orderNumber);
							statement.setString(2, reqDate);
							statement.setString(3, "In Process");
							statement.setString(4, this.comment);
							statement.setInt(5, csNumber);
							
							statement.addBatch();
							
							detailsstatement = db.conn.prepareStatement("INSERT INTO orderdetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES (?, ?, ?, ?, ?)");
							detailsstatement.setInt(1, orderNumber);
							detailsstatement.setString(2, this.productCodes[i]);
							detailsstatement.setInt(3, quantity);
							detailsstatement.setDouble(4, priceEach);
							detailsstatement.setInt(5, (i+1));
							
							detailsstatement.addBatch();
							
						}
						else if (choice.equalsIgnoreCase("N")) 
						{
							System.out.println("Product not added to Order");
							i--;
						}
						else {
							i--;
							System.out.println("Invalid input. Try again");
							}
					}
	           	}
	       
	           
	           statement.executeBatch();
	           detailsstatement.executeBatch();
	           
	    	   db.conn.commit();
	    	   
	           rset.close();							
	           statement.close();
	           detailsstatement.close(); 
	           
	        }
		catch (Exception e) {
	            System.out.println("There was an error in retrieving the data. Place a new Order.");
	            
	            try {
	                if (db.conn != null) {
	                    db.conn.rollback();
	                    System.out.println("Transaction not saved.");
	                }
	            } catch (Exception e1) {
	            	System.out.println(e1.getMessage());
	            }
	        } 
		 finally {
	            // Close the connection
	            if (db.conn != null) {
	                try {
	                    db.disconnect();
	                    
	                } catch (Exception e) {
	                    System.out.println("Error disconnecting from database.");
	                }
	            }
	        }
	}
	
	public void updateOrder(){
		dbconnection db = new dbconnection();
		ResultSet rset;	
		PreparedStatement statement;
		Scanner scanner = new Scanner(System.in);
		
		int orderNumber, customerNumber = 0;
		String orderDate = null, requiredDate = null, shippedDate = null, status = "In Process", comments = null;
		
		try {
			System.out.println("Enter Order Number: ");
			orderNumber = scanner.nextInt();
			scanner.nextLine();
			
		    statement = db.conn.prepareStatement("SELECT * FROM orders WHERE orderNumber = ?");
		    statement.setInt(1, orderNumber);
		    rset = statement.executeQuery();
		    
		    if (rset.next()) {
		        orderDate = rset.getString("orderDate");
		        requiredDate = rset.getString("requiredDate");
		        shippedDate = rset.getString("shippedDate");
		        status = rset.getString("status");
		        comments = rset.getString("comments");
		        customerNumber = rset.getInt("customerNumber");
		    }
		    
		    if(status.equalsIgnoreCase("In Process")) {
		    	System.out.printf("%12s %20s %20s %20s %10s %15s %15s%n", "Order Number", "Order Date", "Required Date", "Shipped Date", "Status", "Comments", "Customer Num.");
		    	System.out.printf("%-12s %-20s %-20s %20s %-10s %15s %15s%n", orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber);
		    	System.out.println("##################################################################################################################");
		    	System.out.println("Are the details correct? (Y/N): ");
		    	String choice = scanner.nextLine();
		    	if(choice.equalsIgnoreCase("Y")) {
		    		System.out.println("What field would you like to update? ");
		    		System.out.println("[1] - Required Date");
		    		System.out.println("[2] - Shipped Date");
		    		System.out.println("[3] - Status");
		    		System.out.println("[4] - Comments");
		    		System.out.println("[5] - EXIT");
		    		int otherchoice = scanner.nextInt();
		    		scanner.nextInt();
		    		scanner.nextLine();
		    		switch(otherchoice) {
		    			case 1: System.out.println("Enter new Required Date: ");
		    					String newReqDate = scanner.nextLine();
		    					statement = db.conn.prepareStatement("UPDATE orders SET requiredDate = ?");
								statement.setString(1, newReqDate);
		    					statement.executeUpdate();
		    					break;		
			    		case 2: System.out.println("Enter new Shipped Date: ");
								String newShipDate = scanner.nextLine();
								statement = db.conn.prepareStatement("UPDATE orders SET shippedDate = ?");
								statement.setString(1, newShipDate);
								statement.executeUpdate();
								break;
			    		case 3: System.out.println("Enter new Status: ");
								String newStatus = scanner.nextLine();
								statement = db.conn.prepareStatement("UPDATE orders SET status = ?");
								statement.setString(1, newStatus);
								statement.executeUpdate();
								break;
			    		case 4: System.out.println("Enter new Comment: ");
								String newCom = scanner.nextLine();
								statement = db.conn.prepareStatement("UPDATE orders SET comments = ?");
								statement.setString(1, newCom);
								statement.executeUpdate();
								break;
			    		case 5: process();
			    				break;
			    		default:	System.out.println("Invalid Input.");
			    					updateOrder();
		    			}
				    			
		    					
		    		}
		    	else if (choice.equalsIgnoreCase("N")){
		    		updateOrder();
		    	}
		    	else {
		    		System.out.println("Invalid Input. Try Again");
		    		updateOrder();
		    	}
		    		
		    	}
		    else {
		    	System.out.println("You are not allowed to update this order.");
		    }
		    
		    
		    rset.close();
		    statement.close();
				  
		} catch (Exception e) {
		    System.out.println("Error");
		    System.out.println(e.getMessage());
		}
		
	}
	
	public void updateProduct(){
		
		dbconnection db = new dbconnection();
		ResultSet rset;	
		PreparedStatement statement;
		Scanner scanner = new Scanner(System.in);
		product prod = new product();
		int orderNumber;
		
		try {
			System.out.println("Enter Order Number: ");
			orderNumber = scanner.nextInt();
			//scanner.nextLine();
			
		    statement = db.conn.prepareStatement("SELECT p.productCode, p.productName FROM products p INNER JOIN orderdetails od ON p.productCode = od.productCode WHERE od.orderNumber = ?");
		    statement.setInt(1, orderNumber);
		    rset = statement.executeQuery();
		    boolean once = false;
		    
		    while (rset.next()) {
		        String productCode = rset.getString("productCode");
		        String productName= rset.getString("productName");
		        if(!once) {
		        	System.out.printf("%12s %70s%n","Product Code", "Product Name");
		        	once = true;
		        }
		        System.out.printf("%12s %70s%n", productCode, productName);
		        bool = true;
		    }
		    
		    
		    if(bool) {
			    System.out.println("");
			    System.out.println("Enter Product Code of Product you want updated: ");
			    String prodCode = scanner.nextLine();
			    prod.productCode = prodCode;
			    System.out.println("New Product Name: ");
				prod.productName = scanner.nextLine();
				System.out.println("New Product Line: ");
				System.out.println("Must be one of the following: ");
				System.out.println("Classic Cars");
				System.out.println("Motorcycles");
				System.out.println("Planes");
				System.out.println("Ships");
				System.out.println("Trains");
				System.out.println("Trucks and Buses");
				System.out.println("Vintage Cars");
				prod.productLine = scanner.nextLine();
				System.out.println("New Product Scale: ");
				prod.productScale = scanner.nextLine();
				System.out.println("New Product Vendor: ");
				prod.productVendor = scanner.nextLine();
				System.out.println("New Product Description: ");
				prod.productDescription = scanner.nextLine();
				System.out.println("New Quantity In Stock: ");
				prod.quantityInStock = scanner.nextInt();
				System.out.println("New Buy Price: ");
				prod.buyPrice = scanner.nextDouble();
				System.out.println("New MSRP: ");
				prod.MSRP = scanner.nextDouble();
			    
				prod.updateProduct();
			}
		    else {
		    	System.out.println("No Product Exists in this Order");
		    }
			
		    rset.close();
		    statement.close();
		    bool = false;
				  
		} catch (Exception e) {
		    System.out.println("Error. Please check your inputs and try again.");
		    System.out.println(e.getMessage());
		    bool = false;
		}
		
	}
	
public void deleteProduct(){
		
		dbconnection db = new dbconnection();
		ResultSet rset;	
		PreparedStatement statement;
		Scanner scanner = new Scanner(System.in);
		product prod = new product();
		int orderNumber;
		
		try {
			System.out.println("Enter Order Number: ");
			orderNumber = scanner.nextInt();
			scanner.nextLine();
			
		    statement = db.conn.prepareStatement("SELECT p.productCode, p.productName FROM products p INNER JOIN orderdetails od ON p.productCode = od.productCode WHERE od.orderNumber = ?");
		    statement.setInt(1, orderNumber);
		    rset = statement.executeQuery();
		    boolean once = false;
		    while (rset.next()) {
		        String productCode = rset.getString("productCode");
		        String productName= rset.getString("productName");
		        if(!once) {
		        	System.out.printf("%12s %70s%n","Product Code", "Product Name");
		        	once = true;
		        }
		        System.out.printf("%12s %70s%n", productCode, productName);
		        bool=true;
		    }
		    
		    if(bool) {
			    System.out.println("");
			    System.out.println("Enter Product Code of Product you want deleted: ");
			    String prodCode = scanner.nextLine();
			    prod.productCode = prodCode;
			    
			    prod.deleteProduct();
			}
		    else {
		    	System.out.println("No Product Exists in this Order");
		    }
			
		    rset.close();
		    statement.close();
		    bool = false;
				  
		} catch (Exception e) {
		    System.out.println("Error. Please check your inputs and try again.");
		    System.out.println(e.getMessage());
		    bool = false;
		}
		
	}
	
	public int process() {
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("######################################################");
		System.out.println("Order Transactions");
		System.out.println("[1] - Place a new Order");
		System.out.println("[2] - Update an Order");
		System.out.println("[3] - Update a Product in an Order");
		System.out.println("[4] - Delete a Product in an Order");
		System.out.println("[5] - Exit");
		System.out.println("######################################################");
		System.out.println("Enter number of choice: ");
		int choice = scanner.nextInt();
		
		if (choice == 1) {
			System.out.println("Creating a new Order");
			System.out.println("How many Products are in this Order?");
			productCount = scanner.nextInt();
			this.productCodes = new String[productCount];
			inputOrderDetails();
		}
		else if (choice == 2) {
			updateOrder();
		}
		else if (choice == 3) {
			updateProduct();
		}else if (choice == 4) {
			deleteProduct();
		}else if (choice == 5) {
			System.out.println("Exiting Order Transactions");
			System.out.println("Transaction Terminated");
			return choice;
		} else {
			System.out.println("Invalid Choice");
		}
		return choice;
	}
}