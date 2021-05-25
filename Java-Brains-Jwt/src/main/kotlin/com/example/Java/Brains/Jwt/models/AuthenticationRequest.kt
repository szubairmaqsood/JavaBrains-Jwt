package com.example.Java.Brains.Jwt.models
/*
   1.This is model for request which will be sent for the first time
   2.In First Request user will send UserName and Password
 */
class AuthenticationRequest {
    private var userName:String;
    private var password:String;

    /*
     Following are the constructors
     */
    constructor(){
        userName = "";
        password = "";
    }
    constructor(_userName:String,_password:String){
        this.userName = _userName;
        this.password = _password;
    }

    //Following are getter and setters for preperties
    fun getUserName():String{
        return this.userName;
    }

    fun setUserName(_userName:String):Unit{
        this.userName = _userName;
    }

    fun getPassword():String{
        return this.password;
    }

    fun setPassword(_password:String):Unit{
        this.password = _password;
    }



}