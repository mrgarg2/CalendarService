package Repository.impl;

import Entity.Event;
import Entity.EventParticipants;
import Repository.EventParticipationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EventParticipationRepositoryImpl implements EventParticipationRepository {

    HashMap<String, EventParticipants> eventParticipationMap = new HashMap<>();

    @Override
    public List<Event> findEventsParticipationByUser(String userId) {
        return null;
    }

    @Override
    public EventParticipants save(EventParticipants eventParticipants) {
        String eventParticipantId = eventParticipants.getEventParticipantId();
        if(eventParticipantId == null){
            eventParticipantId = UUID.randomUUID().toString();
        }

        eventParticipants.setEventParticipantId(eventParticipantId);
        return eventParticipationMap.put(eventParticipantId, eventParticipants);
    }
}
