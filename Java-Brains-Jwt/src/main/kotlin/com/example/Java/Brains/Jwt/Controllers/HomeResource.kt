package com.example.Java.Brains.Jwt.Controllers

import com.example.Java.Brains.Jwt.Services.MyUserDetailService
import com.example.Java.Brains.Jwt.Util.jwtUtil
import com.example.Java.Brains.Jwt.models.AuthenticationRequest
import com.example.Java.Brains.Jwt.models.AuthenticationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.lang.Exception

@RestController
class HomeResource {
    /*
      This member is made to provide authentication
     */
    private var authenticationManager:AuthenticationManager;
    private var userDetailsService:MyUserDetailService;
    private var jwtTokenUtil:jwtUtil;
    @Autowired
    constructor(_authenticationManager:AuthenticationManager, _userDetailsService:MyUserDetailService,_jwtTokenUtil:jwtUtil){
        this.authenticationManager = _authenticationManager;
        this.userDetailsService = _userDetailsService;
        this.jwtTokenUtil = _jwtTokenUtil;
    }

    /* Following url return Home Resource  */
    @GetMapping("/hello")
    fun home():String{
        return "Hello World";
    }

    /*
     1.Following method will be used to authenticate a user
     2.We will Authentication request in request body
     3.Request Entity can Model of Authentication response will be sent containing Authentication Response
     4.Authentication manager try to authenticate user
     5.if credentials provide in step 4 are incorrect expection will be throw of bad credentials
     6.
     */
    @PostMapping("/authenticate")
    fun createAuthenticationToken(@RequestBody authenticateRequest:AuthenticationRequest):ResponseEntity<AuthenticationResponse>{
        try{
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(authenticateRequest.getUserName(),authenticateRequest.getPassword())
            );
        }catch(e:BadCredentialsException){
            throw Exception("Incorrect user name or Passowrd",e);
        }

        val userDetails:UserDetails = this.userDetailsService.loadUserByUsername(authenticateRequest.getUserName());
        val jwt: String? = this.jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(AuthenticationResponse(jwt));

    }
}