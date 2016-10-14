package com.gnet.app.codeword;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.utils.spring.SpringContextHolder;

public class CodewordValidator {
	
	private CodewordValidator(){}
	
	/**
	 * 保存前的验证
	 * @param area
	 * @return
	 */
	static Map<String, Object> validateBeforeCreateCodeword(Codeword codeword) {
		CodewordTypeService codewordTypeService = SpringContextHolder.getBean(CodewordTypeService.class);
		Map<String, Object> map = new HashMap<>();
		
		if (StringUtils.isBlank(codeword.getCodewordTypeId())) {
			map.put("code", CodewordErrorBuilder.ERROR_TYPEID_NULL);
			map.put("msg", "字典值类型不能为空");
			return map;
		} else if (StringUtils.isBlank(codeword.getValue())) {
			map.put("code", CodewordErrorBuilder.ERROR_VALUE_NULL);
			map.put("msg", "字典值不能为空");
			return map;
		}
		
		CodewordType codewordType = codewordTypeService.findById(codeword.getCodewordTypeId());
		if (codewordType == null) {
			map.put("code", CodewordErrorBuilder.ERROR_TYPE_NULL);
			map.put("msg", "字典类型不存在");
			return map;
		} else if (!codewordType.getPermitCustom()) {
			map.put("code", CodewordErrorBuilder.ERROR_CREATED);
			map.put("msg", "该字典类型的数据项不允许增加");
			return map;
		}
		
		return null;
	}

}
