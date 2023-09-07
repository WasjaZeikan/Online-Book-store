package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
