import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataInitializer {
    private static final String FILE_PATH = "library_data.csv";

    /**
     * Reads the master library CSV and populates the BST at startup.
     */
    public static void loadLibraryData(SmartLibrary lib) {
        File file = new File(FILE_PATH);
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
     * Appends a newly created book to the CSV file.
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
     * Overwrites the CSV with the current state of the BST.
     * This is called whenever a book's availability changes (Borrow/Return).
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