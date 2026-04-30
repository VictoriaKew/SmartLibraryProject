# Smart Library System
# WIA1002 Data Sructure Assignment 2025/26

## Project Overview
This project is a functional console interface for a university library. It demonstrates the application of specific Abstract Data Types (ADTs) to optimize access patterns:
* **Binary Search Tree (BST):** Used for the library catalogue to ensure fast $O(\log n)$ searching by ISBN.
* **Stack:** Used for the borrowing history to ensure the most recent transactions are accessed first (LIFO).

## How to Run
1. Ensure you have the Java Development Kit (JDK) installed.
2. Compile the files: `javac *.java`
3. Run the main class: `java Main`
*(Note: The system will automatically initialize with a catalogue of 20 books for immediate testing).*

## System Architecture & Classes
* `LibraryADT` (Interface): Defines the core methods to ensure information hiding.
* `Book`: The entity class containing ISBN, Title, Author, and BST node pointers.
* `BookBST`: Handles the recursive insertion and searching of books.
* `BorrowStack`: Handles the LIFO tracking of borrowed items.
* `SmartLibrary`: The Admin Logic class that integrates the structures, handles user UI, and provides strict try-catch input validation.
* `DataInitializer`: Populates the BST with 20 real book records on startup.
* `Main`: The entry point of the program.

## QA & Testing Results
* **Input Validation:** The system utilizes `Scanner.nextLine()` parsed through `Integer.parseInt()` within a `try-catch` block. This prevents the `InputMismatchException` crashes common with `Scanner.nextInt()`.
* **Graceful Degradation:** Attempting to search or borrow non-existent ISBNs returns user-friendly error messages without breaking the execution loop.
