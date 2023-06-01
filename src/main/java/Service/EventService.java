package Service;

import Entity.Event;

import java.util.Date;
import java.util.List;

public interface EventService {

    Event createEvent(String createdByUser, Date startTime, Date endTime, List<String> participantUsers);

    List<Event> fetchEventForUser(String userId);

    Event deleteEvent(String eventId, String userId);

   // TimeSlot findMostFavourable(String organizer, List<Integer> participantUser);

    List<Event> eventsWithConflicts(String userId);
}
