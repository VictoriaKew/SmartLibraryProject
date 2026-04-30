import java.util.Scanner;

public class SmartLibrary implements LibraryADT {
    private BookBST bookDatabase = new BookBST();
    private BorrowStack userHistory = new BorrowStack();

    @Override
    public void addBook(Book book) {
        bookDatabase.insert(book);
    }

    @Override
    public void borrowBook(String query) {
        // 1. Strict Validation: Reject symbols and mixed letter/number strings
        if (!isEnglishOnly(query)) {
            System.out.println(Colors.RED + "Error: Invalid characters. Numbers and symbols are not allowed in title searches." + Colors.RESET);
        return;
        }

        // 2. Get matches using case-insensitive logic
        java.util.List<Book> matches = bookDatabase.getMatches(query.toLowerCase());

        if (matches.isEmpty()) {
            System.out.println(Colors.RED + "Error: No book found with that name or ISBN." + Colors.RESET);
            return;
        }

        Book selectedBook = null;

        // 3. Auto-select if unique, otherwise handle multiple matches
        if (matches.size() == 1) {
            selectedBook = matches.get(0);
        } else {
            System.out.println(Colors.YELLOW + "Multiple books found:" + Colors.RESET);
            for (Book b : matches) System.out.println(b);
        
        System.out.print("Please enter the EXACT Title or ISBN to confirm: ");
        Scanner sc = new Scanner(System.in);
        String confirmation = sc.nextLine().trim();
        
        // Final confirmation check (Case-insensitive for titles)
        for (Book b : matches) {
            if (b.getIsbn().equals(confirmation) || b.getTitle().toLowerCase().contains(confirmation.toLowerCase())) {
                selectedBook = b;
                break;
            }
        }
    }

    // 4. Final Status Check
    if (selectedBook == null) {
        System.out.println(Colors.RED + "Error: Confirmation did not match any of the results." + Colors.RESET);
    } else if (!selectedBook.isAvailable()) {
        System.out.println(Colors.RED + "Error: '" + selectedBook.getTitle() + "' is already borrowed." + Colors.RESET);
    } else {
        selectedBook.setAvailable(false);
        userHistory.push(selectedBook);
        System.out.println(Colors.GREEN + "Success! Borrowed: " + selectedBook.getTitle() + Colors.RESET);
    }
}

    public boolean isEnglishOnly(String input) {
        return input.matches("^[a-zA-Z\\s]+$") || input.matches("^[0-9]+$");
    }

    @Override
    public void returnLastBook() {
        Book last = userHistory.pop();
        if (last != null) {
            last.setAvailable(true);
            System.out.println("Returned: " + last.getTitle());
        } else {
            System.out.println("No books to return in your history.");
        }
    }

    public void showShelf() { bookDatabase.displayAll(); }

    public void showHistory() { userHistory.displayHistory(); }

    public boolean validateIsbn(String isbn) {
        return isbn.length() == 13 && isbn.matches("\\d+");
    }
}