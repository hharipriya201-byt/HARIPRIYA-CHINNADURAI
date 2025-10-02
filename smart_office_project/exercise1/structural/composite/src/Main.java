package exercise1.structural.composite.src;

public class Main {
    public static void main(String[] args){
        Directory root = new Directory("root");
        root.add(new FileLeaf("file1.txt"));
        Directory sub = new Directory("subdir");
        sub.add(new FileLeaf("file2.txt"));
        root.add(sub);
        root.show("");
        System.out.println("Composite Pattern demo finished.");
    }
}
