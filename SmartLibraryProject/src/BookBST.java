import java.util.ArrayList;
import java.util.List;

/**
 * Node class representing a single structural point in the Binary Search Tree.
 * Packages data properties alongside structural pointers.
 */
class Node {
    Book data;
    Node left, right;
    
    /**
     * Constructs a tree leaf container holding a Book reference.
     * Left and right branch pointers default to null.
     * 
     * @param book The Book data payload to insert into this node position
     */
    public Node(Book book) { this.data = book; }
}

/**
 * BookBST manages the collection of books using a Binary Search Tree logic.
 * Organizes tree nodes according to the natural sorting order of Book ISBN values.
 */
public class BookBST {
    private Node root;

    /**
     * Public interface to insert a new book record into the tree.
     * Passes execution to the internal recursive insertion pipeline.
     * 
     * @param newBook The Book entity to place into the database structure
     */
    public void insert(Book newBook) {
        root = insertRecursive(root, newBook);
    }

    /**
     * Tail recursive algorithm evaluating node positions for book insertions.
     * Compares ISBN keys; roots smaller keys down the left subtree and larger keys down the right.
     * 
     * @param current The cursor Node presently under evaluation
     * @param newBook The targeting Book object intended for placement
     * @return The updated Node pointer reference back to its caller
     */
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
     * Traverses and prints the complete tree index to the console.
     * Employs In-Order Traversal to ensure books are read out in ascending order of ISBNs.
     */
    public void displayAll() {
        displayInOrder(root);
    }

    /**
     * Recursive structural traversal agent operating under In-Order mechanics.
     * Iterates fully down the left node chain, prints the localized node element, 
     * and branches cleanly down the matching right node pathways.
     * 
     * @param current The specific Node pointer presently processing out
     */
    private void displayInOrder(Node current) {
        if (current != null) {
            displayInOrder(current.left);
            System.out.println(current.data);
            displayInOrder(current.right);
        }
    }

    /**
     * Performs a substring-matched query across the database to gather all relevant records.
     * Initiates a full tree scan to collect multiple partial title or ISBN matches.
     * 
     * @param query The user's input search string
     * @return A List containing any Book objects containing matching keywords
     */
    public List<Book> getMatches(String query) {
        List<Book> matches = new ArrayList<>();
        findMatchesRecursive(root, query.toLowerCase(), matches);
        return matches;
    }

    /**
     * Recursive evaluation engine parsing all nodes to look for keyword matches.
     * Since general keyword searches can span non-key fields (like titles), this checks
     * all paths, translating attributes to lowercase to execute a case-insensitive match.
     * 
     * @param current The active evaluation Node pointer
     * @param query   The lower-cased input parameter keyword
     * @param matches The external collection array capturing target records
     */
    private void findMatchesRecursive(Node current, String query, List<Book> matches) {
        if (current == null) return;
        
        findMatchesRecursive(current.left, query, matches);
        
        // Check if the query matches the current node's data
        if (current.data.getIsbn().contains(query) || current.data.getTitle().toLowerCase().contains(query)) {
            matches.add(current.data);
        }
        
        findMatchesRecursive(current.right, query, matches);
    }

    /**
     * Flattens the entire tree inventory into a standard indexed array list.
     * Crucial for data maintenance tasks, such as saving the database state to a CSV file.
     * 
     * @param bookList An external array tracking list waiting to store references
     */
    public void getAllBooks(List<Book> bookList) {
        collectInOrder(root, bookList);
    }

    /**
     * Traverses the tree in-order to populate a linear array structure.
     * Safely copies node data references into the provided tracking list sequentially.
     * 
     * @param current  The Node pointer currently being processed
     * @param bookList The target destination collection array list
     */
    private void collectInOrder(Node current, List<Book> bookList) {
        if (current == null) return;
        collectInOrder(current.left, bookList);
        bookList.add(current.data);
        collectInOrder(current.right, bookList);
    }

    /**
     * Scans the system to find a book matching an exact title sequence.
     * Typically used when rebuilding session parameters from secondary transaction logs.
     * 
     * @param title The exact, case-insensitive title name string to locate
     * @return The matching Book instance found, or null if no record matches
     */
    public Book searchByTitle(String title) {
        return searchTitleRecursive(root, title);
    }

    /**
     * Recursive linear-style search across the ISBN-ordered tree structure to check titles.
     * Exhaustively parses both tree branches when a node choice fails, since node keys 
     * are organized by ISBN criteria rather than title characters.
     * 
     * @param current The specific Node block being assessed
     * @param title   The exact target title search string
     * @return The validated Book object if found; otherwise null
     */
    private Book searchTitleRecursive(Node current, String title) {
        if (current == null) return null;
        if (current.data.getTitle().equalsIgnoreCase(title)) return current.data;

        // Search both branches since titles are not organized by the ISBN BST rules
        Book found = searchTitleRecursive(current.left, title);
        if (found != null) return found;
        return searchTitleRecursive(current.right, title);
    }
}