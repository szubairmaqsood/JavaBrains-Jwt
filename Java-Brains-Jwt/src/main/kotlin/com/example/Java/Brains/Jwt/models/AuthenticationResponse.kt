package com.example.Java.Brains.Jwt.models

class AuthenticationResponse {
    private var jwt:String?;

    constructor(_jwt:String?){
        this.jwt = _jwt;
    }

    fun getJwt():String?{
        return this.jwt;
    }

    fun setJwt(_jwt:String?):Unit{
        this.jwt = _jwt;
    }

}