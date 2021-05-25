package com.example.Java.Brains.Jwt.Security

import com.example.Java.Brains.Jwt.Services.MyUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
class SecurityConfigurer:WebSecurityConfigurerAdapter() {
    lateinit  var myUserDetailService: MyUserDetailService;
    /*
     We are autowiring UserDetailService with our Security Configuration Adapter
     */
    @Autowired
    fun constructor(_myUserDetailService:MyUserDetailService){
        this.myUserDetailService = _myUserDetailService
    }
    /* In this method Authentication Manager is attaching user detail service       */
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(this.myUserDetailService);
    }
    /*
    This method is used to configure authorization
     */

    override fun configure(http: HttpSecurity?) {
        http?.csrf()?.disable()
            ?.authorizeRequests()?.antMatchers("/authenticate")?.permitAll()
            ?.anyRequest()?.authenticated();
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