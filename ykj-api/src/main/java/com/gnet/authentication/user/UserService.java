package com.gnet.authentication.user;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.app.authentication.user.UserMapper;

@Service
@Transactional(readOnly = true)
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	
	public SysUser findById(String id){
		SysUser user = new SysUser();
		user.setId(id);
		return this.find(user);
	}
	
	public SysUser findByUsername(String username) {
		SysUser user = new SysUser();
		user.setUsername(username);
		return find(user);
	}
	
	public SysUser find(SysUser user){
		return userMapper.selectOne(user);
	}
	
	public List<SysUser> findAll(SysUser user){
		return userMapper.selectAll();
	}
	
	
	
	/**
	 * 创建系统用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean create(SysUser user) {
		return userMapper.insertSelective(user) == 1;
	}
	
	/**
	 * 更新系统用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean update(SysUser user){
		return userMapper.updateByPrimaryKeySelective(user) == 1;
	}

	/**
	 * 判断用户名是否存在（增加）
	 * 
	 * @param username
	 * @return
	 */
	public Boolean isExists(String username){
		return userMapper.isCreateExists(username) > 0;
	}
	
	/**
	 * 判断用户名是否存在（编辑）
	 * 
	 * @param username
	 * @return
	 */
	public Boolean isExists(String username, String originValue){
		return userMapper.isUpdateExists(username, originValue) > 0;
	}
	
	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean delete(String id, String userId) {
		SysUser user = new SysUser();
		user.setId(id);
		user.setOperateDeleteStatus(SysUser.DEL_FALSE);
		user = find(user);
		// 无法找到判断为已经被删除
		if (user == null) {
			return true;
		}
		
		Date date = new Date();
		user = new SysUser();
		user.setId(id);
		user.setOperateDeleteStatus(SysUser.DEL_TRUE);
		user.setOperateDeleteTime(date);
		user.setOperatorDeleteUserId(userId);
		user.setModifyDate(date);
		return userMapper.updateByPrimaryKeySelective(user) > 0;
	}
	
}
