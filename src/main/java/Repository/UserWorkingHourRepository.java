package Repository;

import Entity.UserWorkingHours;

import java.util.List;

public interface UserWorkingHourRepository {

    List<UserWorkingHours> getUserWorkingHours(String userId);

    UserWorkingHours save(UserWorkingHours userWorkingHours);
}
