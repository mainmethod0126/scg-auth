package io.github.mainmethod.scgauth.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponseDto {

    private String id;
    private String name;

    @Builder
    public SignUpResponseDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
