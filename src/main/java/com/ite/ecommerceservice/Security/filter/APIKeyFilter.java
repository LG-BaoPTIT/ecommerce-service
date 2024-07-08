package com.ite.ecommerceservice.Security.filter;

import com.ite.ecommerceservice.cachedrequest.CachedBodyHttpServletRequest;
import com.ite.ecommerceservice.utils.SignatureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Configuration
public class APIKeyFilter extends OncePerRequestFilter {
    private static final List<String> protectedUris = Arrays.asList(
            "/ecommerce/order/getOrder",
            "/ecommerce/order/test",
            "/api/protected3"
    );
    @Autowired
    private SignatureUtil signatureUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if(protectedUris.contains(uri)){

            if(signatureUtil.verifySignature(request)){
                filterChain.doFilter(request, response);
            }else{
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid signature");
            }

//            if(signatureUtil.verifySignature(re))
        }else {
            filterChain.doFilter(request, response);
        }
    }
}
