package Model;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;

@Data
public class FreeTime {
    HashSet<String> userSet;

    Date startTime;
    Date endTime;
}
