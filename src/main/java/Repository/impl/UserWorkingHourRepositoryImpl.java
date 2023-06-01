package Repository.impl;

import Entity.UserWorkingHours;
import Repository.UserWorkingHourRepository;
import jdk.nashorn.api.tree.ArrayLiteralTree;

import java.util.*;

public class UserWorkingHourRepositoryImpl implements UserWorkingHourRepository {

    HashMap<String, UserWorkingHours> userWorkingHourMap = new HashMap<>();


    @Override
    public List<UserWorkingHours> getUserWorkingHours(String userId) {
        ArrayList<UserWorkingHours> userWorkingHours = new ArrayList<>();

        for(String userWorkingId: userWorkingHourMap.keySet()){
            if(userWorkingHourMap.get(userWorkingId).getUserId().equals(userId)){
                userWorkingHours.add(userWorkingHourMap.get(userWorkingId));
            }
        }

        return userWorkingHours;
    }

    @Override
    public UserWorkingHours save(UserWorkingHours userWorkingHours) {
        String userWorkingHourId = userWorkingHours.getWorkingHourId();
        if(userWorkingHourId == null){
            userWorkingHourId = UUID.randomUUID().toString();
        }
        userWorkingHours.setWorkingHourId(userWorkingHourId);
        userWorkingHourMap.put(userWorkingHourId, userWorkingHours);
        return userWorkingHourMap.get(userWorkingHourId);
    }
}
