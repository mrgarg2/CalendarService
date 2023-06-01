package Repository;

import Entity.Event;

import java.util.List;

public interface EventRepository {

    Event findEventById(String eventId);

    List<Event> findEventCreatedByUser(String userId);


    Event deleteEvent(String eventId);

    Event save(Event event);
}
