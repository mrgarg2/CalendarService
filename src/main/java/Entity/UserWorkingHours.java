package Entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserWorkingHours {

    String workingHourId;
    String userId;
    Date startTime;
    Date endTime;
}
