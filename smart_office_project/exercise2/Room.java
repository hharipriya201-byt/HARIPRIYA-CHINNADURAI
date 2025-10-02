package exercise2;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Room is observable: observers subscribe to occupancy changes.
 */
public class Room {
    private static final Logger LOG = Logger.getLogger(Room.class.getName());
    private final int id;
    private int maxCapacity = 10;
    private int occupants = 0;
    private boolean booked = false;
    private Instant bookedUntil = null;
    private Instant lastOccupiedAt = null;
    private final List<RoomObserver> observers = new CopyOnWriteArrayList<>();
    public Room(int id){ this.id = id; }
    public int id(){ return id; }
    public synchronized void setMaxCapacity(int c){
        if(c<=0) throw new IllegalArgumentException("Invalid capacity");
        maxCapacity = c;
    }
    public synchronized void addObserver(RoomObserver o){ observers.add(o); }
    public synchronized void removeObserver(RoomObserver o){ observers.remove(o); }
    private void notifyObservers(){
        for(RoomObserver o: observers){
            try { o.update(this); } catch(Exception ex){ LOG.warning("Observer failed: " + ex.getMessage()); }
        }
    }
    public synchronized void setOccupants(int n){
        if(n<0) throw new IllegalArgumentException("Occupants cannot be negative");
        occupants = n;
        if(occupants>=2){
            lastOccupiedAt = Instant.now();
            LOG.info("Room " + id + " occupied by " + occupants);
        } else {
            LOG.info("Room " + id + " occupancy insufficient: " + occupants);
        }
        notifyObservers();
    }
    public synchronized int occupants(){ return occupants; }
    public synchronized boolean isOccupied(){ return occupants>=2; }
    public synchronized void setBooked(boolean b, Instant until){
        booked = b; bookedUntil = until;
    }
    public synchronized boolean isBooked(){ return booked; }
    public synchronized Instant bookedUntil(){ return bookedUntil; }
    public synchronized Instant lastOccupiedAt(){ return lastOccupiedAt; }
    public synchronized int maxCapacity(){ return maxCapacity; }
}
