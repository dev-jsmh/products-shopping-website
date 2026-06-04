package com.devjsmh.icea.content_manager_service.auth.filters;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.devjsmh.icea.content_manager_service.auth.entities.TokenEntity;
import com.devjsmh.icea.content_manager_service.auth.repositories.TokensRepository;
import com.devjsmh.icea.content_manager_service.auth.services.JwtUtil;
import com.devjsmh.icea.content_manager_service.auth.services.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil _jwtUtil;
    private final UserDetailsServiceImpl _userDetailsServiceImpl;
    private final TokensRepository _tokensRepository;

    public JwtRequestFilter(
            JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsServiceImpl,
            TokensRepository tokensRepository) {

        this._jwtUtil = jwtUtil;
        this._userDetailsServiceImpl = userDetailsServiceImpl;
        this._tokensRepository = tokensRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        try {

            String username = this._jwtUtil.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this._userDetailsServiceImpl.loadUserByUsername(username);

                TokenEntity currentToken = this._tokensRepository
                        .findByToken(jwt).get();

                Boolean isTokenExpired = this._jwtUtil.isTokenExpired(currentToken.getToken());

                Boolean validToken = !currentToken.isRevoked() && !isTokenExpired;

                if (validToken) {

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (NoSuchElementException ex) {

            System.out.println("[TOKEN] The token was not found in the database");
            return;

        } catch (ExpiredJwtException ex) {

            System.out.println("[TOKEN] Current user has a expired token");
            return;

        } catch (MalformedJwtException ex) {

            System.out.println("[TOKEN] The token uses for the request is malformed");
            return;

        } catch (JwtException ex) {

            System.out.println("[TOKEN] The token uses for the request is invalid");
            return;

        }

        filterChain.doFilter(request, response);

    }

}
