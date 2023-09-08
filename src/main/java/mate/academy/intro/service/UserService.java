package mate.academy.intro.service;

import mate.academy.intro.model.User;

public interface UserService {
    User findByEmail(String email);

    User save(User user);
}
