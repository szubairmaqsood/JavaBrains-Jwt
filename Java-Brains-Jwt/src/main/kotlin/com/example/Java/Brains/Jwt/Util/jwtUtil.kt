package com.example.Java.Brains.Jwt.Util
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.security.auth.Subject.getSubject
import kotlin.collections.HashMap

@Service
class jwtUtil {
    // Below here is the secret key
    private val SECRET_KEY = "secret"

    /*
     1.This method gets jwt token and extract username from it
     2.It is calling extract claim method
     */
    fun extractUsername(token: String?): String {
        return extractClaim(token, Claims::getSubject)
    }

    /*
     1.This method gets token and extract expiration date from it
     2.It is calling extract claim method
     */
    fun extractExpiration(token: String?): Date {
        return extractClaim<Date>(token, Claims::getExpiration)
    }

    /*
     This is method used by above methods
     */
    fun <T> extractClaim(token: String?, claimsResolver: (Claims)-> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody()
    }

    /*
     1.This method checks if a token has expired or not
     2.Firstly it gets expiration date from token by call method
     3.Then using before method it make sures expiration date is before current date
     */
    private fun isTokenExpired(token: String?): Boolean? {
        return extractExpiration(token).before(Date())
    }

    /*
    1.It is the method that generates Jwt Token
    2.UserDetail object is got from UserDetailService
    3.Claim is jst an empty map
    4.In the last Create token method is called
     */
    fun generateToken(userDetails: UserDetails): String? {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userDetails.username)
    }
    /*
    In this method will be actually created
    1. Used Passed map and name of user in builder Pattern
    2. Issue date and expiry date is set
    3.In the last this payload is signed with particular algorithem and secret key and returned in form of string
     */

    private fun createToken(claims: Map<String, Any>, subject: String): String? {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact()
    }

    /*
     1.This method is used to validate token
     2.This method compare the user name present in token and the username present in userDetails Object
     3.It is also checked that token is expired or not
     */

    fun validateToken(token: String?, userDetails: UserDetails): Boolean? {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)!!
    }
}