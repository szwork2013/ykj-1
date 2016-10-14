package com.gnet.app.authentication.user;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gnet.app.authentication.role.Role;

import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<SysUser> {
	
	
	/**
	 * 获得系统用户所有角色信息
	 * 
	 * @param userId
	 * @return
	 */
	@Select(
		"select sr.id, sr.name, sr.permissions, sr.description from sys_user_role sur " +
		"left join sys_role sr on sr.id=sur.role_id " +
		"where sur.user_id=#{id} "
	)
	public List<Role> selectRoles(@Param("id") String userId);
	
	/**
	 * 验证用户名唯一性（增加）
	 * 
	 * @param username
	 * @return
	 */
	@Select(
		"select count(1) from sys_user su where su.username=#{username}"
	)
	public Integer isCreateExists(@Param("username") String username);
	
	/**
	 * 验证用户名唯一性（编辑）
	 * 
	 * @param username
	 * @param originValue
	 * @return
	 */
	@Select(
		"select count(1) from sys_user su where su.username=#{username} and su.username!=#{originValue}"
	)
	public Integer isUpdateExists(@Param("username") String username, @Param("originValue") String originValue);
	
	@Insert(
		"insert into sys_user (id, username, name, password, role_type, status, operate_delete_status, create_date, modify_date) "
		+ "values (#{sysUser.id}, #{sysUser.username}, #{sysUser.name}, #{sysUser.password}, #{sysUser.roleType}, #{sysUser.status}, #{sysUser.operateDeleteStatus}, #{sysUser.createDate}, #{sysUser.modifyDate})"
	)
	public Integer insertClerkUser(@Param("sysUser") SysUser sysUser);
}
