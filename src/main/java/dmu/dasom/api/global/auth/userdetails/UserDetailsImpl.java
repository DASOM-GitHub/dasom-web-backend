package dmu.dasom.api.global.auth.userdetails;

import dmu.dasom.api.domain.common.Status;
import dmu.dasom.api.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Member member;

    @Override
    public boolean isAccountNonExpired() {
        return !this.member.getStatus().equals(Status.DELETED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.member.getStatus().equals(Status.INACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.member.getStatus().equals(Status.ACTIVE);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> member.getRole().toString());
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    @Override
    public String getUsername() {
        return this.member.getEmail();
    }
}
