package exercise1.structural.flyweight.src;

public class Main {
    public static void main(String[] args){
        GlyphFactory f = new GlyphFactory();
        String s = "hello hello";
        for(char c: s.toCharArray()){
            Glyph g = f.getGlyph(c);
            g.draw();
        }
        System.out.println();
        System.out.println("Glyph pool size: " + f.poolSize());
        System.out.println("Flyweight Pattern demo finished.");
    }
}
