package Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventParticipants {
    String eventParticipantId;
    String eventId;
    String userId;
}
