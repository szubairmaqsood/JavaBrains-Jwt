package com.example.Java.Brains.Jwt.filters

import com.example.Java.Brains.Jwt.Services.MyUserDetailService
import com.example.Java.Brains.Jwt.Util.jwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter:OncePerRequestFilter {

    private var userDetailsService:MyUserDetailService;
    private var jwtUtil: jwtUtil;
    @Autowired
    constructor(_userDetailsService:MyUserDetailService,_jwtUtil:jwtUtil){
                this.userDetailsService = _userDetailsService;
                this.jwtUtil = _jwtUtil;
    }
    /*
    1.It has request
    2.It has response
    3.It has chain of forwarding filters
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        /*
        In header of request we will pass jst with key name as Authorization
        value structure is "Bearer {value of Jwt}"
         */
     val authorizationHeader:String = request.getHeader("Authorization");

        var userName:String?=null;
        var jwt:String?=null;
        /*
         Extract jwt
         Extract username from jst
         */
        if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            userName = this.jwtUtil.extractUsername(jwt);
        }

        /*
        If user name is there get UserDetails of user with given username
        and also SecuityContext no user is authenticated
         */
        if(userName!=null && SecurityContextHolder.getContext().getAuthentication() ==null){
            val userDetails:UserDetails = this.userDetailsService.loadUserByUsername(userName);
            /*
            If user jwt is value make a user Details
             */
            if(jwtUtil.validateToken(jwt,userDetails)){
               val  userNamePasswordAuthenticationToken:UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                   userDetails,null,userDetails.authorities
               );
                //There were things were automatically set by Spring secuity in past
                var _webAuthenticationDetailsSource:WebAuthenticationDetailsSource = WebAuthenticationDetailsSource();
                _webAuthenticationDetailsSource.buildDetails(request);
                userNamePasswordAuthenticationToken.details =_webAuthenticationDetailsSource;
                SecurityContextHolder.getContext().authentication =  userNamePasswordAuthenticationToken;
            }
        }
        /*
        I have done my work so pass it on to next filters
         */
        filterChain.doFilter(request,response);

    }
}