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

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(HISTORY_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    permanentLog.add(line);
                    
                    // If a line is marked "Borrowed", find the book in BST and push it to the stack
                    if (line.contains("Borrowed:")) {
                        String title = line.substring(line.lastIndexOf(":") + 1).trim();
                        Book book = database.searchByTitle(title);
                        if (book != null && !book.isAvailable()) {
                            currentBorrowed.push(book);
                        }
                    } 
                    // If "Returned", remove the top book from the stack
                    else if (line.contains("Returned:")) {
                        if (!currentBorrowed.isEmpty()) currentBorrowed.pop();
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading history logs.");
            }
        }
    }

    /**
     * Adds a book to the stack and records a timestamped borrow event.
     */
    public void push(Book book) {
        currentBorrowed.push(book);
        String logEntry = "Borrowed: " + book.getTitle();
        permanentLog.add(logEntry);
        saveLogToFile(logEntry);
    }

    /**
     * Removes the last borrowed book from the stack (LIFO) and records a return.
     */
    public Book pop() {
        if (currentBorrowed.isEmpty()) return null;
        Book book = currentBorrowed.pop();
        String logEntry = "Returned: " + book.getTitle();
        permanentLog.add(logEntry);
        saveLogToFile(logEntry);
        return book;
    }

    /**
     * Appends a new log entry to the history CSV with a real-time timestamp.
     */
    private void saveLogToFile(String logEntry) {
        String timestamp = java.time.LocalDateTime.now()
                           .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(HISTORY_FILE, true)))) {
            out.println("[" + timestamp + "] " + logEntry);
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