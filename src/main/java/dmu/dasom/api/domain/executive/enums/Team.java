package dmu.dasom.api.domain.executive.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Team {
    PRESIDENT("president"),
    TECH("tech"),
    ACADEMIC("academic"),
    MANAGEMENT("management"),
    PR("pr")
    ;

    private String name;
}
