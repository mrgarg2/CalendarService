import Entity.Event;
import Entity.User;
import Entity.UserWorkingHours;
import Repository.EventParticipationRepository;
import Repository.UserRepository;
import Repository.UserWorkingHourRepository;
import Repository.impl.EventParticipationRepositoryImpl;
import Repository.impl.EventRepositoryImpl;
import Repository.impl.UserRepositoryImpl;
import Repository.impl.UserWorkingHourRepositoryImpl;
import Service.EventService;
import Service.impl.EventServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CalendarService {

    public static UserRepository userRepository = new UserRepositoryImpl();
    public static UserWorkingHourRepository userWorkingHourRepository = new UserWorkingHourRepositoryImpl();

    public static EventService eventService = new EventServiceImpl(userRepository, new EventRepositoryImpl(), userWorkingHourRepository, new EventParticipationRepositoryImpl());


    public static void main(String[] args){
        setup();

        //Adding user 2 as Participation
        List<String> participantUser = new ArrayList();
        participantUser.add("user2");

        //Creating Event by User 1
        Event event = eventService.createEvent("user1", new Date(), new Date(System.currentTimeMillis() + 1000 * 1000), participantUser);
        System.out.println(event);

        //Creating Event By User 2
        Event event2 = eventService.createEvent("user1", new Date(System.currentTimeMillis() + 100 * 1000), new Date(System.currentTimeMillis() + 2000 * 1000), new ArrayList<>(Arrays.asList("user3")));

        System.out.println("*******************************");
        System.out.println("Fetching Events for User 1");
        List<Event> events = eventService.fetchEventForUser("user1");
        System.out.println(events);


        System.out.println("Added 2 events are conflicting, which is printed in Conflicted Events ");
        List<Event> conflictingEvents = eventService.eventsWithConflicts("user1");
        System.out.println(conflictingEvents);

        System.out.println("Deleting an Event");
        eventService.deleteEvent(event.getEventId(), "user1");

        events = eventService.fetchEventForUser("user1");
        System.out.println(events);
    }

    public static void setup(){
        userRepository.save(new User("user1", "abcd"));
        userRepository.save(new User("user2", "sdefg"));
        userRepository.save(new User("user3", "ijkl"));

        UserWorkingHours userWorkingHours = new UserWorkingHours();
        userWorkingHours.setUserId("user1");
        userWorkingHours.setStartTime(new Date(System.currentTimeMillis() - 3600 * 1000));
        userWorkingHours.setEndTime(new Date(System.currentTimeMillis() + 3600 * 1000));
        userWorkingHourRepository.save(userWorkingHours);


        UserWorkingHours userWorkingHours2 = new UserWorkingHours();
        userWorkingHours2.setUserId("user2");
        userWorkingHours2.setStartTime(new Date(System.currentTimeMillis() - 3600 * 1000));
        userWorkingHours2.setEndTime(new Date(System.currentTimeMillis() + 3600 * 1000));
        userWorkingHourRepository.save(userWorkingHours2);

        UserWorkingHours userWorkingHours3 = new UserWorkingHours();
        userWorkingHours3.setUserId("user3");
        userWorkingHours3.setStartTime(new Date(System.currentTimeMillis() - 3600 * 1000));
        userWorkingHours3.setEndTime(new Date(System.currentTimeMillis() + 3600 * 1000));
        userWorkingHourRepository.save(userWorkingHours3);
    }
}
