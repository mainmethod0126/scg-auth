package io.github.mainmethod.scgauth.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    private String id;
    private String name;
    private String password;
}
