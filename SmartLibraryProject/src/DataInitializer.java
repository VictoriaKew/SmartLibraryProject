import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistent database operations for the application.
 * Manages reading and writing operations for the master library CSV data file.
 */
public class DataInitializer {
    private static final String FILE_PATH = "library_data.csv";

    /**
     * Reads the master library CSV file on startup to populate the system database tree.
     * Safely parses values, constructs Book instances, restores their availability flags, 
     * and inserts them directly into the tree structure.
     * 
     * @param lib The active SmartLibrary controller instance being initialized
     */
    public static void loadLibraryData(SmartLibrary lib) {
        File file = new File(FILE_PATH);
        System.out.println(Colors.YELLOW + "Looking for file at: " + file.getAbsolutePath() + Colors.RESET);
        if (!file.exists()) {
            System.out.println(Colors.YELLOW + "[System] No database found. Starting fresh." + Colors.RESET);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Regex: Splits by commas ONLY if they are outside of double quotes
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                if (data.length == 4) {
                    String isbn = data[0].trim();
                    String title = data[1].replace("\"", "").trim();
                    String author = data[2].replace("\"", "").trim();
                    
                    Book book = new Book(isbn, title, author);
                    book.setAvailable(Boolean.parseBoolean(data[3].trim()));
                    lib.addBook(book);
                } else {
                    System.out.println(Colors.YELLOW + "[Warning] Skipped corrupted CSV row: " + line + Colors.RESET);
                }
            }
            System.out.println(Colors.GREEN + "[System] Database loaded successfully." + Colors.RESET);
        } catch (IOException e) {
            System.out.println(Colors.RED + "Load Error: " + e.getMessage() + Colors.RESET);
        }
    }

    /**
     * Appends a single newly registered book record directly to the end of the CSV file.
     * Adds double quotes to titles and authors that contains commas before writing in to the CSV file
     * to prevent read issues
     * Prevents having to rewrite the whole file when simply adding an individual new book item.
     * 
     * @param book The new Book object to write to the file
     */
    public static void saveBookToCSV(Book book) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            String title = book.getTitle().contains(",") ? "\"" + book.getTitle() + "\"" : book.getTitle();
            String author = book.getAuthor().contains(",") ? "\"" + book.getAuthor() + "\"" : book.getAuthor();
            
            out.println(String.format("%s,%s,%s,%b", 
                book.getIsbn(), title, author, book.isAvailable()));
        } catch (IOException e) {
            System.out.println(Colors.RED + "Save Error: " + e.getMessage() + Colors.RESET);
        }
    }

    /**
     * Flattens the database tree and overwrites the CSV file with the updated state.
     * Triggered whenever a book's availability status changes (during borrow or return operations)
     * to keep persistent records synchronized with the runtime system.
     * 
     * @param lib The active SmartLibrary controller instance
     */
    public static void syncDatabase(SmartLibrary lib) {
        List<Book> allBooks = new ArrayList<>();
        lib.getDatabase().getAllBooks(allBooks);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, false)))) {
            for (Book book : allBooks) {
                String title = book.getTitle().contains(",") ? "\"" + book.getTitle() + "\"" : book.getTitle();
                String author = book.getAuthor().contains(",") ? "\"" + book.getAuthor() + "\"" : book.getAuthor();
                
                out.println(String.format("%s,%s,%s,%b", 
                    book.getIsbn(), title, author, book.isAvailable()));
            }
        } catch (IOException e) {
            System.out.println(Colors.RED + "Sync Error: " + e.getMessage() + Colors.RESET);
        }
    }
}