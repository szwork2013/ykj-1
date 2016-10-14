package com.gnet.app.office;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OfficeService {

	@Autowired
	private OfficeMapper officeMapper;

	/**
	 * 商家或门店下是否有部门
	 * @param id
	 * @return
	 */
	public boolean existRelation(String id) {
		return officeMapper.existRelation(id) > 0;
	}
}
