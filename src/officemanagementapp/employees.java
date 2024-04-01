/**
 Course Code: CCINFOM
 Purpose:     Provide a Java-based Database Application Performing Basic Data Operations of
              	1. Create a new Employee.
				2. Update a record of an Employee.
				3. Delete a record of an Employee.
				4. View a record of a specific Employee and the list of customers it is handling if it is a Sales Representative

              
 Created:     March 30, 2024
 Created By:  Sanchez, Darren Julian R.
 */

package DBapp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class employees {
	public int employeeNumber;											// mirrors the field employeeNumber in the employees table
	public String lastName, firstName, extension, email, officeCode;	// mirrors the fields: lastName, firstName, extension, 
																		// officeCode, email in the employees table
	public int reportsTo;												// mirrors the field reportsTo in the employees table
	public String jobTitle;												// mirrors the field jobTitle in the employees table
	public boolean norecord;
	
	public employees() {
		// initialize data items for employees
		employeeNumber = 0;
		lastName = "";
		firstName = "";
		extension = "";
		email = "";
		officeCode = "";
		reportsTo = 0;
		jobTitle = "";
		norecord = true;
		
	}
	
	public void addEmployee() {
		Scanner sc = new Scanner(System.in);
		PreparedStatement pstmt;
		try {
			System.out.println("Preparing to add employee data containing the following data");
			System.out.println("Employee Number: " + employeeNumber);
			System.out.println("Last Name: " + lastName);
			System.out.println("First Name: " + firstName);
			System.out.println("Extension: " + extension);
			System.out.println("Email: " + email);
			System.out.println("Office Code: " + officeCode);
			System.out.println("Reports to: " + reportsTo);
			System.out.println("Job Title: " + jobTitle);
			System.out.println("Press ENTER to save record to the Database");
			sc.nextLine();
			
			dbconnect db = new dbconnect();
			
			pstmt = db.conn.prepareStatement("INSERT INTO employees (employeeNumber, lastName, firstName, extension, email, officecode, reportsTo, jobTitle) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
					
			pstmt.setInt(1, employeeNumber);
			pstmt.setString(2, lastName);
			pstmt.setString(3, firstName);
			pstmt.setString(4, extension);
			pstmt.setString(5, email);
			pstmt.setString(6,  officeCode);
			pstmt.setInt(7, reportsTo);
			pstmt.setString(8, jobTitle);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			
			System.out.println("Employee recorded in the Database");
			db.disconnect();
			
		} catch (Exception e) {
			System.out.println("Error occured while adding an Employee Record");
			System.out.println(e.getMessage());
		}
	}
	
	public void updateEmployee() {
		Scanner 			sc = new Scanner(System.in);
		PreparedStatement 	pstmt;
		try {
			System.out.println("Preparing to update the Employee record containing the following data");
			System.out.println("Employee Number:  " + employeeNumber);
			System.out.println("Last name:   " + lastName);
			System.out.println("First name:  " + firstName);
			System.out.println("Extension: " + extension);
			System.out.println("Email: " + email);
			System.out.println("Office Code: " + officeCode);
			System.out.println("Reports to: " + reportsTo);
			System.out.println("Job Title: " + jobTitle);
			System.out.println("Press ENTER to update record to the Database");
			sc.nextLine();
			
			dbconnect db = new dbconnect();		
			pstmt = db.conn.prepareStatement("UPDATE Employees SET lastName=?, firstName=?, extension=?, email=?, officecode=?, reportsTo=?, jobTitle=? WHERE employeeNumber=?");
			
			
			pstmt.setString(1,lastName.isEmpty() ? null : lastName);
			pstmt.setString(2, firstName.isEmpty() ? null : firstName);
			pstmt.setString(3, extension.isEmpty() ? null : extension);
			pstmt.setString(4, email.isEmpty() ? null : email);
			pstmt.setString(5, officeCode.isEmpty() ? null : officeCode);
			pstmt.setInt(6, reportsTo == 0 ? null : reportsTo);
			pstmt.setString(7, jobTitle.isEmpty() ? null : jobTitle);
			pstmt.setInt(8, employeeNumber);
			
			pstmt.executeUpdate();				
			pstmt.close();
			
			System.out.println("Employee record updated in the Database");			
			db.disconnect();					
		} catch (Exception e) {
	        System.out.println("Error occured while updating a Employee Record");
	        System.out.println(e.getMessage());	
		}
	}
	
	public void viewEmployee() {
	    Scanner sc = new Scanner(System.in);
	    PreparedStatement pstmt;
	    ResultSet rs;
	    try {
	        norecord = true;
	        System.out.println("Retrieving the record of Employee " + employeeNumber);

	        dbconnect db = new dbconnect();

	        pstmt = db.conn.prepareStatement("SELECT e.lastName, e.firstName, e.extension, e.email, e.officecode, e.reportsTo, e.jobTitle " +
	                                         "FROM employees e " +
	                                         "WHERE e.employeeNumber = ?");
	        pstmt.setInt(1, employeeNumber);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            norecord = false;
	            lastName = rs.getString("lastName");
	            firstName = rs.getString("firstName");
	            extension = rs.getString("extension");
	            email = rs.getString("email");
	            officeCode = rs.getString("officeCode");
	            reportsTo = rs.getInt("reportsTo");
	            jobTitle = rs.getString("jobTitle");

	            System.out.println("Employee Details:");
	            System.out.println("Employee Number: " + employeeNumber);
	            System.out.println("Last Name: " + lastName);
	            System.out.println("First Name: " + firstName);
	            System.out.println("Extension: " + extension);
	            System.out.println("Email: " + email);
	            System.out.println("Office Code: " + officeCode);
	            System.out.println("Reports To: " + reportsTo);
	            System.out.println("Job Title: " + jobTitle);

	            if (jobTitle.equals("Sales Rep")) {
	                // Additional SQL statement to fetch customers handled by the Sales Representative
	                PreparedStatement customerPstmt = db.conn.prepareStatement("SELECT customerName FROM customers c JOIN employees e "
	                														+ "ON c.salesRepEmployeeNumber = e.employeeNumber WHERE salesRepEmployeeNumber = ?");
	                customerPstmt.setInt(1, employeeNumber);
	                ResultSet customerRs = customerPstmt.executeQuery();

	                System.out.println("Customers Handled:");
	                while (customerRs.next()) {
	                    String customerName = customerRs.getString("customerName");
	                    System.out.println(customerName);
	                }

	                customerRs.close();
	                customerPstmt.close();
	            }
	        }

	        rs.close();
	        pstmt.close();
	        db.disconnect();

	        if (norecord) {
	            System.out.println("No employee record found.");
	        }
	    } catch (Exception e) {
	        System.out.println("Error occurred while retrieving an Employee Record");
	        System.out.println(e.getMessage());
	    }
	}


	
	public void deleteEmployee() {
		Scanner 			sc = new Scanner(System.in);
		PreparedStatement 	pstmt;
		try {
			System.out.println("Preparing to delete the student record containing the following data");
			System.out.println("Employee Number:  " + employeeNumber);
			System.out.println("Last name:   " + lastName);
			System.out.println("First name:  " + firstName);
			System.out.println("Extension: " + extension);
			System.out.println("Email: " + email);
			System.out.println("Office Code: " + officeCode);
			System.out.println("Reports to: " + reportsTo);
			System.out.println("Job Title: " + jobTitle);
			System.out.println("Press ENTER to delete record to the Database");
			sc.nextLine();
		
			dbconnect db = new dbconnect();
			
			pstmt = db.conn.prepareStatement("DELETE FROM employees WHERE employeeNumber=?");
			pstmt.setInt(1, employeeNumber);
			
			pstmt.executeUpdate();
			pstmt.close();
			
			System.out.println("Employee record deleted in the Database");			
			db.disconnect();						
			
		} catch (Exception e) {
	        System.out.println("Error occured while deleting a Student Record. It may be being used by other records and cannot be deleted");
	        System.out.println(e.getMessage());	
		}
	}
	

	public int function() {
		/* this function is to provide an I/O module to facilitate the different record functions
		 * of the class. The function can be called by the MAIN MODULE of the DB Application.
		 */
		Scanner sc 	= new Scanner(System.in);		
		System.out.println ("------------------------------------------------------");
		System.out.println ("Functions available for Employee Record");
		System.out.println ("[1] - Create a new Employee Record");
		System.out.println ("[2] - Update a Employee Record");
		System.out.println ("[3] - View a Employee Record");
		System.out.println ("[4] - Delete a Employee Record");
		System.out.println ("[5] - Exit");
		System.out.println ("Enter function to perform:");
		int choice = sc.nextInt();
		
		switch(choice) {
		case 1: {
			System.out.println ("Creating new record of an Employee");
			System.out.println ("Enter Employee Number: ");
			employeeNumber = sc.nextInt();
			sc.nextLine();
			System.out.println ("Enter Last Name: ");
			lastName = sc.nextLine();
			System.out.println ("Enter First Name: ");
			firstName = sc.nextLine();
			System.out.println("Enter extension: ");
			extension = sc.nextLine();
			System.out.println("Enter Email: ");
			email = sc.nextLine();
			System.out.println("Enter Office Code: ");
			officeCode = sc.nextLine();
			System.out.println("Enter Employee Number of Superior: ");
			reportsTo = sc.nextInt();
			sc.nextLine();
			System.out.println("Enter Job Title: ");
			jobTitle = sc.nextLine();
			
			addEmployee();
			break;
			}
		case 2: {
			System.out.println("Enter Employee Number to update :");
			employeeNumber = sc.nextInt();
			sc.nextLine();
			viewEmployee();
			
			if (norecord) {
				System.out.println("No record to update.");
				break;
			} else {
				String newlastName  = new String();
				String newfirstName = new String();
				String newextension = new String();
				String newemail = new String();
				String newofficeCode = new String();
				String newreportsTo = new String();
				String newjobTitle = new String();
				
				System.out.println ("Enter updated Last Name: ");
				newlastName  = sc.nextLine();
				System.out.println ("Enter updated First Name: ");
				newfirstName = sc.nextLine();
				System.out.println("Enter updated extension: ");
				newextension = sc.nextLine();
				System.out.println("Enter updated email: ");
				newemail = sc.nextLine();
				System.out.println("Enter updated office code: ");
				newofficeCode = sc.nextLine();
				System.out.println("Enter updated Employee Number of Superior:");
				newreportsTo = sc.nextLine();
				System.out.println("Enter updated Job Title: ");
				newjobTitle = sc.nextLine();
	
			
				updateEmployee();
				break;
			}
		}
		case 3: {
			System.out.println("Enter Employee Number to retrieve :");
			employeeNumber = sc.nextInt();
			viewEmployee();
			break;
		}
		case 4: {
					
			System.out.println("Enter Employee Number to delete :");
			employeeNumber = sc.nextInt();
			sc.nextLine();
			viewEmployee();
			
			if (norecord) {
				System.out.println("No record to delete.");
				break;
			} else {
				deleteEmployee();
				break;
			}
		}
		case 5: {
			System.out.println("Exiting Student Function selected");
			System.out.println("Function terminated");
			return choice;
			
		}
		default: {
			System.out.println("Invalid choice. Please enter a number between 1 and 5.");
			}		
		}
		return choice;
	}


public static void main(String[] args) {
	employees e = new employees();
	while(e.function() != 5) {
	}
}
};