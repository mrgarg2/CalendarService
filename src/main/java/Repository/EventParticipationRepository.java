package Repository;

import Entity.Event;
import Entity.EventParticipants;

import java.util.List;

public interface EventParticipationRepository {

    List<Event> findEventsParticipationByUser(String userId);

    EventParticipants save(EventParticipants eventParticipants);
}
