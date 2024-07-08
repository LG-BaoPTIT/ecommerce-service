package com.ite.ecommerceservice.Security;

import com.ite.ecommerceservice.Entity.User;
import com.ite.ecommerceservice.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;


    private User userDetail;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getRequestURI();

        this.userDetail = userRepository.findUserByEmail(email);
        if(Objects.isNull(userDetail)) {
            throw new UsernameNotFoundException("user not found");
        }
        return CustomUserDetails.mapUserToUserDetail(userDetail);
    }



    public User getUserDetail(){
        // userDetail.setPassword(null);
        return userDetail;
    }
}
