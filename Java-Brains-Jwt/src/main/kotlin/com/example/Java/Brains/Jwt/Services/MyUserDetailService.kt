package com.example.Java.Brains.Jwt.Services

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetailService:UserDetailsService {
    /*Whatever user name is we are retuning foo user      */
    override fun loadUserByUsername(username: String?): UserDetails {
        return User("foo","foo",ArrayList());
    }
}