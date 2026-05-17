import java.util.Scanner;

/**
 * Main application entry point running the console menu interface loops.
 * Manages user interactions, inputs, and text displays, and routes actions to the system controller.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SmartLibrary myLibrary = new SmartLibrary();

    /**
     * Outputs a stylized ASCII art welcome banner to the terminal layout.
     * Combines custom graphics text rows with ANSI colors to display system status messages.
     */
    public static void printWelcomeBanner() {
        printSeparator();
        System.out.println(Colors.CYAN + " _______  __   __  _______  ______    _______ " + Colors.RESET);
        System.out.println(Colors.CYAN + "|       ||  | |  ||       ||    _ |  |       |" + Colors.RESET);
        System.out.println(Colors.CYAN + "|  _____||  |_|  ||   _   ||   | ||  |__   __|" + Colors.RESET);
        System.out.println(Colors.CYAN + "| |_____ |       ||  |_|  ||   |_||_    | |   " + Colors.RESET);
        System.out.println(Colors.CYAN + "|_____  ||       ||       ||    __  |   | |   " + Colors.RESET);
        System.out.println(Colors.CYAN + " _____| ||   _   ||   _   ||   |  | |   | |   " + Colors.RESET);
        System.out.println(Colors.CYAN + "|_______||__| |__||__| |__||___|  |_|   |_|   " + Colors.RESET);
        System.out.println(Colors.YELLOW + "         L I B R A R Y   S Y S T E M          " + Colors.RESET);
        System.out.println("\n" + Colors.GREEN + "Welcome! Smart Library System initialized and ready.  " + Colors.RESET);
        printSeparator();
    }

    /**
     * Outputs a uniform, colored separator line across the console layout.
     * Helps separate different menus and options visually.
     */
    public static void printSeparator() {
        System.out.println(Colors.PURPLE + "====================================================================================================" + Colors.RESET);
    }
    
    /**
     * Formats and prints matching column header titles for data tables.
     * Ensures headers stay aligned with the layout defined in the book's toString() method.
     */
    public static void printShelfHeader() {
        System.out.println(Colors.CYAN + String.format("%-40s | %-15s | %-20s | %s", "Title", "ISBN", "Author", "Status") + Colors.RESET);
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    /**
     * The main execution loop of the application.
     * Loads saved files, restores logs, and runs an infinite loop prompting users
     * to pick a role (Librarian, Student, or Shut Down) and routing inputs accordingly.
     * 
     * @param args Standard command-line configuration arguments string array
     */
    public static void main(String[] args) {
        DataInitializer.loadLibraryData(myLibrary);
        myLibrary.initializeHistory();
        printWelcomeBanner();

        while (true) {
            System.out.println("\n" + Colors.CYAN + "=== LOGIN: SELECT YOUR ROLE ===" + Colors.RESET);
            System.out.println("1. Librarian (Admin)");
            System.out.println("2. Student (User)");
            System.out.println("3. Shut Down System");
            System.out.print("Choice: ");

            try {
                int roleChoice = Integer.parseInt(scanner.nextLine());
                
                if (roleChoice == 1) {
                    runAdminMenu();
                } else if (roleChoice == 2) {
                    runStudentMenu();
                } else if (roleChoice == 3) {
                    printSeparator();
                    System.out.println(Colors.PURPLE + "Closing Smart Library System... Goodbye!" + Colors.RESET);
                    printSeparator();
                    System.exit(0);
                } else {
                    System.out.println(Colors.RED + "Invalid choice. Select 1, 2, or 3." + Colors.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
            }
        }
    }

    /**
     * Displays options and routes actions for users logged in as a Librarian.
     * Offers admin options to add books, view the full inventory, or check the operational ledger.
     */
    private static void runAdminMenu() {
        while (true) {
            System.out.println("\n" + Colors.CYAN + "--- [ LIBRARIAN DASHBOARD ] ---" + Colors.RESET);
            System.out.println(Colors.YELLOW + "1. Add New Book\n2. View Full Inventory\n3. Check Transaction History\n4. Logout to Main Menu" + Colors.RESET);
            System.out.print("Admin Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("\n--- Register New Book ---");
                        System.out.print("ISBN: "); String isbn = scanner.nextLine().trim();
                        while(!isbn.matches("\\d{13}")){
                            System.out.println(Colors.RED + "Invalid ISBN. Must be 13 numbers. Please try again." + Colors.RESET);
                            System.out.print("ISBN: "); 
                            isbn = scanner.nextLine().trim();
                        }
                        System.out.print("Title: "); String title = scanner.nextLine().trim();
                        while(title.isEmpty()){
                            System.out.println(Colors.RED + "Title cannot be empty. Please try again." + Colors.RESET);
                            System.out.print("Title: "); 
                            title = scanner.nextLine().trim();
                        }
                        System.out.print("Author: "); String author = scanner.nextLine().trim();
                        while(author.isEmpty()){
                            System.out.println(Colors.RED + "Author cannot be empty. Please try again." + Colors.RESET);
                            System.out.print("Author: "); 
                            author = scanner.nextLine().trim();
                        }
                        
                        Book nb = new Book(isbn, title, author);
                        myLibrary.addBook(nb);
                        DataInitializer.saveBookToCSV(nb);
                        System.out.println(Colors.GREEN + "Successfully saved to Smart Library System." + Colors.RESET);
                        break;
                    case 2:
                        printSeparator();
                        printShelfHeader();
                        myLibrary.showShelf();
                        printSeparator();
                        break;
                    case 3:
                        printSeparator();
                        myLibrary.showHistory();
                        printSeparator();
                        break;
                    case 4:
                        System.out.println(Colors.YELLOW + "Logging out..." + Colors.RESET);
                        return; // Exits back to the role selection loop
                    default:
                        System.out.println(Colors.RED + "Invalid choice. Select 1-4." + Colors.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
            }
        }
    }

    /**
     * Displays options and routes actions for users logged in as a Student.
     * Allows students to browse the inventory, search for books, borrow items, 
     * or return their last borrowed book. The borrow option loops continuously, 
     * letting students check out multiple items until they type 'done'.
     */
    private static void runStudentMenu() {
        while (true) {
            System.out.println("\n" + Colors.CYAN + "--- [ STUDENT TERMINAL ] ---" + Colors.RESET);
            System.out.println(Colors.YELLOW + "1. View Full Inventory\n2. Search Book\n3. Borrow Book\n4. View Borrow History\n5. Return Last Borrowed Book\n6. Logout to Main Menu" + Colors.RESET);
            System.out.print("Student Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        printSeparator();
                        printShelfHeader();
                        myLibrary.showShelf(); // Safely displays the BST without allowing edits
                        printSeparator();
                        break;
                    case 2:
                        System.out.print("Search (Title/ISBN): ");
                        String query = scanner.nextLine();
                        printSeparator();
                        printShelfHeader();
                        myLibrary.searchOnly(query);
                        printSeparator();
                        break;
                    case 3: 
                        while (true) {
                            System.out.print("\nEnter ISBN/Title to borrow (or type 'done' to finish): ");
                            String borrowQuery = scanner.nextLine().trim();
                            if (borrowQuery.equalsIgnoreCase("done")) break; 
                            
                            if (!borrowQuery.isEmpty()) {
                                printSeparator();
                                myLibrary.borrowBook(borrowQuery);
                                printSeparator();
                            }
                        }
                        break;
                    case 4:
                        printSeparator();
                        myLibrary.showBorrowHistory();
                        printSeparator();
                        break;
                    case 5:
                        printSeparator();
                        myLibrary.returnLastBook();
                        printSeparator();
                        break;
                    case 6:
                        System.out.println(Colors.YELLOW + "Logging out..." + Colors.RESET);
                        return; // Exits back to the role selection loop
                    default:
                        System.out.println(Colors.RED + "Invalid choice. Select 1-5." + Colors.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
            }
        }
    }
}