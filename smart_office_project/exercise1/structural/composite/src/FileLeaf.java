package exercise1.structural.composite.src;

public class FileLeaf implements Component {
    private String name;
    public FileLeaf(String name){ this.name = name; }
    public void show(String indent){ System.out.println(indent + "- " + name); }
}
