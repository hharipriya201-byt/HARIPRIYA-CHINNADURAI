package exercise1.structural.composite.src;

import java.util.ArrayList;
import java.util.List;

public class Directory implements Component {
    private String name;
    private List<Component> children = new ArrayList<>();
    public Directory(String name){ this.name = name; }
    public void add(Component c){ children.add(c); }
    public void show(String indent){
        System.out.println(indent + "+ " + name);
        for(Component c: children) c.show(indent + "  ");
    }
}
