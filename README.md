# Smart Library System (Console Edition)

A high-performance, console-based library administration and borrowing portal built in Java. This system leverages advanced custom data structures—specifically a **Binary Search Tree (BST)** for highly optimized inventory indexing and a **Stack (LIFO)** tracking model to manage sequential borrowing and automated book returns.

---

## 🛠️ Architecture & Core Components

The system is strictly built using clean Object-Oriented Programming (OOP) principles and an **Abstract Data Type (ADT)** pattern to separate core business logic from data storage and terminal rendering.

### 1. Database Indexing Layout (Custom BST)
Instead of linear searching arrays (which require $O(N)$ time complexity), the library utilizes a custom Binary Search Tree (`BookBST`).
* **Sorting Rule:** Nodes are arranged lexicographically based on the book's unique International Standard Book Number (**ISBN**).
* **Inventory Printing:** Employs **In-Order Traversal** (Left subtree, Root node, Right subtree) to dynamically stream the library catalog in perfect ascending order.
* **Search Performance:** Yields average $O(\log N)$ search intervals for lightning-fast matching operations.

### 2. Transaction Management (LIFO Stack)
User borrowing sessions follow a Last-In, First-Out (**LIFO**) contract managed by the `BorrowStack`.
* **Automated Returns:** Students can execute zero-input quick returns. The system simply pops the most recently borrowed item off the stack, restoring its availability status instantly.
* **Persistent Ledger:** Every transaction dynamically formats an immutable ledger block, written directly to an external transaction archive.

### 3. File Synchronization Engine
To prevent session data dropouts upon system shutdowns, `DataInitializer` provides complete lifecycle data persistence.
* **Database Restoration:** At startup, the engine maps records from `library_data.csv` straight into the memory collection tree.
* **State Synchronization:** Any change to book availability flags automatically flattens the memory tree to refresh the underlying CSV, keeping records current.
* **Stack Recovery:** Re-scans collection tree flags upon boot to rebuild the LIFO memory sequence seamlessly without requiring duplicate input sequences.

---

## 📂 Project Directory Structure

```text
├── src/
│   ├── Book.java              # Core Data Model (Implements Comparable)
│   ├── BookBST.java           # Custom Binary Search Tree & Node Architecture
│   ├── BorrowStack.java       # Session Management Stack & Logger
│   ├── Colors.java            # ANSI Terminal Formatting Color Utilities
│   ├── DataInitializer.java   # CSV Parser & State Synchronization Engine
│   ├── LibraryADT.java        # Operational Architectural Contract Interface
│   ├── SmartLibrary.java      # Central System Processing Controller Core
│   └── Main.java              # Application Initialization & Loop Router Interface
├── library_data.csv           # Master Inventory Database File (Auto-generated)
└── transaction_history.csv    # Official Audit Ledger History Archive (Auto-generated)

