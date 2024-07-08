package com.ite.ecommerceservice.Security.filter;


import com.ite.ecommerceservice.Entity.User;
import com.ite.ecommerceservice.Repository.UserRepository;
import com.ite.ecommerceservice.Security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
public class CustomFilter extends OncePerRequestFilter {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RestTemplate restTemplate;

    private Claims claims = null;
    private String userName = null;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Optional<String> authorizationHeader = Optional.ofNullable(request.getHeader("Authorization"));
        if(authorizationHeader.isPresent() && authorizationHeader.get().startsWith("Bearer ")){

            String email = request.getHeader("email");


            User user = userRepository.findUserByEmail(email);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }


    public String getCurrentUser(){

        return this.userName;
    }

}















//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if(request.getServletPath().matches("/user/login|/user/signup|/user/forgotPassword")){
//            filterChain.doFilter(request,response);
//        }else{
//
//            String authorizationHeader = request.getHeader("Authorization");
//            String token = null;
//
//            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
//                token = authorizationHeader.substring(7);
//                userName = jwtUtil.extractUsername(token);
//                claims = jwtUtil.extractAllClaims(token);
//            }
//            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
//                if(jwtUtil.validateToken(token)){
//                    System.out.println("Roles from token: " + claims.get("role"));
//                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//
//                    usernamePasswordAuthenticationToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                }
//            }
//            filterChain.doFilter(request,response);
//        }
//
//
//    }
//    public boolean isSystemRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_SYSTEM.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isManagerRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_MANAGER.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isStaffRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_STAFF.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isClientRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_CLIENT.name().equalsIgnoreCase(roleName);
//    }
//
//    public boolean isGuestRole() {
//        String roleName = (String) claims.get("role");
//        return ERole.ROLE_GUEST.name().equalsIgnoreCase(roleName);
//    }
//