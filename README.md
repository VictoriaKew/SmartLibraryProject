📚 Smart Library System

Welcome to the Smart Library System, a high-performance console-based Java application designed for efficient book management. Tailored for the specific needs of modern librarians, this system leverages advanced data structures—specifically a Binary Search Tree (BST) for optimized searching and a Stack for session-based transaction tracking—to ensure fast, reliable, and persistent library operations.

✨ Key Features at a Glance
O(log n) Search Efficiency: Powered by a custom Binary Search Tree.
Chronological LIFO Tracking: Managed via a Stack data structure for exact return logic.
Persistent Storage: Real-time synchronization with library_data.csv and transaction_history.csv.
Bulletproof Validation: Regex-based input handling prevents crashes and data corruption.
Rich Console UI: Features ANSI color-coding and formatted ASCII banners.

🛠️ System Architecture & Operations
The system operates through a robust, crash-proof command-line interface featuring six primary options:

1. Add New Book
Registers a new book into the active database and saves it permanently.

Methods:

Book(isbn, title, author): Instantiates a new object (defaults to isAvailable = true).
SmartLibrary.addBook(book): Routes the object to the BST.
BookBST.insert(newBook): Recursively places the book in the tree based on ISBN.
DataInitializer.saveBookToCSV(nb): Appends details to library_data.csv.

Console Output: 🟩 "Successfully saved to Smart Library System."

2. Search Book
Performs a non-destructive, case-insensitive search across the library.

Methods:

SmartLibrary.isEnglishOnly(query): Regex validator ensuring only alphanumeric inputs.
SmartLibrary.searchOnly(query): Coordinates UI and fallback logic (displays entire shelf if no match is found).
BookBST.getMatches(query): Recursively traverses the BST finding substring matches in ISBNs or Titles.

Console Output: Displays results in a formatted, cyan-headed table with real-time, color-coded availability status.

3. Borrow Book
Checks out a book and updates the global ledger.

Methods:

SmartLibrary.borrowBook(query): Handles selection logic, including forcing exact ISBN confirmation if multiple matches are found.
BorrowStack.push(selectedBook): Pushes the book to the LIFO stack and logs a timestamped entry.
DataInitializer.syncDatabase(lib): Overwrites the CSV to mark the book's status as false.

Console Output: 🟩 "Success! Borrowed: [Title]" (Loops until the user types "done").

4. Return Book
Returns the most recently borrowed book from the current session.

Methods:

SmartLibrary.returnLastBook(): Triggers the return process using LIFO rules.
BorrowStack.pop(): Removes the top book from the stack and logs the return.
DataInitializer.syncDatabase(lib): Updates the CSV to mark the book as true.

Console Output: 🟩 "Returned (LIFO): [Title]" or 🟥 "No books in the stack to return.

5. Check History
Displays the persistent transaction ledger.

Methods:

SmartLibrary.showHistory(): Gateway to the history data.
BorrowStack.displayHistory(): Iterates through the stored log list to render activity.

Console Output: Displays the "OFFICIAL TRANSACTION LEDGER". Borrowings are marked with 🟩 [+] and returns with 🟥 [-], alongside precise timestamps.

6. Exit
Safely terminates the application.

Methods:

System.exit(0): Closes the JVM.

Console Output: 🟪 "Closing Smart Library System... Goodbye!"
