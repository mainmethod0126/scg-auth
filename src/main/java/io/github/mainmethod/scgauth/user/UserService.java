package io.github.mainmethod.scgauth.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.mainmethod.scgauth.role.Role;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        var savedUser = userRepository
                .save(User.builder().userId(signUpRequestDto.getId()).name(signUpRequestDto.getName())
                        .password(passwordEncoder.encode(signUpRequestDto.getPassword())).lockStatus(false).roles(roles)
                        .build());

        return SignUpResponseDto.builder().id(savedUser.getUserId()).name(savedUser.getName()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUserId(username);
    }

}