
package eventboard;

/**
 *
 * @author aorpr
 */
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventActions {
    // Thread-safe list for CONCURRENT access
    private static List<EventRecord> events = new CopyOnWriteArrayList<>();

    //Add event with error handlign
    public static synchronized String add(String date, String time, String description) throws InvalidActionException {
        if (date == null || date.isBlank()) throw new InvalidActionException("Invalid date");
        if (time == null || time.isBlank()) throw new InvalidActionException("Invalid time");
        if (description == null || description.isBlank()) throw new InvalidActionException("Invalid description");

        EventRecord event = new EventRecord(date.trim(), time.trim(), description.trim());
        events.add(event);

        return list(date); // return updated list for the date
    }

    //remove event with error handling
    public static synchronized String remove(String date, String time, String description) throws InvalidActionException {
        if (date == null || date.isBlank()) throw new InvalidActionException("Invalid date");
        if (time == null || time.isBlank()) throw new InvalidActionException("Invalid time");
        if (description == null || description.isBlank()) throw new InvalidActionException("Invalid description");

        boolean removed = events.removeIf(e ->
                e.getDate().equals(date.trim()) &&
                e.getTime().equals(time.trim()) &&
                e.getEventDesc().equals(description.trim())
        );

        if (!removed) throw new InvalidActionException("Event not found");

        return list(date); // return updated list
    }

    //list events by date 
    public static synchronized String list(String date) throws InvalidActionException {
        if (date == null || date.isBlank()) throw new InvalidActionException("Invalid date");

        // Filter events for this date
        List<EventRecord> filtered = new ArrayList<>();
        for (EventRecord e : events) {
            if (e.getDate().equals(date.trim())) filtered.add(e);
        }

        // Sort by time "6pm" / "12pm" etc
        filtered.sort(Comparator.comparing(EventRecord::getTime));

        // Build output: "date; time, description; time, description; ..."
        if (filtered.isEmpty()) return date.trim() + "; -";

        StringBuilder sb = new StringBuilder();
        sb.append(date.trim());
        for (EventRecord e : filtered) {
            sb.append("; ").append(e.getTime()).append(", ").append(e.getEventDesc());
        }
        return sb.toString();
    }
}
