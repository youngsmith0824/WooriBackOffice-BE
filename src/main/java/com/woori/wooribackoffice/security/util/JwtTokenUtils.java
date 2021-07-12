package com.woori.wooribackoffice.security.util;

import com.woori.wooribackoffice.domain.entity.User;
import com.woori.wooribackoffice.security.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class JwtTokenUtils {
    /**
     * Generate enough secure random keys to be suitable for signature compliance
     */
    private static final byte[] API_KEY_SECRET_BYTES = DatatypeConverter.parseBase64Binary(SecurityConstants.JWT_SECRET_KEY);
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(API_KEY_SECRET_BYTES);

    private static final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build();

    public static String createToken(final User user, final boolean isRememberMe) {
        final long expiration = isRememberMe ? SecurityConstants.EXPIRATION_REMEMBER : SecurityConstants.EXPIRATION;
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        final List<String> roles = user.getRoles()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String tokenPrefix = Jwts.builder()
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .claim(SecurityConstants.ROLE_CLAIMS, String.join(",", roles))
                .setId(user.getId().toString())
                .setIssuer("SnailClimb")
                .setIssuedAt(createdDate)
                .setSubject(user.getUserName())
                .setExpiration(expirationDate)
                .compact();

        return SecurityConstants.TOKEN_PREFIX + tokenPrefix; // Add the token prefix "Bearer "
    }

    /**
     * 토큰을 파싱하여 id 추출
     */
    public static String getId(final String token) {
        return jwtParser.parseClaimsJws(token).getBody().getId();
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        String userName = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userName, token, getAuthorities(claims));
    }

    private static List<SimpleGrantedAuthority> getAuthorities(final Claims claims) {
        String role = (String) claims.get(SecurityConstants.ROLE_CLAIMS);
        return Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
