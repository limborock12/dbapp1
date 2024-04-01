package DBapp;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    int choice = 0;
    Scanner sc = new Scanner(System.in);

    while (choice != 4) {
      System.out.println("=====================================================");
      System.out.println("Main Menu of the Office Management Application");
      System.out.println("[1] Records Management");
      System.out.println("[2] Transaction Management");
      System.out.println("[3] Report Generation");
      System.out.println("[4] Exit Application");
      System.out.println("=====================================================");
      System.out.println("Enter Function to Perform: ");
      choice = sc.nextInt();

      switch (choice) {
        case 1: {
          int recordChoice = 0;
          while (recordChoice != 5) {
            System.out.println("=====================================================");
            System.out.println("Records Management");
            System.out.println("[1] Product");
            System.out.println("[2] Customer");
            System.out.println("[3] Employee");
            System.out.println("[4] Office");
            System.out.println("[5] Exit");
            System.out.println("=====================================================");
            System.out.println("Enter Function to Perform: ");
            recordChoice = sc.nextInt();

            switch (recordChoice) {
              case 1:
                product p = new product();
		            while (p.process() != 5) {}
                break;
              case 2:
                // Call the method to manage customer records
                break;
              case 3:
                  employees e = new employees();
                  e.function();
                break;
              case 4:
                OfficeManagementApp office = new OfficeManagementApp();
                office.main();
                break;
              case 5:
                break;
              default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
            }
          }
          break;
        }
        case 2: {
          int transactionChoice = 0;
          while (transactionChoice != 3) {
            System.out.println("=====================================================");
            System.out.println("Transaction Management");
            System.out.println("[1] Orders");
            System.out.println("[2] Payments");
            System.out.println("[3] Exit");
            System.out.println("=====================================================");
            System.out.println("Enter Function to Perform: ");
            transactionChoice = sc.nextInt();

            switch (transactionChoice) {
              case 1:
                order o = new order();
		            while (o.process()!= 5) {}
                break;
              case 2:
                // Call the method to handle payments
                break;
              case 3:
                break;
              default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
            }
          }
          break;
        }
        case 3: {
          int reportChoice = 0;
          while (reportChoice != 3) {
            System.out.println("=====================================================");
            System.out.println("Report Generation");
            System.out.println("[1] Sales Report [Per Status]");
            System.out.println("[2] Sales Report [Per Product]");
            System.out.println("[3] Exit");
            System.out.println("=====================================================");
            System.out.println("Enter Function to Perform: ");
            reportChoice = sc.nextInt();

            switch (reportChoice) {
              case 1:
                SalesReport1 sr = new SalesReport1();
                sr.function();
                break;
              case 2:
                GenerateSalesReport gr = new GenerateSalesReport();
                gr.function();
                break;
              case 3:
                break;
              default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
            }
          }
          break;
        }
        case 4: {
          System.out.println("Exiting Application...");
          break;
        }
        default: {
          System.out.println("Invalid choice. Please enter a valid option.");
          break;
        }
      }
    }
    sc.close();
  }
}
