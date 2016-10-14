package com.gnet.app.noticeMsg;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoticeMsgService {
	
	@Autowired
	private NoticeMsgMapper noticeMsgMapper;
	
	public NoticeMsg findById(String id) {
		return noticeMsgMapper.selectByPrimaryKey(id);
	}
	
	public NoticeMsg find(NoticeMsg noticeMsg) {
		return noticeMsgMapper.selectOne(noticeMsg);
	}
	
	@Transactional(readOnly = false)
	public Boolean create(NoticeMsg noticeMsg) {
		return noticeMsgMapper.insertSelective(noticeMsg) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(NoticeMsg noticeMsg) {
		return noticeMsgMapper.updateByPrimaryKeySelective(noticeMsg) == 1;
	}
	
	
	@Transactional(readOnly = false)
	public Boolean delete(String id, Date date) {
		return noticeMsgMapper.deleteById(id, date) == 1;
	}

	public NoticeMsg findByFromId(String id) {
		return noticeMsgMapper.findByFromId(id);
	}
	
}