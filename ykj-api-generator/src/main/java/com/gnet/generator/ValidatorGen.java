package com.gnet.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnet.tools.TemplateKit;
import com.gnet.tools.ToCamelNameTemplateMethodModel;
import com.google.common.collect.Maps;
import com.jfinal.kit.StrKit;

public class ValidatorGen {
	
	private static Logger LOG = LoggerFactory.getLogger(ValidatorGen.class);
	
	public static void process(String targetDirPath, String packageParentDir
			, String resourceName, String resourceNameComplex
			, String resourceChineseName
			, List<String[]> needValidateCreateAndUpdate
			, Map<String, List<Object>> columnMap) {
		
		Map<String, Object> model = new HashMap<>();
		model.put("packageDir", packageParentDir);
		model.put("resourceName", resourceName);
		model.put("lowResourceName", resourceName.toLowerCase());
		model.put("resourceChineseName", resourceChineseName);
		model.put("resourceNameComplex", resourceNameComplex == null ? resourceName + "s" : resourceNameComplex);
		model.put("columnMap", columnMap);
		
		if (!needValidateCreateAndUpdate.isEmpty()) {
			boolean dataValiSucc = true;
			Map<String, String[]> validateColumns = Maps.newHashMap();
			for(String[] item : needValidateCreateAndUpdate) {
				if (StrKit.isBlank(item[0]) || StrKit.isBlank(item[1])  || StrKit.isBlank(item[2])) {
					dataValiSucc = false;
					LOG.warn("创建时需要验证的数据不符合规则\nnew String[]{字段名, 字段中文名, 错误编码}\n" + item.toString());
					break;
				}
				
				String isStr = "false";
				if (item.length == 4 && StrKit.notBlank(item[3]) && item[3].equalsIgnoreCase("true")) {
					isStr = "true";
				}
				
				validateColumns.put(item[0], new String[]{"ERROR_" + ToCamelNameTemplateMethodModel.camelName(item[0]).toUpperCase() + "_NULL"
								, item[1]
								, isStr});
			}
			
			if (dataValiSucc) {
				model.put("validateColumns", validateColumns);
			}
		}
		
		String className = ToCamelNameTemplateMethodModel.camelName(resourceName);
		className = new StringBuilder().append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
		TemplateKit.process("Validator.ftl"
				, targetDirPath + File.separator + className + "Validator.java"
				, model);
		
		LOG.info(resourceName + "Validator生成成功");
	}

}
