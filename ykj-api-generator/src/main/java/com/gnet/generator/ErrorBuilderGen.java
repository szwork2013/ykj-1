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

public class ErrorBuilderGen {
	
	private static Logger LOG = LoggerFactory.getLogger(ErrorBuilderGen.class);
	
	public static void process(String targetDirPath, String packageParentDir
			, String resourceName, String resourceChineseName
			, List<String> errorCodes, List<String[]> needValidateCreateAndUpdate
			, Map<String, List<Object>> columnMap) {
			
		Map<String, Object> model = new HashMap<>();
		model.put("packageDir", packageParentDir);
		model.put("resourceName", resourceName);
		model.put("resourceChineseName", resourceChineseName);
		if (!errorCodes.isEmpty()) {
			if (errorCodes.size() >= 8 && errorCodes.get(7) != null) {
				Integer lengthErrorCode = Integer.valueOf(errorCodes.get(7));
				Map<String, String> lengthErrorCodesMap = Maps.newLinkedHashMap();
				for (Map.Entry<String, List<Object>> entry : columnMap.entrySet()) {
					if (entry.getValue().get(1) != null) {
						lengthErrorCodesMap.put(entry.getKey(), lengthErrorCode.toString());
						lengthErrorCode ++;
					}
				}
				
				model.put("lengthErrorCodes", lengthErrorCodesMap);
			}
			
			
			model.put("errorCodes", errorCodes);
		}
		
		if (!needValidateCreateAndUpdate.isEmpty()) {
			boolean dataValiSucc = true;
			Map<String, String[]> customErrorCodes = Maps.newHashMap();
			for(String[] item : needValidateCreateAndUpdate) {
				if (StrKit.isBlank(item[0]) || StrKit.isBlank(item[1])  || StrKit.isBlank(item[2])) {
					dataValiSucc = false;
					LOG.warn("创建时需要验证的数据不符合规则\nnew String[]{字段名, 字段中文名, 错误编码}\n" + item.toString());
					break;
				}
				
				customErrorCodes.put(item[0], new String[]{item[2], item[1] + "为空错误"});
			}
			
			if (dataValiSucc) {
				model.put("customErrorCodes", customErrorCodes);
			}
		}
		
		String className = ToCamelNameTemplateMethodModel.camelName(resourceName);
		className = new StringBuilder().append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
		TemplateKit.process("ErrorBuilder.ftl"
				, targetDirPath + File.separator + className + "ErrorBuilder.java"
				, model);
		
		LOG.info(resourceName + "ErrorBuilder生成成功");
	}
}
