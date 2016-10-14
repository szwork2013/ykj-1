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

public class ControllerGen {
	
	private static Logger LOG = LoggerFactory.getLogger(ControllerGen.class);
	
	public static void process(String targetDirPath, String packageParentDir
			, String resourceName, String resourceNameComplex
			, String resourceChineseName, Boolean isSoftDel
			, List<String> searchList, Map<String, List<Object>> columnMap) {
		
		Map<String, String> columnTypeMap = Maps.newLinkedHashMap();
		for (String item : searchList) {
			columnTypeMap.put(item, columnMap.get(item).get(0).toString());
		}
		
		Map<String, Object> model = new HashMap<>();
		model.put("packageDir", packageParentDir);
		model.put("resourceName", resourceName);
		model.put("lowResourceName", resourceName.toLowerCase());
		model.put("upResourceName", resourceName.toUpperCase());
		model.put("resourceNameComplex", resourceNameComplex == null ? resourceName + "s" : resourceNameComplex);
		model.put("resourceChineseName", resourceChineseName);
		model.put("isSoftDel", isSoftDel);
		model.put("searchList", searchList);
		model.put("searchMap", columnTypeMap);
		
		String className = ToCamelNameTemplateMethodModel.camelName(resourceName);
		className = new StringBuilder().append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
		TemplateKit.process("Controller.ftl"
				, targetDirPath + File.separator + className + "Controller.java"
				, model);
		
		LOG.info(resourceName + "Controller生成成功");
	}

}
