package io.github.mainmethod.scgauth.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        var savedUser = userRepository
                .save(User.builder().userId(signUpRequestDto.getId()).name(signUpRequestDto.getName())
                        .password(signUpRequestDto.getPassword()).build());

        return SignUpResponseDto.builder().id(savedUser.getUserId()).name(savedUser.getName()).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

}