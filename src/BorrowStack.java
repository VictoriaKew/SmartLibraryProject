import java.util.Stack;

public class BorrowStack {
    private Stack<Book> currentBorrowed = new Stack<>(); // For return logic
    private java.util.ArrayList<String> permanentLog = new java.util.ArrayList<>(); // For View History

    public void push(Book book) {
        currentBorrowed.push(book);
        permanentLog.add("Borrowed: " + book.getTitle());
    }

    public Book pop() {
        if (currentBorrowed.isEmpty()) return null;
        Book book = currentBorrowed.pop();
        permanentLog.add("Returned: " + book.getTitle());
        return book;
    }

    public void displayHistory() {
        if (permanentLog.isEmpty()) {
            System.out.println(Colors.YELLOW + "No activity recorded yet." + Colors.RESET);
             return;
        }
        System.out.println(Colors.CYAN + "--- [ OFFICIAL TRANSACTION LEDGER ] ---" + Colors.RESET);
        for (String log : permanentLog) {
            if (log.startsWith("Borrowed")) {
                System.out.println(Colors.GREEN + " [+] " + log + Colors.RESET);
            } else {
                System.out.println(Colors.YELLOW + " [-] " + log + Colors.RESET);
            }
        }
    }
}