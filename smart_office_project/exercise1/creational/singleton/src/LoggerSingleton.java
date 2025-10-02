package exercise1.creational.singleton.src;

import java.util.logging.Logger;

public class LoggerSingleton {
    private static final Logger LOGGER = Logger.getLogger("GlobalLogger");
    private static LoggerSingleton instance;
    private LoggerSingleton(){ /* private */ }
    public static synchronized LoggerSingleton getInstance(){
        if(instance==null) instance = new LoggerSingleton();
        return instance;
    }
    public Logger getLogger(){ return LOGGER; }
}
