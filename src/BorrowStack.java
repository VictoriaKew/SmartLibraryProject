import java.util.Stack;
import java.io.*;
import java.util.ArrayList;

public class BorrowStack {
    private Stack<Book> currentBorrowed = new Stack<>(); // Tracks current session's LIFO order
    private ArrayList<String> permanentLog = new ArrayList<>(); // Stores historical text for the ledger
    private static final String HISTORY_FILE = "transaction_history.csv";

    /**
     * Rebuilds the session Stack and the permanent log from the CSV file.
     * This ensures the system "remembers" the LIFO order after a restart.
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
        java.util.List<Book> allBooks = new java.util.ArrayList<>();
        database.getAllBooks(allBooks); 

        for (Book book : allBooks) {
            if (!book.isAvailable()) {
                // Push it to the stack so Choice 4 can "pop" it to return it
                currentBorrowed.push(book);
            }
        }
    }

    /**
     * Adds a book to the stack and records a timestamped borrow event.
     */
    public void push(Book book) {
    currentBorrowed.push(book);
    // Format the log entry with the timestamp IMMEDIATELY
    String logEntry = "[" + generateTimestamp() + "] Borrowed: " + book.getTitle();
    permanentLog.add(logEntry); 
    
    // Pass the already formatted entry to the file saver
    saveLogToFileDirect(logEntry);
}

    // Helper method to get the current timestamp string
    private String generateTimestamp() {
        return java.time.LocalDateTime.now()
             .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    /**
     * Removes the last borrowed book from the stack (LIFO) and records a return.
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
     * Appends a new log entry to the history CSV with a real-time timestamp.
     */
    private void saveLogToFileDirect(String fullLogLine) {
    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(HISTORY_FILE, true)))) {
        out.println(fullLogLine);
    } catch (IOException e) {
        System.out.println("Error saving transaction.");
    }
}

    /**
     * Prints the transaction ledger with color-coded symbols (+/-).
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