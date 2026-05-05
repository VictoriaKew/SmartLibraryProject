/**
 * Node class representing a single point in the Binary Search Tree.
 */
class Node {
    Book data;
    Node left, right;
    public Node(Book book) { this.data = book; }
}

/**
 * BookBST manages the collection of books using a Binary Search Tree logic.
 */
public class BookBST {
    private Node root;

    // Standard recursive insertion based on ISBN
    public void insert(Book newBook) {
        root = insertRecursive(root, newBook);
    }

    private Node insertRecursive(Node current, Book newBook) {
        if (current == null) return new Node(newBook);
        
        // If the new ISBN is smaller, go left; if larger, go right
        if (newBook.compareTo(current.data) < 0) 
            current.left = insertRecursive(current.left, newBook);
        else if (newBook.compareTo(current.data) > 0) 
            current.right = insertRecursive(current.right, newBook);
            
        return current;
    }

    /**
     * Displays books in ascending order of ISBN using In-Order Traversal.
     */
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

    /**
     * Searches for multiple books that contain the query string in Title or ISBN.
     */
    public java.util.List<Book> getMatches(String query) {
        java.util.List<Book> matches = new java.util.ArrayList<>();
        findMatchesRecursive(root, query.toLowerCase(), matches);
        return matches;
    }

    private void findMatchesRecursive(Node current, String query, java.util.List<Book> matches) {
        if (current == null) return;
        
        findMatchesRecursive(current.left, query, matches);
        
        // Check if the query matches the current node's data
        if (current.data.getIsbn().contains(query) || current.data.getTitle().toLowerCase().contains(query)) {
            matches.add(current.data);
        }
        
        findMatchesRecursive(current.right, query, matches);
    }

    /**
     * Rebuilds a flat list of all books for CSV synchronization.
     */
    public void getAllBooks(java.util.List<Book> bookList) {
        collectInOrder(root, bookList);
    }

    private void collectInOrder(Node current, java.util.List<Book> bookList) {
        if (current == null) return;
        collectInOrder(current.left, bookList);
        bookList.add(current.data);
        collectInOrder(current.right, bookList);
    }

    /**
     * Linear search used for reconstruction logic when rebuilding the Stack from logs.
     */
    public Book searchByTitle(String title) {
        return searchTitleRecursive(root, title);
    }

    private Book searchTitleRecursive(Node current, String title) {
        if (current == null) return null;
        if (current.data.getTitle().equalsIgnoreCase(title)) return current.data;

        // Search both branches since titles are not organized by the ISBN BST rules
        Book found = searchTitleRecursive(current.left, title);
        if (found != null) return found;
        return searchTitleRecursive(current.right, title);
    }
}