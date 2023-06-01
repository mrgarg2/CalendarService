package Service.impl;

import Entity.Event;
import Entity.EventParticipants;
import Entity.User;
import Entity.UserWorkingHours;
import Model.FreeTime;
import Repository.EventParticipationRepository;
import Repository.EventRepository;
import Repository.UserRepository;
import Repository.UserWorkingHourRepository;
import Service.EventService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EventServiceImpl implements EventService {

    UserRepository userRepository;
    EventRepository eventRepository;
    UserWorkingHourRepository userWorkingHourRepository;
    EventParticipationRepository eventParticipationRepository;

    public EventServiceImpl(UserRepository userRepository, EventRepository eventRepository, UserWorkingHourRepository userWorkingHourRepository, EventParticipationRepository eventParticipationRepository){
        this.userRepository = userRepository;
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventRepository = eventRepository;
        this.userWorkingHourRepository = userWorkingHourRepository;
    }

    @Override
    public Event createEvent(String createdByUserId, Date startTime, Date endTime, List<String> participantUsers) {
        //GetUser
        User user = userRepository.findUserById(createdByUserId);
        if(user == null){
            throw new RuntimeException("No Such user");
        }

        List<UserWorkingHours> userWorkingHoursList = userWorkingHourRepository.getUserWorkingHours(createdByUserId);
        Collections.sort(userWorkingHoursList, (u1, u2) ->{
            if(u1.getStartTime().before(u2.getStartTime())) return -1;
            else return 1;
        });
        if(isConflictingWorkingHours(userWorkingHoursList, startTime, endTime)){
            throw new RuntimeException("Cannot create Event due to conflict Working hours");
        }

        //Check Participant Users
        if(!validateParticipantUsers(participantUsers)){
            throw new RuntimeException("All Participant users are not valid");
        }


        Event event = new Event();
        event.setOwnerUserId(createdByUserId);
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event = eventRepository.save(event);

        for(String participantUserId: participantUsers){
            EventParticipants eventParticipants = new EventParticipants();
            eventParticipants.setUserId(participantUserId);
            eventParticipants.setEventId(event.getEventId());
            eventParticipationRepository.save(eventParticipants);
        }

        return event;
    }

    private boolean validateParticipantUsers(List<String> participantUserIdList){
        for(String userId: participantUserIdList){
            if(!userRepository.isPresent(userId)){
                return false;
            }
        }
        return true;
    }

    private boolean isConflictingWorkingHours(List<UserWorkingHours> userWorkingHoursList, Date startTime, Date endTime){
        for(UserWorkingHours userWorkingHour: userWorkingHoursList){
            if((userWorkingHour.getStartTime().before(startTime) || userWorkingHour.getStartTime().equals(startTime)) &&
                    (userWorkingHour.getEndTime().before(endTime) || userWorkingHour.getEndTime().equals(endTime))){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Event> fetchEventForUser(String userId) {
        List<Event> eventList = new ArrayList<>();

        List<Event> createdEvents = eventRepository.findEventCreatedByUser(userId);
        List<Event> eventsUserParticipating = eventParticipationRepository.findEventsParticipationByUser(userId);

        if(createdEvents!=null && createdEvents.size() > 0)
            eventList.addAll(createdEvents);

        if(eventsUserParticipating!=null && eventsUserParticipating.size() > 0)
            eventList.addAll(eventsUserParticipating);

        Collections.sort(eventList, (e1, e2) -> {
            if(e1.getStartTime().before(e2.getStartTime())) return -1;
            else return 1;
        });

        return eventList;
    }

    @Override
    public Event deleteEvent(String eventId, String userId) {
        Event event = eventRepository.findEventById(eventId);
        if(event == null){
            throw new RuntimeException("No Such Event Present");
        }

        if(event.getOwnerUserId() != userId){
            throw new RuntimeException("User is not the owner of event");
        }

        event = eventRepository.deleteEvent(eventId);
        if(event == null){
            throw new RuntimeException("Unable to Delete Event");
        }

        return event;
    }

    @Override
    public List<Event> eventsWithConflicts(String userId) {
        //Check SUer Exists

        List<Event> allUserEvents = fetchEventForUser(userId);

        Collections.sort(allUserEvents, (u1, u2) ->{
            if(u1.getStartTime().before(u2.getStartTime())) return -1;
            else return 1;
        });

        if(allUserEvents.size() <= 1)   return allUserEvents;
        System.out.println("All User Events : " + allUserEvents);

        List<Event> conflictingEvents = new ArrayList<>();
        Event prev = allUserEvents.get(0);
        for(int i=1;i<allUserEvents.size();i++){
            Event curr = allUserEvents.get(i);
            System.out.println("Prev End Time : " + prev.getEndTime());
            System.out.println("Curr Start Time : " + curr.getStartTime());
            if(prev.getEndTime().after(curr.getStartTime())){
                conflictingEvents.add(prev);
                conflictingEvents.add(curr);
            }
            prev = curr;
        }
        return conflictingEvents;
    }

    /*
    public List<FreeTime> getFavourableTimeSlot(List<String> userIdList){

        for(String userId: userIdList){
            if(!userRepository.isPresent(userId)) {
                throw new RuntimeException("User " + userId + " not present");
            }
        }

        List<FreeTime> freeTimeList = new ArrayList<>();

        for(String userId:userIdList){
            List<FreeTime> freeTimesPerUser = getFreeTimeForUser(userId);
            if(freeTimesPerUser.size() <= 0){
                return freeTimeList;
            }
            freeTimeList.addAll(freeTimesPerUser);
        }

        Collections.sort(freeTimeList, (w1, w2) -> {
            if(w1.getStartTime().before(w2.getStartTime())) return -1;
            else return 1;
        });

        O(N*E + Sorting)


        O(1)


        startTIme => 10AM, 11AM 10times


        Event:
        {
            isRecurring: true
            source: eventId
            startTime:
            endTime:
        }

    }

    private List<FreeTime> getFavourableTime(List<FreeTime> freeTimeList, int noOfUsers){
        List<FreeTime> freeTimes = new ArrayList<>();
        if(freeTimes.size() == 0)   return freeTimes;

        FreeTime prevTime = freeTimeList.get(0);

        for(int i=1;i<freeTimeList.size();i++){
            FreeTime curr = freeTimeList.get(i);
            if(prevTime.getStartTime().before(curr.getStartTime())){
                if(prevTime.getUserSet().size() == noOfUsers){
                    freeTimes.add(prevTime);
                }
            }
            else{

            }
        }

    }

    private List<FreeTime> getFreeTimeForUser(String userId){
        if(!userRepository.isPresent(userId)){
            throw new RuntimeException("User "+ userId + " not present");
        }

        List<Event> eventList = fetchEventForUser(userId);
        List<Event> mergedList = getMergeEventList(eventList);

        List<UserWorkingHours> workingHours = userWorkingHourRepository.getUserWorkingHours(userId);
        Collections.sort(workingHours, (w1, w2) -> {
            if(w1.getStartTime().before(w2.getStartTime())) return -1;
            else return 1;
        });

        List<FreeTime> freeTimeList = getFreeTime(mergedList, workingHours);
        return freeTimeList;
    }

    private List<FreeTime> getFreeTime(List<Event> mergedList, List<UserWorkingHours> workingHours){
        List<FreeTime> freeTimeList = new ArrayList<>();
        int i=0;
        int j=0;
        UserWorkingHours userWorkingHours = workingHours.get(i);
        while(j<mergedList.size()) {
            Event curr = mergedList.get(j);


            startTIme = startOFShift
            endTime = startOfEvent

            startTime = prevEndTime
            endTime = currEventStartTime

            if(userWorkingHours.getStartTime().before(curr.getStartTime())){
                FreeTime freeTime = new FreeTime();
                freeTime.setStartTime(userWorkingHours.getStartTime());
                freeTime.setEndTime(curr.getStartTime());
                freeTimeList.add(freeTime);
                j++;
            }
            else {
                i++;
                userWorkingHours = workingHours.get(i);
            }
        }
        return freeTimeList;
    }

    private List<Event> getMergeEventList(List<Event> eventList){
        List<Event> res = new ArrayList<>();

        if(eventList.size() == 0)   return eventList;

        Event prev = eventList.get(0);
        for(int i=1;i<eventList.size();i++){
            Event curr = eventList.get(i);
            if(prev.getEndTime().before(curr.getStartTime())){
                res.add(prev);
                prev = curr;
            }
            else{
                if(prev.getEndTime().before(curr.getEndTime())){
                    prev.setEndTime(curr.getStartTime());
                }
            }
        }
        res.add(prev);
        return res;
    }
     */
}
