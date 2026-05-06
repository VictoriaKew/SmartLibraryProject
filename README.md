# Smart Library Project
## README: Smart Library System

Welcome to the **Smart Library System**, a high-performance console-based application designed for efficient book management using advanced data structures. This system is tailored for the specific needs of a modern librarian, utilizing a **Binary Search Tree (BST)** for optimized searching and a **Stack** for session-based transaction tracking.

---

### 🛠 System Architecture & Features

The system operates through a structured command-line interface with six primary options. Below is the technical breakdown of the logic and presentation for each:

#### **Option 1: Add New Book**
*   **Methods Involved**:
    *   `Book(isbn, title, author)`: Instantiates a new Book object with default availability set to `true`.
    *   `SmartLibrary.addBook(book)`: Routes the new object to the BST.
    *   `BookBST.insert(newBook)`: Recursively places the book in the tree based on ISBN for $O(\log n)$ retrieval.
    *   `DataInitializer.saveBookToCSV(nb)`: Appends the book details to the permanent `library_data.csv` file.
*   **Output**: A green confirmation message: **"Saved to system"** or **"Successfully saved to Smart Library System."**

#### **Option 2: Search Book**
*   **Methods Involved**:
    *   `SmartLibrary.isEnglishOnly(query)`: A regex-based validator ensuring queries contain only alphanumeric characters.
    *   `SmartLibrary.searchOnly(query)`: Coordinates the search UI and result fetching.
    *   `BookBST.getMatches(query)`: Recursively traverses the BST to find all books where the ISBN or Title contains the search string.
*   **Output**: Results are displayed between purple **separators**. A cyan header identifies the columns, followed by matching books formatted via `Book.toString()`, showing real-time status (Available/Borrowed) in color-coded text.

#### **Option 3: Borrow Book**
*   **Methods Involved**:
    *   `SmartLibrary.borrowBook(query)`: Handles selection logic (including multiple-match resolution).
    *   `BorrowStack.push(selectedBook)`: Pushes the book onto the LIFO stack and appends a timestamped entry to `transaction_history.csv`.
    *   `DataInitializer.syncDatabase(lib)`: Overwrites the inventory CSV to update the book's status to `false`.
*   **Output**: An interactive loop allows multiple borrows until the user types **"done."** Success is confirmed with a green message: **"Success! Borrowed: [Title]."**

#### **Option 4: Return Book**
*   **Methods Involved**:
    *   `SmartLibrary.returnLastBook()`: Triggers the return process using LIFO logic.
    *   `BorrowStack.pop()`: Removes the top book from the stack and logs the return in the transaction ledger.
    *   `DataInitializer.syncDatabase(lib)`: Updates the inventory CSV to mark the book as `true` (Available).
*   **Output**: A green success message: **"Returned (LIFO): [Title]."** If no books are in the current session stack, a red error is displayed: **"No books in the stack to return."**

#### **Option 5: Check History**
*   **Methods Involved**:
    *   `SmartLibrary.showHistory()`: Acts as a gateway to the history ledger.
    *   `BorrowStack.displayHistory()`: Iterates through the stored log list to render the full activity history.
*   **Output**: Displays the **"OFFICIAL TRANSACTION LEDGER"** in cyan. Borrowings are marked with a green **`[+]`** and returns with a red **`[-]`**, including precise timestamps for each action.

#### **Option 6: Exit**
*   **Methods Involved**:
    *   `System.exit(0)`: Safely terminates the Java application.
*   **Output**: Prints a final purple message: **"Closing Smart Library System... Goodbye!"** framed by separators before the program ends.

---

### 💻 Developer Profile
*   **Developer**: Victoria Kew Kim Tian
*   **Academic Year**: Year 1, Universiti Malaya (UM)
*   **Specialization**: Multimedia Computing
*   **Built With**: Java, VS Code