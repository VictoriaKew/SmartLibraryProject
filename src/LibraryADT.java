import java.util.*;

public interface LibraryADT {
    void addBook(int isbn, String title, String author); 
    void borrowBook(int isbn);                          
    void viewLatestHistory();                            
    void searchBook(int isbn);
    void returnBook();                          
}