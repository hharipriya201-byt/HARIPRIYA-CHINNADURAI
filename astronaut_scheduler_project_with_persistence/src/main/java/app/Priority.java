package app;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH;

    public static Priority fromString(String s) {
        try {
            return Priority.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
