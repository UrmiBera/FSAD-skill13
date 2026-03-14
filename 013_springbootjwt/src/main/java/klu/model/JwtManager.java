package klu.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtManager {
	public final String SECRETE_KEY = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	public final SecretKey key = Keys.hmacShaKeyFor(SECRETE_KEY.getBytes());
	
	//Generate JWT
	public String generateJWT(String username) throws Exception
	{
		Map<String, String> payload = new HashMap<String, String>();
		payload.put("username", username);
		
		return Jwts.builder()
				.claims(payload)
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + 86400000))
				.signWith(key)		
				.compact();
	}
	
	//Validate JWT
	public Map<String, String> validateJWT(String token)throws Exception
	{
		Claims claims = Jwts.parser()
							.verifyWith(key)
							.build()
							.parseSignedClaims(token)
							.getPayload();
		
		Date expiration = claims.getExpiration();
		
		Map<String, String> payload  = new HashMap<String, String>();
		if(expiration == null || expiration.before(new Date()))
			throw new Exception("Invalid Token!");
		
		payload.put("username", claims.get("username", String.class));

		return payload;
	}
}
