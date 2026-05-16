import java.util.Stack;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the user session transaction history using a LIFO (Last-In, First-Out) tracking model.
 * Synchronizes runtime transactions with persistent ledger files.
 */
public class BorrowStack {
    private Stack<Book> currentBorrowed = new Stack<>(); // Tracks current session's LIFO order
    private ArrayList<String> permanentLog = new ArrayList<>(); // Stores historical text for the ledger
    private static final String HISTORY_FILE = "transaction_history.csv";

    /**
     * Restores state tracking histories from transaction log files during system startup.
     * Loads the ledger text file into active arrays, then parses the main book tree 
     * database to rebuild the LIFO stack with any books that are still checked out.
     * 
     * @param database The system's primary BookBST database instance
     */
    public void loadHistoryFromFile(BookBST database) {
        File file = new File(HISTORY_FILE);
        permanentLog.clear();
        currentBorrowed.clear();

        // 1. Load the text logs for the ledger view
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    permanentLog.add(line);
                }
            } catch (IOException e) {
                System.out.println("Error loading history logs.");
            }
        }

        // 2. CRITICAL SYNC: Rebuild the Stack based on the actual status of books
        // We look at all books in the BST and push any that are marked 'false'
        List<Book> allBooks = new ArrayList<>();
        database.getAllBooks(allBooks); 

        for (Book book : allBooks) {
            if (!book.isAvailable()) {
                // Push it to the stack so Choice 4 can "pop" it to return it
                currentBorrowed.push(book);
            }
        }
    }

    /**
     * Places a newly borrowed book onto the top of the session stack.
     * Records a timestamped transaction log entry, updates the active ledger list,
     * and appends the new entry to the transaction history file.
     * 
     * @param book The Book object currently being checked out
     */
    public void push(Book book) {
        currentBorrowed.push(book);
        // Format the log entry with the timestamp IMMEDIATELY
        String logEntry = "[" + generateTimestamp() + "] Borrowed: " + book.getTitle();
        permanentLog.add(logEntry); 
        
        // Pass the already formatted entry to the file saver
        saveLogToFileDirect(logEntry);
    }

    /**
     * Internal utility method that generates a standard, uniform timestamp string.
     * Uses the local system clock to format the current date and time.
     * 
     * @return String formatted as "yyyy-MM-dd HH:mm:ss"
     */
    private String generateTimestamp() {
        return java.time.LocalDateTime.now()
             .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Pops the most recently borrowed book off the session stack to process a return (LIFO).
     * Generates a corresponding return entry, updates the transaction ledger, 
     * and logs the event to the history file.
     * 
     * @return The Book popped from the top of the stack, or null if no books are currently borrowed.
     */
    public Book pop() {
        if (currentBorrowed.isEmpty()) return null;
        Book book = currentBorrowed.pop();
        
        // Format the log entry with the timestamp IMMEDIATELY
        String logEntry = "[" + generateTimestamp() + "] Returned: " + book.getTitle();
        permanentLog.add(logEntry);
        
        saveLogToFileDirect(logEntry);
        return book;
    }

    /**
     * Appends a new transaction log line directly to the end of the history CSV file.
     * Opens file writers in append mode to log transactions in real time.
     * 
     * @param fullLogLine The completely formatted transaction log message string
     */
    private void saveLogToFileDirect(String fullLogLine) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(HISTORY_FILE, true)))) {
            out.println(fullLogLine);
        } catch (IOException e) {
            System.out.println("Error saving transaction.");
        }
    }

    /**
     * Prints the complete operational ledger histories to the user interface.
     * Parses stored records and prepends color-coded status indicators 
     * ([+] in Green for borrows; [-] in Red for returns).
     */
    public void displayHistory() {
        if (permanentLog.isEmpty()) {
            System.out.println(Colors.YELLOW + "No activity recorded yet." + Colors.RESET);
            return;
        }
        System.out.println(Colors.CYAN + "--- [ OFFICIAL TRANSACTION LEDGER ] ---" + Colors.RESET);
        for (String log : permanentLog) {
            if (log.contains("Borrowed:")) {
                System.out.println(Colors.GREEN + " [+] " + Colors.RESET + log);
            } else if (log.contains("Returned:")) {
                System.out.println(Colors.RED + " [-] " + Colors.RESET + log);
            }
        }
    }
}