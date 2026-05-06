import java.util.Scanner;
import java.util.List;

/**
 * The core controller for the Smart Library System.
 * Coordinates between the BST database, the session Stack, and CSV persistence.
 */
public class SmartLibrary implements LibraryADT {
    private BookBST bookDatabase = new BookBST();
    private BorrowStack userHistory = new BorrowStack();

    // Provides access to the BST for initialization and syncing
    public BookBST getDatabase() { return bookDatabase; }

    @Override
    public void addBook(Book book) {
        bookDatabase.insert(book);
    }

    /**
     * Rebuilds the session history and LIFO stack from the transaction ledger.
     */
    public void initializeHistory() {
        userHistory.loadHistoryFromFile(bookDatabase);
    }

    /**
     * Validates input to prevent symbols from breaking the search logic.
     */
    public boolean isEnglishOnly(String input) {
        return input.matches("^[a-zA-Z\\s]+$") || input.matches("^[0-9]+$");
    }

    /**
     * Option 2: Performs a non-destructive search and displays all matches.
     */
    public void searchOnly(String query) {
    if (!isEnglishOnly(query)) {
        System.out.println(Colors.RED + "Error: Invalid characters. Symbols are not allowed." + Colors.RESET);
        return;
    }

    // Attempt to find matches in the BST
    List<Book> matches = bookDatabase.getMatches(query.toLowerCase());
    
        if (matches.isEmpty()) {
            // 1. Alert the user that the specific search failed
            System.out.println(Colors.RED + "No book found for '" + query + "'." + Colors.RESET);
        
            // 2. Automatically show the full inventory as a fallback
            System.out.println(Colors.CYAN + "\n--- [ CURRENT LIBRARY INVENTORY ] ---" + Colors.RESET);
            System.out.println(String.format("%-40s | %-15s | %-20s | %-10s", "Title", "ISBN", "Author", "Status"));
            System.out.println("--------------------------------------------------------------------------------------------------");
        
            // This calls your existing displayAll() method which uses In-Order Traversal
            bookDatabase.displayAll(); 
        
            System.out.println("--------------------------------------------------------------------------------------------------");
        } else {
            // Display only the matches if found
            System.out.println(Colors.CYAN + "Matches found:" + Colors.RESET);
            for (Book b : matches) System.out.println(b);
        }
    }
    
    /**
     * Option 3: Finds a book and updates its status to 'Borrowed'.
     * Synchronizes changes to library_data.csv and transaction_history.csv.
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

        // Selection logic: Auto-selects if unique, otherwise requires ISBN confirmation
        if (matches.size() == 1) {
            selectedBook = matches.get(0);
        } else {
            System.out.println(Colors.YELLOW + "Multiple books found:" + Colors.RESET);
            for (Book b : matches) System.out.println(b);
        
            System.out.print("Please enter the EXACT Title or ISBN to confirm: ");
            Scanner sc = new Scanner(System.in);
            String confirmation = sc.nextLine().trim();
            
            for (Book b : matches) {
                if (b.getIsbn().equals(confirmation) || b.getTitle().equalsIgnoreCase(confirmation)) {
                    selectedBook = b;
                    break;
                }
            }
        }

        // Final validation and synchronization
        if (selectedBook == null) {
            System.out.println(Colors.RED + "Error: Confirmation did not match any results." + Colors.RESET);
        } else if (!selectedBook.isAvailable()) {
            System.out.println(Colors.RED + "Error: '" + selectedBook.getTitle() + "' is already borrowed." + Colors.RESET);
        } else {
            selectedBook.setAvailable(false);
            userHistory.push(selectedBook); // Updates Stack and transaction_history.csv
            DataInitializer.syncDatabase(this); // Updates library_data.csv
            System.out.println(Colors.GREEN + "Success! Borrowed: " + selectedBook.getTitle() + Colors.RESET);
        }
    }

    /**
     * Option 4: Returns the most recently borrowed book using LIFO (Stack) logic.
     */
    @Override
    public void returnLastBook() {
        Book last = userHistory.pop(); 
    
        if (last != null) {
            last.setAvailable(true); 
            DataInitializer.syncDatabase(this); // Save status change to file
            System.out.println(Colors.GREEN + "Returned (LIFO): " + last.getTitle() + Colors.RESET);
        } else {
            System.out.println(Colors.RED + "No books in the stack to return." + Colors.RESET);
        }
    }

    public void showShelf() { bookDatabase.displayAll(); }
    public void showHistory() { userHistory.displayHistory(); }
}