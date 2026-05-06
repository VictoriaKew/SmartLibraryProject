public class Book {
    int isbn;               
    String title, author;   
    Book leftISBN, rightISBN, leftTitle, rightTitle;
    //we have two trees, one in isbn and one in title, so

    public Book(int isbn, String title, String author) {
        this.isbn = isbn;   
        this.title = title; 
        this.author = author;
    }
}