package com.team.playmatebackend.domain.user.dto;

import com.team.playmatebackend.domain.user.entity.enums.AgeGroup;
import com.team.playmatebackend.domain.user.entity.enums.Gender;
import com.team.playmatebackend.domain.user.entity.enums.PreferCategory;
import com.team.playmatebackend.domain.user.entity.enums.UserRoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserCreateRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "비밀번호는 8~16자이며, 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    @NotBlank(message = "닉네임은 필수입니댜.")
    private String nickname;

    @NotBlank(message = "이메일 입력은 필수입니다.")
    private String email;


    private Gender gender;
    private PreferCategory preferCategory;
    private AgeGroup age;

    private UserRoleType roleType;
}
