package com.gnet.codeword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.gnet.app.codeword.Codeword;
import com.gnet.app.codeword.CodewordMapper;
import com.gnet.app.codeword.CodewordType;
import com.gnet.app.codeword.CodewordTypeMapper;

import tk.mybatis.mapper.entity.Example;

/**
 * 数据字典加载器
 * 
 * @author xuq
 * @date 2016年9月13日
 * @version 1.0
 */
@Component
public class CodewordLoader implements CommandLineRunner {
	
	@Autowired
	private RedisTemplate<String, Object> template;
	@Autowired
	private CodewordTypeMapper codewordTypeMapper;
	@Autowired
	private CodewordMapper codewordMapper;
	
	static final String CODEWORDS = "codewords";
	static final String CODEWORDS_CODES = String.format("%s:codes", CODEWORDS);
	static final String CODEWORDS_NAMES = String.format("%s:names", CODEWORDS);
	
	static final String VALUES_SUFFIX = "values";
	static final String KEYS_SUFFIX = "codes";
	
	static String CODEWORD_TYPE_PREFIX_BY_ID(String typeId) {
		return String.format("%s:%s", CODEWORDS, typeId);
	}
	
	static String CODEWORD_TYPE_VALUES_BY_ID(String typeId) {
		return String.format("%s:%s", CODEWORD_TYPE_PREFIX_BY_ID(typeId), VALUES_SUFFIX);
	}
	
	static String CODEWORD_TYPE_KEYS_BY_ID(String typeId) {
		return String.format("%s:%s", CODEWORD_TYPE_PREFIX_BY_ID(typeId), KEYS_SUFFIX);
	}

	@Override
	public void run(String... args) throws Exception {
		// 银空间项目数据字典
		Example codewordExample = new Example(Codeword.class);
		codewordExample.setTableName("sc_codeword");
		List<Codeword> scCodewords = codewordMapper.selectByExample(codewordExample);
		
		Example codewordTypeExample = new Example(CodewordType.class);
		codewordTypeExample.setTableName("sc_codeword_type");
		List<CodewordType> scCodewordTypes = codewordTypeMapper.selectByExample(codewordTypeExample);
		
		// 加载至Redis缓存内
		loopIntoRedis(scCodewordTypes, scCodewords);
	}
	
	/**
	 * 将Object数据加载入Redis
	 * 
	 * @param types
	 * @param codewords
	 */
	private void loopIntoRedis(List<CodewordType> types, List<Codeword> codewords) {
		final BoundHashOperations<String, String, String> codesOperations = template.boundHashOps("codewords:codes");
		final BoundHashOperations<String, String, String> namesOperations = template.boundHashOps("codewords:names");
		for (CodewordType type : types) {
			final BoundHashOperations<String, String, Codeword> boundHashOperations = template.boundHashOps(String.format("codewords:%s:values", type.getId()));
			final BoundHashOperations<String, String, String> boundListOperations = template.boundHashOps(String.format("codewords:%s:codes", type.getId()));
			for (int i = 0; i < codewords.size(); i++) {
				final Codeword codeword = codewords.get(i);
				if (codeword.getCodewordTypeId().equals(type.getId())) {
					boundHashOperations.put(codeword.getId(), codeword);
					boundListOperations.put(codeword.getCode(), codeword.getId());
				}
			}
			codesOperations.put(type.getTypeValue(), type.getId());
			namesOperations.put(type.getTypeName(), type.getId());
		}
	}

}
