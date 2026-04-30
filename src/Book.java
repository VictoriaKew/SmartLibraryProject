public class Book implements Comparable<Book> {
    private String isbn;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    // Getters and Setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public int compareTo(Book other) {
        return this.isbn.compareTo(other.isbn);
    }

    @Override
    public String toString() {
        return String.format("%-40s | %-15s | %-20s | [%s]", 
            title, isbn, author, 
            isAvailable ? Colors.GREEN + "Available" + Colors.RESET 
                        : Colors.RED + "Borrowed" + Colors.RESET);
    }
}