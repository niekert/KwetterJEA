package kwetter.dao;

import kwetter.domain.User;

import java.util.List;

public interface UserDAO {

    int count();

    void create(User user);

    void edit(User user);

    List<User> findAll();

    User find(Long id);

    User findByName(String name);

    void remove(User user);

    boolean addFollowing(User invokingUser, User targetedUser);

    boolean removeFollowing(User invokingUser, User targetUser);

    User registerNewUser(String username, String email, String password);
}
