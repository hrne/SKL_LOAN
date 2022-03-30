package com.st1.ifx.web.authentication;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class HostAuthenticationProvider implements AuthenticationProvider {
	private static final Logger logger = LoggerFactory.getLogger(HostAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String userId = token.getName();
		if (!userId.startsWith("baga")) {
			throw new UsernameNotFoundException("Invalid userId");
		}
		String passstr = "baga";
		if (!passstr.equals(token.getCredentials())) {
			throw new BadCredentialsException("Invalid password");
		}
		IfxUser user = new IfxUser(); // build from attach tota
		user.userId = userId;
		// user.password = password;
		user.userName = "笨蛋";
		user.level = "3";

		IfxUserDetails userDetails = new IfxUserDetails(user);
		logger.info("userDetails" + userDetails);
		Collection<? extends GrantedAuthority> authorities = IfxUserAuthorityUtils.createAuthorities(user);

		return new UsernamePasswordAuthenticationToken(userDetails, passstr, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
