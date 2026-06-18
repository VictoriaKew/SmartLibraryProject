import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SmartLibrary myLibrary = new SmartLibrary();

    public static void printWelcomeBanner() {
        printSeparator();
        System.out.println(Colors.CYAN + "        _______  __   __  _______  ______    _______ " + Colors.RESET);
        System.out.println(Colors.CYAN + "       |       || |_|   ||       ||    _ |  |       |" + Colors.RESET);
        System.out.println(Colors.CYAN + "       |  _____||       ||   _   ||   | ||  |_     _|" + Colors.RESET);
        System.out.println(Colors.CYAN + "       | |_____ |       ||  |_|  ||   |_||_   |   |  " + Colors.RESET);
        System.out.println(Colors.CYAN + "       |_____  ||       ||       ||    __  |  |   |  " + Colors.RESET);
        System.out.println(Colors.CYAN + "        _____| || ||_|| ||   _   ||   |  | |  |   |  " + Colors.RESET);
        System.out.println(Colors.CYAN + "       |_______||_|   |_||__| |__||___|  |_|  |___|  " + Colors.RESET);
        System.out.println(Colors.YELLOW + "                L I B R A R Y   S Y S T E M          " + Colors.RESET);
        System.out.println("\n" + Colors.GREEN + "        Welcome! Smart Library System initialized and ready.  " + Colors.RESET);
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
                        
                        String isbn = "";
                        while (true) {
                            System.out.print("ISBN: ");
                            isbn = scanner.nextLine().trim();
                            if (isbn.matches("^\\d{13}$")) {
                                break;
                            }
                            System.out.println(Colors.RED + "[Validation Error] ISBN must be exactly 13 digits and contain numbers only.\n" + Colors.RESET);
                        }

                        String title = "";
                        while (true) {
                            System.out.print("Title: ");
                            title = scanner.nextLine().trim();
                            if (!title.isEmpty() && title.matches(".*[a-zA-Z0-9].*")) {
                                break;
                            }
                            System.out.println(Colors.RED + "[Validation Error] Title cannot be blank or contain only symbols.\n" + Colors.RESET);
                        }

                        String author = "";
                        while (true) {
                            System.out.print("Author: ");
                            author = scanner.nextLine().trim();
                            if (!author.isEmpty() && author.matches("^[a-zA-Z\\s.-]+$")) {
                                break;
                            }
                            System.out.println(Colors.RED + "[Validation Error] Author name cannot be blank, contain symbols, or numbers. Only letters, spaces, dots, and hyphens are allowed.\n" + Colors.RESET);
                        }
                        
                        
                        Book nb = new Book(isbn, title, author);
                        myLibrary.addBook(nb);
                        DataInitializer.saveBookToCSV(nb);
                        System.out.println(Colors.GREEN + "\nSuccessfully saved to Smart Library System." + Colors.RESET);
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
                        return; 
                    default:
                        System.out.println(Colors.RED + "Invalid choice. Select 1-4." + Colors.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
            }
        }
    }

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
                        myLibrary.showShelf(); 
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
                        return; 
                    default:
                        System.out.println(Colors.RED + "Invalid choice. Select 1-5." + Colors.RESET);
                }
            } catch (Exception e) {
                System.out.println(Colors.RED + "Invalid input. Please enter a number." + Colors.RESET);
            }
        }
    }
}