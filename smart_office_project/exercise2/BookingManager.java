package exercise2;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Singleton booking manager. Uses ScheduledExecutorService to release bookings
 * if room remains unoccupied for 5 minutes.
 *
 * For demo convenience, timeouts may be accelerated by modifying RELEASE_DELAY.
 */
public class BookingManager {
    private static final Logger LOG = Logger.getLogger(BookingManager.class.getName());
    private static BookingManager instance;
    private final Map<Integer, ScheduledFuture<?>> releaseTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    // Real app: Duration.ofMinutes(5)
    private static final Duration RELEASE_DELAY = Duration.ofMinutes(5);
    private BookingManager(){}
    public static synchronized BookingManager getInstance(){
        if(instance==null) instance = new BookingManager();
        return instance;
    }
    public synchronized boolean book(int roomId, Instant from, Duration minutes){
        OfficeConfig cfg = OfficeConfig.getInstance();
        Optional<Room> rOpt = cfg.getRoom(roomId);
        if(!rOpt.isPresent()) throw new IllegalArgumentException("Invalid room");
        Room r = rOpt.get();
        synchronized(r){
            if(r.isBooked()){
                LOG.warning("Room " + roomId + " already booked");
                return false;
            }
            r.setBooked(true, from.plus(minutes));
            LOG.info("Room " + roomId + " booked from " + from + " for " + minutes.toMinutes() + " mins");
            scheduleAutoReleaseIfUnoccupied(r, RELEASE_DELAY);
            return true;
        }
    }
    public synchronized boolean cancel(int roomId){
        OfficeConfig cfg = OfficeConfig.getInstance();
        Optional<Room> rOpt = cfg.getRoom(roomId);
        if(!rOpt.isPresent()) throw new IllegalArgumentException("Invalid room");
        Room r = rOpt.get();
        synchronized(r){
            if(!r.isBooked()){
                LOG.warning("Room " + roomId + " is not booked");
                return false;
            }
            r.setBooked(false, null);
            ScheduledFuture<?> f = releaseTasks.remove(roomId);
            if(f!=null) f.cancel(false);
            LOG.info("Booking for room " + roomId + " cancelled");
            return true;
        }
    }
    private void scheduleAutoReleaseIfUnoccupied(Room r, Duration delay){
        int id = r.id();
        ScheduledFuture<?> existing = releaseTasks.get(id);
        if(existing!=null) existing.cancel(false);
        // Check after 'delay' whether room is still unoccupied and release booking.
        ScheduledFuture<?> f = scheduler.schedule(() -> {
            try{
                synchronized(r){
                    if(!r.isOccupied()){
                        r.setBooked(false, null);
                        LOG.info("Room " + id + " booking released automatically (unoccupied for " + delay.toMinutes() + " mins)");
                    } else {
                        LOG.info("Room " + id + " was occupied; booking retained.");
                    }
                }
            } catch(Exception ex){ LOG.warning("Auto-release failed: " + ex.getMessage()); }
        }, delay.toMillis(), TimeUnit.MILLISECONDS);
        releaseTasks.put(id, f);
    }
    public void shutdown(){ scheduler.shutdownNow(); }
}
