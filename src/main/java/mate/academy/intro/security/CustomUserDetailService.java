package mate.academy.intro.security;

import mate.academy.intro.model.Role;
import mate.academy.intro.repository.RoleRepository;
import mate.academy.intro.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        initRoles();
    }

    private void initRoles() {
        Role role;
        for (Role.RoleName roleName : Role.RoleName.values()) {
            if (isSavedRole(roleName)) {
                continue;
            }
            role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

    private boolean isSavedRole(Role.RoleName roleName) {
        return roleRepository.findByName(roleName).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Can't load a user"));
    }
}
