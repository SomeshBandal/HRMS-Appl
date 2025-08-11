package com.hrms.JWT;

import com.hrms.Security.CustomUserDetails;

public interface JwtService {

    /**
     * Generate a JWT token for the given email and role.
     *
     * @param email the subject of the token (usually user's email)
     * @param role the role of the user (e.g., EMPLOYEE, HR)
     * @return the signed JWT token
     */

    String generateToken(CustomUserDetails customUserDetails);

    /**
     * Extract the email (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the email (subject)
     */
    String extractEmail(String token);

    /**
     * Check whether the given token is still valid (i.e., not expired).
     *
     * @param token the JWT token
     * @return true if token is valid, false otherwise
     */
    boolean isTokenValid(String token);
}
