import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SmartLibrary myLibrary = new SmartLibrary();

    /**
     * Prints a consistent visual border to organize the console output.
     */
    public static void printSeparator() {
        System.out.println(Colors.PURPLE + "====================================================================================================" + Colors.RESET);
    }

    /**
     * Prints the table header for the library shelf view.
     */
    public static void printShelfHeader() {
        System.out.println(Colors.CYAN + String.format("%-40s | %-15s | %-20s | %s", 
            "Title", "ISBN", "Author", "Status") + Colors.RESET);
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    public static void main(String[] args) {
        // Initialize the system with 20 sample books
        DataInitializer.loadSampleData(myLibrary);

        while (true) {
            System.out.println("\n" + Colors.CYAN + "=== SMART LIBRARY SYSTEM ===" + Colors.RESET);
            System.out.println(Colors.YELLOW + "1. View Shelf (BST In-Order)");
            System.out.println("2. Search/Borrow Book (BST/Linear)");
            System.out.println("3. Return Last Book (Stack Pop)");
            System.out.println("4. View History (Stack View)");
            System.out.println("5. Exit" + Colors.RESET);
            System.out.print("Choice: ");

            try {
                // Read user menu choice
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1: 
                        printSeparator();
                        printShelfHeader();
                        myLibrary.showShelf(); 
                        printSeparator();
                        break;

                    case 2:
                        String query = "";
                        while (true) {
                            System.out.print("\nEnter ISBN/Title to borrow (or type 'done' to finish): ");
                            query = scanner.nextLine().trim();
                            
                            if (query.equalsIgnoreCase("done")) {
                                break;
                            }
                            
                            if (!query.isEmpty()) {
                                myLibrary.borrowBook(query);
                            }
                        }
                        break;

                    case 3: 
                        printSeparator();
                        myLibrary.returnLastBook(); 
                        printSeparator();
                        break;

                    case 4:
                        printSeparator();
                        myLibrary.showHistory();
                        printSeparator();
                        break;

                    case 5: 
                        printSeparator();
                        System.out.println(Colors.PURPLE + "Closing system... Goodbye!" + Colors.RESET);
                        printSeparator();
                        System.exit(0);

                    default: 
                        System.out.println(Colors.RED + "Invalid option. Please choose 1-5." + Colors.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(Colors.RED + "Error: Please enter a numeric choice (1-5)." + Colors.RESET);
            } catch (Exception e) {
                System.out.println(Colors.RED + "An unexpected error occurred: " + e.getMessage() + Colors.RESET);
            }
        }
    }
}