public class Book implements Comparable<Book> {
    private String isbn;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String isbn, String title, String author) {
        if (isbn == null || !isbn.trim().matches("^\\d{13}$")) {
            throw new IllegalArgumentException("ISBN must be exactly 13 digits and contain numbers only.");
        }

        if (title == null || title.trim().isEmpty() || !title.matches(".*[a-zA-Z0-9].*")) {
            throw new IllegalArgumentException("Title cannot be blank or contain only symbols.");
        }

        if (author == null || author.trim().isEmpty() || !author.trim().matches("^[a-zA-Z\\s.-]+$")) {
            throw new IllegalArgumentException("Author name cannot be blank, contain symbols, or numbers. Only letters, spaces, dots, and hyphens are allowed.");
        }

        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.isAvailable = true;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public int compareTo(Book other) {
        return this.isbn.compareTo(other.isbn);
    }

    @Override
    public String toString() {
        String status = isAvailable ? Colors.GREEN + "Available" + Colors.RESET 
                                    : Colors.RED + "Borrowed" + Colors.RESET;

        String displayTitle = title;
        if (title.length() > 40) {
            displayTitle = title.substring(0, 37) + "...";
        }

        String displayAuthor = author;
        if (author.length() > 20) {
            displayAuthor = author.substring(0, 17) + "...";
        }

        return String.format("%-40s | %-15s | %-20s | [%s]", 
            displayTitle, isbn, displayAuthor, status);
    }
}