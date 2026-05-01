import java.util.Stack;

public class BorrowStack {
    private Stack<Book> stack = new Stack<>();

    public void push(Book b) {
        stack.push(b);
        System.out.println("Borrowed: [ISBN: " + b.isbn + "] " + b.title);
    }

    public Book pop(){
        if(stack.isEmpty()){
            System.out.println("No history to pop.");
            return null;
        } else {
            return stack.pop();
        }
    }

    public void show(){
        if(stack.isEmpty()){
            System.out.println("History is empty.");
        } else {
            // Reverse iteration to show most recent first
            for(int i = stack.size() - 1; i >= 0; i--){
                Book b = stack.get(i);
                System.out.println("[ISBN: " + b.isbn + "]" + b.title);
            }
        }
    }
}