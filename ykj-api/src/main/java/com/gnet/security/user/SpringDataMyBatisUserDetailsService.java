package com.gnet.security.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.gnet.app.authentication.role.Role;
import com.gnet.app.authentication.user.SysUser;
import com.gnet.app.authentication.user.UserMapper;
import com.gnet.app.clerk.Clerk;
import com.gnet.app.clerk.ClerkMapper;
import com.gnet.app.clerk.ClerkNotFoundException;

@Component
public class SpringDataMyBatisUserDetailsService implements UserDetailsService {
	
	private final UserMapper userMapper;
	
	private final ClerkMapper clerkMapper;
	
	@Autowired
	public SpringDataMyBatisUserDetailsService(UserMapper userMapper, ClerkMapper clerkMapper) {
		this.userMapper = userMapper;
		this.clerkMapper = clerkMapper;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		SysUser user = new SysUser(username);
		user = userMapper.selectOne(user);
		
		if (user == null) {
			throw new UsernameNotFoundException("Given username does not match sysuser.");
		}
		
		Clerk clerk = clerkMapper.selectByPrimaryKey(user.getId());
		if (clerk == null) {
			throw new ClerkNotFoundException("Couldn't found clerk in system");
		}
		
		List<Role> roles = userMapper.selectRoles(user.getId());
		
		List<String> permissions = new ArrayList<>();
		for (Role role : roles) {
			permissions.addAll(CollectionUtils.arrayToList(role.getPermissions()));
		}
		
		return new CustomUser(user.getId(), username, user.getPassword(), AuthorityUtils.createAuthorityList(permissions.toArray(new String[]{})))
					.setUser(user)
					.setClerk(clerk);
		
	}

}
