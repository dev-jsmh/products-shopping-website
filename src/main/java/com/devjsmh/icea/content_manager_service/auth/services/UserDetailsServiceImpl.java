package com.devjsmh.icea.content_manager_service.auth.services;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devjsmh.icea.content_manager_service.auth.entities.UserEntity;
import com.devjsmh.icea.content_manager_service.auth.repositories.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository _usersRepository;

    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this._usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity foundUser = this._usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(foundUser.getUsername(), foundUser.getPassword(),
                new ArrayList<>());
    }

}
