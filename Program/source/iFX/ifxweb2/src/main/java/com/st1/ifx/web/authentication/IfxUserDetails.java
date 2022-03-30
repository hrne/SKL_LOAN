package com.st1.ifx.web.authentication;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class IfxUserDetails extends IfxUser implements UserDetails {
	private static final Logger logger = LoggerFactory.getLogger(IfxUserDetails.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -5039926992558472223L;

	public IfxUserDetails() {
	}

	public IfxUserDetails(IfxUser ifxUser) {
		logger.info("constructor, ifxUser-->ifxUserDetails:" + ifxUser.toString());
		setUserId(ifxUser.userId);
		// setPassword(ifxUser.password);
		userName = ifxUser.userName;
		level = ifxUser.level;

		logger.info("constructor, ifxUser-->ifxUserDetails this:" + this.toString());
	}

	private String hi = "hi!!!!";

	public String getHi() {
		return hi;
	}

	public void setHi(String hi) {
		this.hi = hi;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return IfxUserAuthorityUtils.createAuthorities(this);
	}

	@Override
	public String getUsername() {
		return this.userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String toString() {
		return "IfxUserDetails [userId=" + userId + ", name=" + userName + ", level=" + level + "]";
	}

}
