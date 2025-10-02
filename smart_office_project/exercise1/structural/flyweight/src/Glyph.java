package exercise1.structural.flyweight.src;

public class Glyph {
    private final char ch;
    public Glyph(char c){ this.ch = c; }
    public void draw(){ System.out.print(ch); }
}
