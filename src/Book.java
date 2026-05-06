/**
 * Represents a book in the Smart Library System.
 * Implements Comparable to allow the BST to organize books by ISBN.
 */
public class Book implements Comparable<Book> {
    private String isbn;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isAvailable = true; // New books default to available
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public int compareTo(Book other) {
        // Essential for BST insertion and searching logic
        return this.isbn.compareTo(other.isbn);
    }

    @Override
    public String toString() {
        // Formats the output for the console table view
        return String.format("%-40s | %-15s | %-20s | [%s]", 
            title, isbn, author, 
            isAvailable ? Colors.GREEN + "Available" + Colors.RESET 
                        : Colors.RED + "Borrowed" + Colors.RESET);
    }
}