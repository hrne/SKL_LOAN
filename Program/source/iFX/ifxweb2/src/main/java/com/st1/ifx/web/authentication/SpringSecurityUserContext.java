package com.st1.ifx.web.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUserContext implements UserContext {

	@Override
	public IfxUser getCurrentUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return null;
		}
		return (IfxUser) authentication.getPrincipal();
	}

	@Override
	public void setCurrentUser(IfxUser user) {
		// Collection authorities = IfxUserAuthorityUtils.createAuthorities(user);
		// Authentication authentication = new UsernamePasswordAuthenticationToken(
		// user, user.getPassword(), authorities);
		// SecurityContextHolder.getContext().setAuthentication(authentication);

	}

}
