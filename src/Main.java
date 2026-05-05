import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SmartLibrary myLibrary = new SmartLibrary();

    public static void printWelcomeBanner() {
        printSeparator();
        // Updated ASCII to say SMART
        System.out.println(Colors.CYAN + "         _______  __   __  _______  ______    _______ " + Colors.RESET);
        System.out.println(Colors.CYAN + "        |       ||  |_|  ||   _   ||    _ |  |       |" + Colors.RESET);
        System.out.println(Colors.CYAN + "        |  _____||       ||  |_|  ||   | ||  |_     _|" + Colors.RESET);
        System.out.println(Colors.CYAN + "        | |_____ |       ||       ||   |_||_   |   |  " + Colors.RESET);
        System.out.println(Colors.CYAN + "        |_____  ||       ||       ||    __  |  |   |  " + Colors.RESET);
        System.out.println(Colors.CYAN + "         _____| || ||_|| ||   _   ||   |  | |  |   |  " + Colors.RESET);
        System.out.println(Colors.CYAN + "        |_______||_|   |_||__| |__||___|  |_|  |___|  " + Colors.RESET);
        System.out.println(Colors.YELLOW + "                L I B R A R Y   S Y S T E M          " + Colors.RESET);
        System.out.println("\n" + Colors.GREEN + "       Welcome! Smart Library System initialized and ready.  " + Colors.RESET);
        printSeparator();
    }

    public static void printSeparator() {
        System.out.println(Colors.PURPLE + "====================================================================================================" + Colors.RESET);
    }
    
    public static void printShelfHeader() {
        System.out.println(Colors.CYAN + String.format("%-40s | %-15s | %-20s | %s", "Title", "ISBN", "Author", "Status") + Colors.RESET);
        System.out.println("----------------------------------------------------------------------------------------------------");
    }

    public static void main(String[] args) {
        DataInitializer.loadLibraryData(myLibrary);
        myLibrary.initializeHistory();
        printWelcomeBanner();

        while (true) {
            System.out.println("\n" + Colors.CYAN + "=== SMART LIBRARY SYSTEM MANAGEMENT ===" + Colors.RESET);
            System.out.println(Colors.YELLOW + "1. Add New Book\n2. Search Book\n3. Borrow Book\n4. Return Book\n5. Check History\n6. Exit" + Colors.RESET);
            System.out.print("Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("\n--- Register New Book ---");
                        System.out.print("ISBN: "); String isbn = scanner.nextLine();
                        System.out.print("Title: "); String title = scanner.nextLine();
                        System.out.print("Author: "); String author = scanner.nextLine();
                        
                        Book nb = new Book(isbn, title, author);
                        myLibrary.addBook(nb);
                        DataInitializer.saveBookToCSV(nb);
                        System.out.println(Colors.GREEN + "Successfully saved to Smart Library System." + Colors.RESET);
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

                            if (borrowQuery.equalsIgnoreCase("done")) {
                                break; 
                            }

                            if (!borrowQuery.isEmpty()) {
                                printSeparator();
                                myLibrary.borrowBook(borrowQuery);
                                printSeparator();
                            }
                        }
                        break;

                    case 4:
                        printSeparator();
                        myLibrary.returnLastBook();
                        printSeparator();
                        break;

                    case 5:
                        printSeparator();
                        myLibrary.showHistory();
                        printSeparator();
                        break;

                    case 6:
                        printSeparator();
                        System.out.println(Colors.PURPLE + "Closing Smart Library System... Goodbye!" + Colors.RESET);
                        printSeparator();
                        System.exit(0);

                    default:
                        System.out.println(Colors.RED + "Invalid choice. Select 1-6." + Colors.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
            }
        }
    }
}