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
                String[] data = line.split(",");
                if (data.length == 4) {
                    Book book = new Book(data[0], data[1], data[2]);
                    book.setAvailable(Boolean.parseBoolean(data[3]));
                    lib.addBook(book);
                }
            }
            System.out.println(Colors.GREEN + "[System] Database loaded successfully." + Colors.RESET);
        } catch (IOException e) {
            System.out.println(Colors.RED + "Load Error: " + e.getMessage() + Colors.RESET);
        }
    }

    /**
     * Appends a single newly registered book record directly to the end of the CSV file.
     * Prevents having to rewrite the whole file when simply adding an individual new book item.
     * 
     * @param book The new Book object to write to the file
     */
    public static void saveBookToCSV(Book book) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_PATH, true)))) {
            out.println(String.format("%s,%s,%s,%b", 
                book.getIsbn(), book.getTitle(), book.getAuthor(), book.isAvailable()));
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
                out.println(String.format("%s,%s,%s,%b", 
                    book.getIsbn(), book.getTitle(), book.getAuthor(), book.isAvailable()));
            }
        } catch (IOException e) {
            System.out.println(Colors.RED + "Sync Error: " + e.getMessage() + Colors.RESET);
        }
    }
}