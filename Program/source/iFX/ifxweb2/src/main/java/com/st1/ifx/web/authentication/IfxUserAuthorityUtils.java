package com.st1.ifx.web.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class IfxUserAuthorityUtils {
	private static final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
	private static final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_USER");

	public static Collection<? extends GrantedAuthority> createAuthorities(IfxUser user) {

		if (user.userId.endsWith("admin")) {
			return ADMIN_ROLES;
		}
		return USER_ROLES;
	}
}
