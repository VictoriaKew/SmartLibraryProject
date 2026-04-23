public class Book {
    int isbn;               
    String title, author;   
    Book left, right;      

    public Book(int isbn, String title, String author) {
        this.isbn = isbn;   
        this.title = title; 
        this.author = author;
    }
}