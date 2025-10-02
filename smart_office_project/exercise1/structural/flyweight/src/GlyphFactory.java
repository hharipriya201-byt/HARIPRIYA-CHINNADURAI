package exercise1.structural.flyweight.src;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlyphFactory {
    private Map<Character, Glyph> pool = new ConcurrentHashMap<>();
    public Glyph getGlyph(char c){
        return pool.computeIfAbsent(c, Glyph::new);
    }
    public int poolSize(){ return pool.size(); }
}
