package Repository;

import Entity.User;

public interface UserRepository {

    User findUserById(String userId);

    boolean isPresent(String userId);

    User save(User user);
}
