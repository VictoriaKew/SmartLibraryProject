class Node {
    Book data;
    Node left, right;
    public Node(Book book) { this.data = book; }
}

public class BookBST {
    private Node root;

    public void insert(Book newBook) {
        root = insertRecursive(root, newBook);
    }

    private Node insertRecursive(Node current, Book newBook) {
        if (current == null) return new Node(newBook);
        if (newBook.compareTo(current.data) < 0) current.left = insertRecursive(current.left, newBook);
        else if (newBook.compareTo(current.data) > 0) current.right = insertRecursive(current.right, newBook);
        return current;
    }

    public Book searchByIsbn(String isbn) {
        return searchIsbnRecursive(root, isbn);
    }

    private Book searchIsbnRecursive(Node current, String isbn) {
        if (current == null || current.data.getIsbn().equals(isbn)) return (current != null) ? current.data : null;
        return (isbn.compareTo(current.data.getIsbn()) < 0) 
            ? searchIsbnRecursive(current.left, isbn) : searchIsbnRecursive(current.right, isbn);
    }

    // Linear Search by Title (In-Order Traversal)
    public Book searchByTitle(String title) {
        return searchTitleRecursive(root, title.toLowerCase());
    }

    private Book searchTitleRecursive(Node current, String title) {
        if (current == null) return null;
        if (current.data.getTitle().toLowerCase().contains(title.toLowerCase())) return current.data;
    
        Book found = searchTitleRecursive(current.left, title);
        if (found != null) return found;
        return searchTitleRecursive(current.right, title);
    }

    public void displayAll() {
        displayInOrder(root);
    }

    private void displayInOrder(Node current) {
        if (current != null) {
            displayInOrder(current.left);
            System.out.println(current.data);
            displayInOrder(current.right);
        }
    }

    public void searchAndDisplayMatches(String query) {
        boolean found = searchRecursiveMatches(root, query.toLowerCase());
        if (!found) {
            System.out.println(Colors.RED + "No books match your search." + Colors.RESET);
        }
    }

    private boolean searchRecursiveMatches(Node current, String query) {
        if (current == null) return false;
        boolean leftMatches = searchRecursiveMatches(current.left, query);
        boolean currentMatches = current.data.getIsbn().contains(query) || 
            current.data.getTitle().toLowerCase().contains(query);
        if (currentMatches) {
            System.out.println(current.data);
            }
        boolean rightMatches = searchRecursiveMatches(current.right, query);
        return leftMatches || currentMatches || rightMatches;
    }
    
    public java.util.List<Book> getMatches(String query) {
        java.util.List<Book> matches = new java.util.ArrayList<>();
        findMatchesRecursive(root, query.toLowerCase(), matches);
        return matches;
    }

    private void findMatchesRecursive(Node current, String query, java.util.List<Book> matches) {
        if (current == null) return;
        findMatchesRecursive(current.left, query, matches);
    
        if (current.data.getIsbn().contains(query) || 
            current.data.getTitle().toLowerCase().contains(query)) {
            matches.add(current.data);
        }
        findMatchesRecursive(current.right, query, matches);
    }
}