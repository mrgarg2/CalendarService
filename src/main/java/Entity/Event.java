package Entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Event {
    public String eventId;
    public String ownerUserId;

    Date startTime;
    Date endTime;

}
