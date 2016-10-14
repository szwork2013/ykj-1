package com.gnet.generator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnet.tools.TemplateKit;
import com.gnet.tools.ToCamelNameTemplateMethodModel;

public class ResourceGen {
	
	private static Logger LOG = LoggerFactory.getLogger(ResourceGen.class);
	
	public static void process(String targetDirPath, String packageParentDir, String resourceName) {
		
		Map<String, Object> model = new HashMap<>();
		model.put("packageDir", packageParentDir);
		model.put("resourceName", resourceName);
		
		String className = ToCamelNameTemplateMethodModel.camelName(resourceName);
		className = new StringBuilder().append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
		TemplateKit.process("Resource.ftl"
				, targetDirPath + File.separator + className + "Resource.java"
				, model);
		
		LOG.info(resourceName + "Resource生成成功");
	}
	
}
