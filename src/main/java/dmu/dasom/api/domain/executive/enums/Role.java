package dmu.dasom.api.domain.executive.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// enum + 문자열 매핑
@AllArgsConstructor
@Getter
public enum Role {

    ROLE_PRESIDENT("PRESIDENT"),                                // 회장
    ROLE_VICE_PRESIDENT("VICE_PRESIDENT"),                      // 부회장
    ROLE_TECHNICAL_MANAGER("TECHNICAL_MANAGER"),                // 기술팀장
    ROLE_ACADEMIC_MANAGER("ACADEMIC_MANAGER"),                  // 학술팀장
    ROLE_ACADEMIC_SENIOR("ACADEMIC_SENIOR"),                    // 학술차장
    ROLE_PUBLIC_RELATIONS_MANAGER("PUBLIC_RELATIONS_MANAGER"),  // 홍보팀장
    ROLE_CLERK("CLERK"),                                        // 서기
    ROLE_MANAGER("MANAGER"),                                    // 총무
    ROLE_SUB_MANAGER("SUB_MANAGER"),                            // 부총무
    ;

    private String name;

}
