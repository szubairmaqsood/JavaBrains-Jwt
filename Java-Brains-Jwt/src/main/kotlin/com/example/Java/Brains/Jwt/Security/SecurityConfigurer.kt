package com.example.Java.Brains.Jwt.Security

import com.example.Java.Brains.Jwt.Services.MyUserDetailService
import com.example.Java.Brains.Jwt.filters.JwtRequestFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class SecurityConfigurer:WebSecurityConfigurerAdapter() {
    lateinit  var myUserDetailService: MyUserDetailService;
    lateinit var jwtRequestFilter: JwtRequestFilter;
    /*
     We are autowiring UserDetailService with our Security Configuration Adapter
     */
    @Autowired
    fun constructor(_myUserDetailService:MyUserDetailService,_jwtRequestFilter:JwtRequestFilter){
        this.myUserDetailService = _myUserDetailService
        this.jwtRequestFilter = _jwtRequestFilter;
    }
    /* In this method Authentication Manager is attaching user detail service       */
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(this.myUserDetailService);
    }
    /*
    This method is used to configure authorization
    On first line csrf is disabled
    "authentication" url will be allowd to every one
    As per 5th line no session will be managed on server
    As per 6th line we are adding our filter
     */

    override fun configure(http: HttpSecurity?) {
        http?.csrf()?.disable()
            ?.authorizeRequests()?.antMatchers("/authenticate")?.permitAll()
            ?.anyRequest()?.authenticated()
            ?.and()?.sessionManagement()
            ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http?.addFilterBefore(this.jwtRequestFilter,UsernamePasswordAuthenticationFilter::class.java);
    }

    /*
    This method is used to get bean of authentication manager
     */
    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    /*Following is the bean for Password Encoder     */
    @Bean
    fun getPasswordEncoder():PasswordEncoder{
        return NoOpPasswordEncoder.getInstance();
    }

}