public class BookBST{
    private Book ISBNRoot;
    private Book titleRoot;

    public void insert(int isbn, String t, String a){
        ISBNRoot = ins(ISBNRoot, isbn, t, a);
        titleRoot = 
    }

    private Book ins(Book r, int i, String t, String a){
        if(r == null) 
            return new Book(i, t, a);
        if(i < r.isbn){
            r.left = ins(r.left, i, t, a);
        }
        else if(i > r.isbn){
            r.right = ins(r.right, i, t, a);
        }
        return r;
    }

    public Book searchByIsbn(int i){
        return sea(ISBNRoot, i);
    }

    private Book sea(Book r, int i){
        if(r == null || r.isbn == i)
            return r;
        return(i < r.isbn) ? sea(r.left, i) : sea(r.right, i);
    }

    public Book searchByTitle(String t){
        return null; //ae
    }
}