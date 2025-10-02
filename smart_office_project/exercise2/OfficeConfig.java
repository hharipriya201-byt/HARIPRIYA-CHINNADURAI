package exercise2;

import java.util.*;
import java.util.logging.Logger;

/**
 * Singleton that holds office configuration and room instances.
 */
public class OfficeConfig {
    private static final Logger LOG = Logger.getLogger(OfficeConfig.class.getName());
    private static OfficeConfig instance;
    private final Map<Integer, Room> rooms = new HashMap<>();
    private OfficeConfig() {}
    public static synchronized OfficeConfig getInstance(){
        if(instance==null) instance = new OfficeConfig();
        return instance;
    }
    public synchronized void configureRooms(int count){
        if(count<=0) throw new IllegalArgumentException("Room count must be positive");
        rooms.clear();
        for(int i=1;i<=count;i++){
            rooms.put(i, new Room(i));
        }
        LOG.info("Office configured with " + count + " rooms");
    }
    public Optional<Room> getRoom(int id){
        return Optional.ofNullable(rooms.get(id));
    }
    public Collection<Room> allRooms(){ return Collections.unmodifiableCollection(rooms.values()); }
}
