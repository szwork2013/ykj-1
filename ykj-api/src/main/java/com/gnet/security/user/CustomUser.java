package com.gnet.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.app.clerk.Clerk;

import lombok.Getter;

public class CustomUser extends User {

	private static final long serialVersionUID = 5653650699969762979L;
	
	@Getter private String id;
	@Getter private SysUser user;
	@Getter private Clerk clerk;
	
	public CustomUser(String id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
	}

	public CustomUser(String id, String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
	}
	
	public CustomUser setUser(SysUser user) {
		this.user = user;
		return this;
	}
	
	public CustomUser setClerk(Clerk clerk) {
		this.clerk = clerk;
		return this;
	}
	
}
