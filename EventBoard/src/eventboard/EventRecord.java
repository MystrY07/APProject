
package eventboard;

/**
 *
 * @author aorpr
 */
public class EventRecord {
    
    private String date;
    private String time;
    private String eventDesc;

    public EventRecord(String date, String time, String eventDesc) {
        this.date = date;
        this.time = time;
        this.eventDesc = eventDesc;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    @Override
    public String toString() {
        return "Event{" + "date=" + date + ", time=" + time + ", eventDesc=" + eventDesc + '}';
    }
    
}
