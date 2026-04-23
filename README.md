# SmartLibraryProject
Y1S2 WIA 1002 DATA STRUCTURE GROUP ASSIGNMENT 

# Project Overview
This is a Functional Console Interface  designed for a university library system. The project demonstrates the use of different data structures to handle specific access patterns, specifically using a Binary Search Tree (BST) for efficient searching and a Stack for tracking borrowing history.

# Features
Catalogue Management (BST): Stores book titles and authors indexed by ISBN for search efficiency.
Borrowing History (Stack): Tracks checked-out books using a LIFO (Last-In, First-Out) pattern so the most recent activity is shown first.
Information Hiding: Uses a LibraryADT interface to ensure the system's internal structures are private and protected.

# File Structure
LibraryADT.java: The interface defining the system's capabilities.
Book.java: The entity class representing a book node.
BookBST.java: Implementation of the Binary Search Tree and recursive search logic.
BorrowStack.java: Implementation of the Stack for borrowing history.
SmartLibrary.java: The main system logic that implements the ADT.
Main.java: The entry point that runs the menu-driven console interface.
