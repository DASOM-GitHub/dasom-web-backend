package dmu.dasom.api.domain.member.service;

import dmu.dasom.api.domain.member.dto.SignupRequestDto;
import dmu.dasom.api.domain.member.entity.Member;
import dmu.dasom.api.global.auth.dto.TokenBox;
import dmu.dasom.api.global.auth.userdetails.UserDetailsImpl;

public interface MemberService {

    Member getMemberByEmail(final String email);

    boolean checkByEmail(final String email);

    void signUp(final SignupRequestDto request);

    TokenBox tokenRotation(final UserDetailsImpl userDetails);

}
