public interface LibraryADT {
    void addBook(Book book);
    void borrowBook(String query);
    void returnLastBook();
}