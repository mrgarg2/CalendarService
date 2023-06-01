package Repository.impl;

import Entity.Event;
import Repository.EventRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EventRepositoryImpl implements EventRepository {

    HashMap<String, Event> eventMap = new HashMap<>();

    @Override
    public Event findEventById(String eventId) {
        if(!eventMap.containsKey(eventId))  return null;

        return eventMap.get(eventId);
    }

    @Override
    public List<Event> findEventCreatedByUser(String userId) {
        List<Event> eventList = new ArrayList<>();

        for(String eventId: eventMap.keySet()){
            if(eventMap.get(eventId).getOwnerUserId().equals(userId)){
                eventList.add(eventMap.get(eventId));
            }
        }

        return eventList;
    }

    @Override
    public Event deleteEvent(String eventId) {
        if(!eventMap.containsKey(eventId))  return null;

        return eventMap.remove(eventId);
    }

    @Override
    public Event save(Event event) {
        String eventId = event.getEventId();
        if(eventId == null || eventId.length()<=0){
            eventId = UUID.randomUUID().toString();
        }
        event.setEventId(eventId);

        eventMap.put(eventId, event);
        return eventMap.get(eventId);
    }
}
