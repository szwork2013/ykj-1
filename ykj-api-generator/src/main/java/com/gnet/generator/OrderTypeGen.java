package com.gnet.generator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnet.tools.TemplateKit;
import com.gnet.tools.ToCamelNameTemplateMethodModel;

public class OrderTypeGen {
	
	private static Logger LOG = LoggerFactory.getLogger(ControllerGen.class);
	
	public static void process(String targetDirPath, String packageParentDir, String resourceName, String resourceChineseName, Map<String, String> sortMap) {
		
		Map<String, Object> model = new HashMap<>();
		model.put("packageDir", packageParentDir);
		model.put("resourceName", resourceName);
		model.put("resourceChineseName", resourceChineseName);
		model.put("sortMap", sortMap);
		
		String className = ToCamelNameTemplateMethodModel.camelName(resourceName);
		className = new StringBuilder().append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
		TemplateKit.process("OrderType.ftl"
				, targetDirPath + File.separator + className + "OrderType.java"
				, model);
		
		LOG.info(resourceName + "OrderType生成成功");
	}
}
