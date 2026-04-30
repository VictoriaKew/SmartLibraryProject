public class DataInitializer {
    /**
     * This method populates the SmartLibrary with 20 dummy books.
     * It simulates the work of the 'QA & Data Lead'.
     */
    public static void loadSampleData(SmartLibrary lib) {
        lib.addBook(new Book("9780134685991", "Effective Java", "Joshua Bloch"));
        lib.addBook(new Book("9780132350884", "Clean Code", "Robert Martin"));
        lib.addBook(new Book("9780596009205", "Head First Design Patterns", "Eric Freeman"));
        lib.addBook(new Book("9780201633610", "Design Patterns", "Erich Gamma"));
        lib.addBook(new Book("9780134494166", "Clean Architecture", "Robert Martin"));
        lib.addBook(new Book("9780131103627", "The C Programming Language", "Kernighan"));
        lib.addBook(new Book("9780321125217", "Domain-Driven Design", "Eric Evans"));
        lib.addBook(new Book("9780596517748", "JavaScript: The Good Parts", "Crockford"));
        lib.addBook(new Book("9780137081073", "The Clean Coder", "Robert Martin"));
        lib.addBook(new Book("9780201616224", "The Pragmatic Programmer", "Andrew Hunt"));
        lib.addBook(new Book("9780132143011", "Introduction to Algorithms", "Cormen"));
        lib.addBook(new Book("9780596007126", "Head First Java", "Kathy Sierra"));
        lib.addBook(new Book("9780321356680", "Effective C++", "Scott Meyers"));
        lib.addBook(new Book("9780131103702", "Operating Systems", "Tanenbaum"));
        lib.addBook(new Book("9780596004651", "Java Network Programming", "Elliotte Rusty"));
        lib.addBook(new Book("9780132350800", "Refactoring", "Martin Fowler"));
        lib.addBook(new Book("9780131177054", "Working Effectively with Legacy Code", "Feathers"));
        lib.addBook(new Book("9780321146533", "Test Driven Development", "Kent Beck"));
        lib.addBook(new Book("9780596003005", "Learning Java", "Niemeyer"));
        lib.addBook(new Book("9780136291558", "Object-Oriented Analysis", "Grady Booch"));
        
        System.out.println("\u001B[32m" + "[System] 20 Books successfully loaded into the BST." + "\u001B[0m");
    }
}