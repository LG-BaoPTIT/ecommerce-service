package com.ite.ecommerceservice.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ite.ecommerceservice.Entity.EStatus;
import com.ite.ecommerceservice.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private String userId;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
    private String phone;
    private EStatus status;

    private GrantedAuthority authority;

    public CustomUserDetails() {
    }


    public static CustomUserDetails mapUserToUserDetail(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        return new CustomUserDetails(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getStatus(),
                authority
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(this.authority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.status.equals(EStatus.PENDING_APPROVAL);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.status.equals(EStatus.LOCK);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(EStatus.ACTIVE);

    }


}
