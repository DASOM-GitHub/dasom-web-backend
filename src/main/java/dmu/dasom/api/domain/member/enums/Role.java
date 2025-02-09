package dmu.dasom.api.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    ROLE_MEMBER("MEMBER"),
    ROLE_ADMIN("ADMIN"),
    ;

    private final String name;

}
