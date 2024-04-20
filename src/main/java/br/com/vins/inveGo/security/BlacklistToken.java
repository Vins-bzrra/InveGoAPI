package br.com.vins.inveGo.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class BlacklistToken {
	private Set<String> invalidatedTokens = new HashSet<>();

	public void invalidateToken(String token) {
		invalidatedTokens.add(token);
	}

	public boolean isTokenInvalidated(String token) {
		return invalidatedTokens.contains(token);
	}
}
