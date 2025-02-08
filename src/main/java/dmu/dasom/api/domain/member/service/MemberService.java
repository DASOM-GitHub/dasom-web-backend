package dmu.dasom.api.domain.member.service;

import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.entity.Member;

public interface MemberService {

    Member getMemberByEmail(final String email);

    boolean checkByEmail(final String email);

    void signUp(final SignupRequestDto request);

}
