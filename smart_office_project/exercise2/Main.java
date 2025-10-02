package exercise2;

import exercise2.commands.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Console app entry. Uses simple command parsing.
 *
 * Commands:
 *   config rooms <count>
 *   set maxcapacity <roomId> <capacity>
 *   block <roomId> <minutes>
 *   cancel <roomId>
 *   addoccupant <roomId> <count>
 *   status <roomId>
 *   exit
 */
public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    public static void main(String[] args){
        setupLogger();
        Scanner sc = new Scanner(System.in);
        LOG.info("SmartOffice Console started. Type 'help' for commands.");
        SensorSimulator sensor = new SensorSimulator();
        // Add controllers
        OfficeConfig cfg = OfficeConfig.getInstance();

        while(true){
            System.out.print("> ");
            String line = sc.nextLine();
            if(line==null) break;
            String[] parts = line.trim().split("\\s+");
            if(parts.length==0 || parts[0].isEmpty()) continue;
            String cmd = parts[0].toLowerCase();
            try {
                switch(cmd){
                    case "help": printHelp(); break;
                    case "config":
                        if(parts.length>=3 && "rooms".equalsIgnoreCase(parts[1])){
                            int cnt = Integer.parseInt(parts[2]);
                            cfg.configureRooms(cnt);
                            // register observers on each room
                            for(Room r: cfg.allRooms()){
                                r.addObserver(new LightController());
                                r.addObserver(new ACController());
                            }
                            System.out.println("Office configured with " + cnt + " rooms.");
                        } else System.out.println("Usage: config rooms <count>");
                        break;
                    case "set":
                        if(parts.length>=4 && "maxcapacity".equalsIgnoreCase(parts[1])){
                            int rid = Integer.parseInt(parts[2]);
                            int cap = Integer.parseInt(parts[3]);
                            cfg.getRoom(rid).ifPresentOrElse(r -> { r.setMaxCapacity(cap); System.out.println("Room " + rid + " max capacity set to " + cap); }, () -> System.out.println("Invalid room number.")); 
                        } else System.out.println("Usage: set maxcapacity <roomId> <capacity>");
                        break;
                    case "block":
                        if(parts.length>=3){
                            int rid = Integer.parseInt(parts[1]);
                            long minutes = Long.parseLong(parts[2]);
                            BookCommand b = new BookCommand(rid, Instant.now(), Duration.ofMinutes(minutes));
                            boolean ok = b.execute();
                            System.out.println(ok?"Booked":"Failed to book");
                        } else System.out.println("Usage: block <roomId> <minutes>");
                        break;
                    case "cancel":
                        if(parts.length>=2){
                            int rid = Integer.parseInt(parts[1]);
                            CancelCommand c = new CancelCommand(rid);
                            boolean ok = c.execute();
                            System.out.println(ok?"Cancelled":"Failed to cancel");
                        } else System.out.println("Usage: cancel <roomId>");
                        break;
                    case "addoccupant":
                        if(parts.length>=3){
                            int rid = Integer.parseInt(parts[1]);
                            int occ = Integer.parseInt(parts[2]);
                            sensor.setOccupants(rid, occ);
                            System.out.println("Occupants updated");
                        } else System.out.println("Usage: addoccupant <roomId> <count>");
                        break;
                    case "status":
                        if(parts.length>=2){
                            int rid = Integer.parseInt(parts[1]);
                            cfg.getRoom(rid).ifPresentOrElse(r -> {
                                System.out.println("Room " + rid + ": occupied=" + r.isOccupied() + ", occupants=" + r.occupants() + ", booked=" + r.isBooked());
                            }, () -> System.out.println("Invalid room number."));
                        } else System.out.println("Usage: status <roomId>");
                        break;
                    case "exit":
                        BookingManager.getInstance().shutdown();
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Unknown command. Type 'help'.");
                }
            } catch(NumberFormatException nfe){
                System.out.println("Invalid number format.");
            } catch(Exception ex){
                LOG.warning("Command failed: " + ex.getMessage());
            }
        }
    }
    private static void printHelp(){
        System.out.println("Commands:");
        System.out.println(" config rooms <count>");
        System.out.println(" set maxcapacity <roomId> <capacity>");
        System.out.println(" block <roomId> <minutes>");
        System.out.println(" cancel <roomId>");
        System.out.println(" addoccupant <roomId> <count>");
        System.out.println(" status <roomId>");
        System.out.println(" exit");
    }
    private static void setupLogger(){
        Logger root = Logger.getLogger("");
        Handler[] handlers = root.getHandlers();
        for(Handler h: handlers) root.removeHandler(h);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.INFO);
        ch.setFormatter(new SimpleFormatter());
        root.addHandler(ch);
        root.setLevel(Level.INFO);
    }
}
