/**
 * Represents a book in the Smart Library System.
 * Implements Comparable to allow the BST to automatically organize books by ISBN.
 */
public class Book implements Comparable<Book> {
    private String isbn;
    private String title;
    private String author;
    private boolean isAvailable;

    /**
     * Constructs a new Book instance with provided metadata.
     * New books default to an available state.
     * 
     * @param isbn   The unique International Standard Book Number
     * @param title  The full title text of the book
     * @param author The name of the book's author
     */
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isAvailable = true; // New books default to available
    }

    /**
     * Retrieves the book's ISBN.
     * 
     * @return String representing the unique ISBN identifier.
     */
    public String getIsbn() { return isbn; }

    /**
     * Retrieves the book's title.
     * 
     * @return String representing the title of the book.
     */
    public String getTitle() { return title; }

    /**
     * Retrieves the book's author.
     * 
     * @return String representing the author's name.
     */
    public String getAuthor() { return author; }

    /**
     * Evaluates whether the book is currently available on the shelf.
     * 
     * @return true if the book can be borrowed; false if it is currently out.
     */
    public boolean isAvailable() { return isAvailable; }

    /**
     * Updates the borrowing availability status of the book.
     * Called when a book undergoes a borrow (false) or return (true) operation.
     * 
     * @param available The new availability status of the book
     */
    public void setAvailable(boolean available) { isAvailable = available; }

    /**
     * Comports with the Comparable contract to establish a natural sorting order.
     * Compares this book's ISBN lexicographically against another book's ISBN.
     * Essential for routing insertion and search paths in the Binary Search Tree.
     * 
     * @param other The target Book object to compare against
     * @return A negative integer if less, zero if equal, or a positive integer if greater.
     */
    @Override
    public int compareTo(Book other) {
        // Essential for BST insertion and searching logic
        return this.isbn.compareTo(other.isbn);
    }

    /**
     * Formats the book properties into a structured table row string.
     * Pads text fields to fixed columns and applies ANSI colors dynamically 
     * to visually highlight whether the book is "Available" (Green) or "Borrowed" (Red).
     * 
     * @return A styled, fixed-width console output row string.
     */
    @Override
    public String toString() {
        // Formats the output for the console table view
        return String.format("%-40s | %-15s | %-20s | [%s]", 
            title, isbn, author, 
            isAvailable ? Colors.GREEN + "Available" + Colors.RESET 
                        : Colors.RED + "Borrowed" + Colors.RESET);
    }
}