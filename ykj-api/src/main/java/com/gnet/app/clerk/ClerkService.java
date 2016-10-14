package com.gnet.app.clerk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.authentication.user.SysUser;
import com.gnet.app.authentication.user.UserMapper;
import com.gnet.app.office.Office;
import com.gnet.app.office.OfficeMapper;
import com.gnet.utils.date.DateUtil;
import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class ClerkService {
	
	@Autowired
	private ClerkMapper clerkMapper;
	@Autowired
	private OfficeMapper officeMapper;
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * 返回某个未删除的员工
	 * @param id
	 * @return
	 */
	public Clerk findById(String id) {
		return clerkMapper.selectByPrimaryKey(id);
	}
	
	public Clerk find(Clerk clerk) {
		return clerkMapper.selectOne(clerk);
	}
	
	public List<Clerk> findClerksUnderBusiness(String businessId, String name, List<String> orderList) {
		return clerkMapper.selectClerksUnderBusiness(name, businessId);
	}
	
	public List<Clerk> findClerksUnderStore(String storeId, String name, List<String> orderList) {
		return clerkMapper.selectClerksUnderStore(name, storeId);
	}
	
	public List<Clerk> findClerksUnderOffice(String officeId) {
		return clerkMapper.selectClerksListUnderOffice(officeId);
	}
	
	public Page<Clerk> paginationClerksUnderBusiness(Pageable pageable, String businessId, String name, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Clerk>() {
			
			@Override
			public List<Clerk> getPageContent() {
				return clerkMapper.selectClerksUnderBusiness(name, businessId);
			}
			
		});
	}
	
	/**
	 * 返回所有未删除的员工
	 * @return
	 */
	public List<Clerk> findAll(){
		return clerkMapper.findAll();
	}

	
	public Page<Clerk> paginationClerksUnderStore(Pageable pageable, String storeId, String name, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Clerk>() {
			
			@Override
			public List<Clerk> getPageContent() {
				return clerkMapper.selectClerksUnderStore(name, storeId);
			}
			
		});
	}
	
	public Page<Clerk> paginationEmpty(Pageable pageable) {
		return PageUtil.pagination(pageable, new PageUtil.Callback<Clerk>() {
			
			@Override
			public List<Clerk> getPageContent() {
				return new ArrayList<>();
			}
			
		});
	}
	
	@Transactional(readOnly = false)
	public Boolean create(Clerk clerk, Map<String, Object> extraMap) {
		Date date = new Date();
		clerk.setCreateDate(date);
		clerk.setModifyDate(date);
		clerk.setIsDel(Clerk.DEL_FALSE);
		Office office = officeMapper.selectByPrimaryKey(clerk.getOfficeId());
		
		if (office == null) {
			return false;
		}
		
		// 根据部门不同层级获取不同的上级部门
		if (office.getLevel() == 0 && Office.TYPE_BUSINESS.equals(office.getType())) {
			clerk.setBusinessId(office.getId());
		} else if (office.getLevel() == 1 && Office.TYPE_DEPARTMENT.equals(office.getType())) {
			clerk.setBusinessId(office.getParentid());
		} else if (office.getLevel() == 1 && Office.TYPE_STORE.equals(office.getType())) {
			clerk.setBusinessId(office.getParentid());
			clerk.setStoreId(office.getId());
		} else if (office.getLevel() == 2 && Office.TYPE_DEPARTMENT.equals(office.getType())) {
			clerk.setStoreId(office.getParentid());
			Office officeStore = new Office();
			officeStore.setParentid(office.getParentid());
			officeStore = officeMapper.selectOne(officeStore);
			clerk.setBusinessId(officeStore.getParentid());
		} else {
			return false;
		}
		
		if (clerkMapper.insertSelective(clerk) != 1) {
			return false;
		}
		
		// 新增系统用户
		SysUser sysUser = new SysUser();
		sysUser.setUsername(extraMap.get("username").toString());
		sysUser.setName(extraMap.get("name").toString());
		sysUser.setPassword(SysUser.PASSWORD_ENCODER.encode(extraMap.get("password").toString()));
		sysUser.setRoleType(SysUser.TYPE_CLERK);
		sysUser.setStatus(SysUser.STATUS_ENABLED);
		sysUser.setOperateDeleteStatus(SysUser.DEL_FALSE);
		sysUser.setCreateDate(date);
		sysUser.setModifyDate(date);
		sysUser.setId(clerk.getId());
		if (userMapper.insertClerkUser(sysUser) != 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	 
	@Transactional(readOnly = false)
	public Boolean update(Clerk clerk) {
		clerk.setModifyDate(new Date());
		return clerkMapper.updateByPrimaryKeySelective(clerk) == 1;
	}
	
	
	@Transactional(readOnly = false)
	public Boolean delete(String id) {
		return clerkMapper.softDeleteById(id, new Date()) == 1;
	}
	
	public Boolean belongBusiness(Integer roleType) {
		return Clerk.ROLE_TYPE_ADMIN.equals(roleType) 
				|| Clerk.ROLE_TYPE_MANAGER.equals(roleType) 
				|| Clerk.ROLE_TYPE_LOGISTIC.equals(roleType)
				|| Clerk.ROLE_TYPE_AFTER_SALES.equals(roleType)
				|| Clerk.ROLE_TYPE_WAREHOUSE.equals(roleType);
		
	}
	
	public Boolean belongStore(Integer roleType) {
		return Clerk.ROLE_TYPE_CLERK.equals(roleType) 
				|| Clerk.ROLE_TYPE_STORE_MANAGER.equals(roleType);
		
	}
	
	public Clerk mapToClerk(Map<String, Object> map) {
		Clerk clerk = new Clerk();
		clerk.setRoleType(map.get("roleType") == null ? null : Integer.valueOf(map.get("roleType").toString()));
		clerk.setOfficeId(map.get("officeId") == null ? null : map.get("officeId").toString());
		clerk.setName(map.get("name") == null ? null : map.get("name").toString());
		clerk.setSex(map.get("sex") == null ? null : Integer.valueOf(map.get("sex").toString()));
		clerk.setBirthday(map.get("birthday") == null ? null : DateUtil.stringtoDate(map.get("birthday").toString(), "yyyy-MM-dd"));
		clerk.setPhone(map.get("phone") == null ? null : map.get("phone").toString());
		clerk.setPhoneSec(map.get("phoneSec") == null ? null : map.get("phoneSec").toString());
		clerk.setQQ(map.get("QQ") == null ? null : map.get("QQ").toString());
		clerk.setWechat(map.get("wechat") == null ? null : map.get("wechat").toString());
		clerk.setEmail(map.get("email") == null ? null : map.get("email").toString());
		clerk.setPhoto(map.get("photo") == null ? null : map.get("photo").toString());
		return clerk;
	}

	public boolean businessExistClerk(String id) {
		return clerkMapper.businessExistClerk(id) > 0;
	}

	public boolean storeExistClerk(String id) {
		return clerkMapper.storeExistClerk(id) > 0;
	}
	
	
	
}