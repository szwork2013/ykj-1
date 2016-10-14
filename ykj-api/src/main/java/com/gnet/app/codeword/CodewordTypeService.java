package com.gnet.app.codeword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

@Service
public class CodewordTypeService {
	
	@Autowired
	private CodewordTypeMapper codewordTypeMapper;
	
	public CodewordType findById(String typeId) {
		Example codewordType = new Example(CodewordType.class);
		codewordType.setTableName("sc_codeword_type");
		codewordType.createCriteria().andEqualTo("id", typeId);
		List<CodewordType> resultList = codewordTypeMapper.selectByExample(codewordType);
		if (resultList.isEmpty()) {
			return null;
		}
		
		return resultList.get(0);
	}
}
