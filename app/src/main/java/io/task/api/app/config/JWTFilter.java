package io.task.api.app.config;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;

import io.task.api.app.service.PersonDetailsService;
import io.task.api.app.utils.AlgoAES;
import io.task.api.app.utils.AppConstants;
import io.task.api.app.utils.JWTUtil;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(JWTFilter.class.getName());
    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    @Autowired
    private final AlgoAES algoAes;

    @Autowired
    private JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService, AlgoAES algoAes) {
        super();
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
        this.algoAes = algoAes;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        LOGGER.info("Started authentication process");
        String authHeader = request.getHeader(AppConstants.JWT_TOKEN_HEADER);

        if (authHeader != null && !authHeader.trim().isEmpty() && authHeader.startsWith(AppConstants.TOKEN_PREFIX)) {

            String jwt = authHeader.substring(7);

            if (jwt.trim().isEmpty()) {
                LOGGER.info("JWT is empty");
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  
                response.getWriter().write(AppConstants.JSON_INVALID_JWT);
                return;
            } else {

                try {
                    String transparentUserName = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    LOGGER.info("Logged in user: " + transparentUserName);
                    UserDetails userdetails = personDetailsService.loadUserByUsername(algoAes.encrypt(transparentUserName));
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userdetails,
                            userdetails.getPassword(), userdetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException e) {
                    LOGGER.info("JWT verification exception: " + response.SC_BAD_REQUEST + " " + e.getMessage());
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  
                    response.getWriter().write(AppConstants.JSON_INVALID_JWT);
                    return;
                    
                    
                }

            }

        }
        LOGGER.info("REQUEST: " + request.getMethod() + " " + request.getRequestURL());
        LOGGER.info("JWT FILTER RESPONSE: " + response.getStatus());
        filterChain.doFilter(request, response);

    }
}
