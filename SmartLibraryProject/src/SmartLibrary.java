import java.util.List;
import java.util.Scanner;

/**
 * Core Controller for the Smart Library System.
 * Coordinates operations between the BST database, the transactional stack, 
 * and persistent storage syncs, implementing the LibraryADT contract rules.
 */
public class SmartLibrary implements LibraryADT {
    private BookBST bookDatabase = new BookBST();
    private BorrowStack userHistory = new BorrowStack();

    /**
     * Provides public access to the underlying Binary Search Tree container.
     * Essential for initialization, data utilities and synchronization components.
     * 
     * @return The private BookBST instance object
     */
    public BookBST getDatabase() { return bookDatabase; }

    /**
     * Adds a new book to the library system.
     * Satisfies the LibraryADT contract rule by inserting the book directly into the tree structure.
     * 
     * @param book The unique Book object instance to add
     */
    @Override
    public void addBook(Book book) {
        bookDatabase.insert(book);
    }

    /**
     * Rebuilds session history tracking data from transaction logs on startup.
     * Passes the database reference to the stack loader to link records and statuses correctly.
     */
    public void initializeHistory() {
        userHistory.loadHistoryFromFile(bookDatabase);
    }

    /**
     * Validates user inputs using regular expressions to filter out harmful characters.
     * Restricts inputs to simple alphanumeric strings, spaces, and allowed symbols to protect search operations.
     * 
     * @param input The raw user entry text string under validation
     * @return true if the string is purely alphanumeric, spaces and allowed symbols; false if other symbols are present
     */
    public boolean isEnglishOnly(String input) {
        String cleanInput = input.replace("\u00a0", " ").trim();
        return cleanInput.matches("^[a-zA-Z0-9\\s&:',.!\\?-]+$");
    }

    /**
     * Processes non-destructive search queries and prints any matching records to the console.
     * Validates input characters first. If the search returns no matches, it automatically
     * calls displayAll() to show the full library inventory as a helpful fallback.
     * 
     * @param query The title or ISBN keyword search string input by the user
     */
    public void searchOnly(String query) {
        if (!isEnglishOnly(query)) {
            System.out.println(Colors.RED + "Error: Invalid characters. Symbols are not allowed." + Colors.RESET);
            return;
        }

       
        List<Book> matches = bookDatabase.getMatches(query.toLowerCase());
        
        if (matches.isEmpty()) {
        
            System.out.println(Colors.RED + "No book found for '" + query + "'." + Colors.RESET);
        
            System.out.println(Colors.CYAN + "\n--- [ CURRENT LIBRARY INVENTORY ] ---" + Colors.RESET);
            System.out.println(String.format("%-40s | %-15s | %-20s | %-10s", "Title", "ISBN", "Author", "Status"));
            System.out.println("--------------------------------------------------------------------------------------------------");
        
            bookDatabase.displayAll(); 
        
            System.out.println("--------------------------------------------------------------------------------------------------");
        } else {
            
            System.out.println(Colors.CYAN + "Matches found:" + Colors.RESET);
            for (Book b : matches) System.out.println(b);
        }
    }
    
    /**
     * Coordinates the checkout process to borrow a book from the library.
     * Finds matches in the tree; if a single match is found, it proceeds automatically,
     * whereas multiple matches prompt the user to type the exact ISBN or Title to confirm.
     * Updates the book's status, pushes it onto the stack, and syncs changes to the file system.
     * 
     * @param query The title or ISBN lookup keyword string entered by the student
     */
    @Override
    public void borrowBook(String query) {
        if (!isEnglishOnly(query)) {
            System.out.println(Colors.RED + "Error: Invalid characters." + Colors.RESET);
            return;
        }

        List<Book> matches = bookDatabase.getMatches(query.toLowerCase());

        if (matches.isEmpty()) {
            System.out.println(Colors.RED + "Error: No book found matching that query." + Colors.RESET);
            return;
        }

        Book selectedBook = null;
        Scanner sc = new Scanner(System.in);

        if (matches.size() == 1) {
            selectedBook = matches.get(0);
            System.out.println(Colors.YELLOW + "Match found:" + Colors.RESET);
            System.out.println(selectedBook);
        } else {
            System.out.println(Colors.YELLOW + "Multiple books found:" + Colors.RESET);
            for (int i = 0; i < matches.size(); i++) {
                System.out.println("[" + (i + 1) + "] " + matches.get(i));
            }
            System.out.print("Please enter the number of desired book (eg. 1), or 0 to cancel: ");
            String confirmation = sc.nextLine().trim();
            
        try {
                int choice = Integer.parseInt(confirmation);
                if (choice == 0) {
                    System.out.println(Colors.YELLOW + "Action canceled." + Colors.RESET);
                    return;
                }
                if (choice > 0 && choice <= matches.size()) {
                    selectedBook = matches.get(choice - 1); 
                }
            } catch (NumberFormatException e) {}
        }

        // Final validation and synchronization
        if (selectedBook == null) {
            System.out.println(Colors.RED + "Error: Invalid selection." + Colors.RESET);
            return;
        }
        if (!selectedBook.isAvailable()) {
            System.out.println(Colors.RED + "Error: '" + selectedBook.getTitle() + "' is already borrowed." + Colors.RESET);
            return;
        }
        while (true) {
            System.out.print("\nBorrow '" + selectedBook.getTitle() + "'? (y/n): ");
            String validation = sc.nextLine().trim().toLowerCase();

            if (validation.equals("y") || validation.equals("yes")) {
                selectedBook.setAvailable(false);
                userHistory.push(selectedBook); 
                DataInitializer.syncDatabase(this);
                System.out.println(Colors.GREEN + "Success! Borrowed: " + selectedBook.getTitle() + Colors.RESET);
                break;
            } else if (validation.equals("n") || validation.equals("no")) {
                System.out.println(Colors.YELLOW + "Cancelled. Book was not borrowed." + Colors.RESET);
                break;
            } else {
                System.out.println(Colors.RED + "Error: Invalid input. Please type 'y' or 'n'." + Colors.RESET);
            }
        }
    }

    /**
     * Returns the most recently checked-out book using LIFO stack rules.
     * Pops the book from the session history stack, changes its availability status 
     * back to true, and saves the updated state to the data file.
     */
    @Override
    public void returnLastBook() {
        Book last = userHistory.pop(); 
    
        if (last != null) {
            last.setAvailable(true); 
            DataInitializer.syncDatabase(this); 
            System.out.println(Colors.GREEN + "Returned (LIFO): " + last.getTitle() + Colors.RESET);
        } else {
            System.out.println(Colors.RED + "No books in the stack to return." + Colors.RESET);
        }
    }

    /**
     * Simple wrapper method that outputs the complete library shelf inventory.
     * Tells the underlying tree database to display its data using sorted In-Order Traversal.
     */
    public void showShelf() { bookDatabase.displayAll(); }

    /**
     * Simple wrapper method that prints the complete history log ledger.
     * Tells the user history container to print out all transaction entries.
     */
    public void showHistory() { userHistory.displayHistory(); }

    /**
     * Simple wrapper method that prints the list of borrowed books.
     * Tells the user history container to print out all borrowed books.
     */
    public void showBorrowHistory() { userHistory.displayBorrowHistory(); }
}