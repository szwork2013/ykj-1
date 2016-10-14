package com.gnet.codeword;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.gnet.app.codeword.Codeword;

@Component
public class CodewordGetter {

	@Autowired
	private RedisTemplate<String, Object> template;
	
	/**
	 * 根据数据字典类型名获取数据字典列表
	 * 
	 * @param typeName 数据字典类型名
	 * @return
	 */
	public List<Codeword> getCodewordsByTypeName(String typeName) {
		String typeId = (String) template.boundHashOps(CodewordLoader.CODEWORDS_NAMES).get(typeName);
		List<Object> codewords = template.boundHashOps(CodewordLoader.CODEWORD_TYPE_VALUES_BY_ID(typeId)).values();
		return codewords.stream().map( code -> (Codeword) code ).collect(Collectors.toList());
	}
	
	/**
	 * 根据数据字典类型值获取数据字典列表
	 * 
	 * @param typeCode
	 * @return
	 */
	public List<Codeword> getCodewordsByTypeCode(String typeCode) {
		String typeId = (String) template.boundHashOps(CodewordLoader.CODEWORDS_CODES).get(typeCode);
		List<Object> codewords = template.boundHashOps(CodewordLoader.CODEWORD_TYPE_VALUES_BY_ID(typeId)).values();
		return codewords.stream().map( code -> (Codeword) code ).collect(Collectors.toList());
	}
	
	/**
	 * 根据数据字典编号获得数据字典值
	 * 
	 * @param typeCode
	 * @param codewordId
	 * @return
	 */
	public Codeword getCodewordById(String typeCode, String codewordId) {
		String typeId = (String) template.boundHashOps(CodewordLoader.CODEWORDS_CODES).get(typeCode);
		Object codeword = template.boundHashOps(CodewordLoader.CODEWORD_TYPE_VALUES_BY_ID(typeId)).get(codewordId);
		return (Codeword) codeword;
	}
	
	/**
	 * 根据数据字典CODE获得数据字典值
	 * 
	 * @param typeCode
	 * @param codewordKey
	 * @return
	 */
	public Codeword getCodewordByKey(String typeCode, String codewordKey) {
		String typeId = (String) template.boundHashOps(CodewordLoader.CODEWORDS_CODES).get(typeCode);
		String codewordId = (String) template.boundHashOps(CodewordLoader.CODEWORD_TYPE_KEYS_BY_ID(typeId)).get(codewordKey);
		Object codeword = template.boundHashOps(CodewordLoader.CODEWORD_TYPE_VALUES_BY_ID(typeId)).get(codewordId);
		return (Codeword) codeword;
	}
	
}
