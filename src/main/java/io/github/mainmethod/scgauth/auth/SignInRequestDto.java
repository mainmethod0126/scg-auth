package io.github.mainmethod.scgauth.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequestDto {

    private String id;
    private String password;

}
