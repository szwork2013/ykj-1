package com.gnet.authentication.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.security.user.CustomUser;

import lombok.Getter;

public class UserResource extends ResourceSupport {

	@Getter private Map<String, Object> user;
	
	public UserResource(CustomUser customUser, Iterable<Link> links) {
		this(customUser);
		this.add(links);
	}
	
	public UserResource(CustomUser customUser, Link... links) {
		this(customUser);
		this.add(links);
	}
	
	private UserResource(CustomUser customUser) {
		this.user = new HashMap<>();
		SysUser user = customUser.getUser();
		this.user.put("name", user.getName());
		this.user.put("id", user.getId());
		this.user.put("roleType", user.getRoleType());
		this.user.put("username", user.getUsername());
		this.user.put("nickName", user.getNickname());
	}
	
	@Override
	public String toString() {
		return String.format("Resource { content: %s, %s }", this.user, super.toString());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceSupport#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || !obj.getClass().equals(getClass())) {
			return false;
		}

		UserResource that = (UserResource) obj;

		boolean contentEqual = this.user == null ? that.user == null : this.user.equals(that.user);
		return contentEqual ? super.equals(obj) : false;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceSupport#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = super.hashCode();
		result += user == null ? 0 : 17 * user.hashCode();
		return result;
	}
	
}
