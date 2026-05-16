/**
 * Abstract Data Type defining the core functional requirements for the library system.
 * Serves as an architectural contract that any library system controller must implement.
 */
public interface LibraryADT {
    
    /**
     * Inserts a new book instance into the library system storage database.
     * 
     * @param book The Book object entity to save
     */
    void addBook(Book book);
    
    /**
     * Searches for a book matching the query string and marks it as borrowed.
     * 
     * @param query An ISBN or Title keyword search string
     */
    void borrowBook(String query);
    
    /**
     * Returns the most recently borrowed book using LIFO stack rules.
     */
    void returnLastBook();
}