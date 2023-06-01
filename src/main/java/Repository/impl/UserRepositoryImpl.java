package Repository.impl;

import Entity.User;
import Repository.UserRepository;

import java.util.HashMap;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    HashMap<String, User> userHashMap = new HashMap<>();


    @Override
    public User findUserById(String userId) {
        if(!userHashMap.containsKey(userId))    return null;
        return userHashMap.get(userId);
    }

    @Override
    public boolean isPresent(String userId) {
        return userHashMap.containsKey(userId);
    }

    @Override
    public User save(User user) {
        String userId = user.getUserId();
        if(userId == null){
            userId = UUID.randomUUID().toString();
        }
        user.setUserId(userId);

        return userHashMap.put(userId, user);
    }
}
