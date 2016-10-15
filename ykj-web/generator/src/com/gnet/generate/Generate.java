package com.gnet.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gnet.kit.FileKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

import jodd.util.PropertiesUtil;

/**
 * 代码生成器
 */
public class Generate {

	private static Logger logger = LoggerFactory.getLogger(Generate.class);

	public static void main(String[] args) throws Exception {

		// ========== ↓↓↓↓↓↓ 执行前请修改参数，谨慎执行。↓↓↓↓↓↓ ====================
		Boolean isDev = false;
		
		// 主要提供基本功能模块代码生成。
		String projectPath = "/Users/cj/git/react-dva-example";
		String generatorPath = projectPath + File.separator + "generator";
		
		String author = "xuq";
		
		String packageName = "courier";
		String moduleName = "AgentDistribution";
		String moduleCName = "快递代发记录";
		
		String dbName = "courier-cabinet";
		String tableName = "sc_express_info";
		String uniqueName = "";
		
		Boolean canDelAll = false;		// 是否允许列表出现删除全部操作
		Boolean generateList = true;	
		Boolean generateDetail = true;
		Boolean generateAdd = true;
		Boolean generateEdit = true;
		Boolean generateModel = true;
		Boolean generatePage = true;
		Boolean generateSelectors = true;
		Boolean generateService = true;
		
		String ROOT = "/api/agent_distribution";
		String SEARCH = ROOT + "/search";
		String ADD = ROOT;
		String EDIT = ROOT + "/${id}";
		String VIEW = EDIT;
		String DELETE = EDIT;
		String DELETE_BATCH = ROOT;
		
		String[] searchableList = new String[]{"user_phone", "platform_code", "exp_no", "in_time"};
		String[] searchableAList = new String[]{"user_phone", "platform_code", "exp_no", "in_time", "community_id", "exp_memo", "getType", "accept_way", "exp_source"};
		String[] onlyAddFields = new String[]{"getType"};
		String[] onlyEditFields = new String[]{"in_time"};
		// ========== ↑↑↑↑↑↑ 执行前请修改参数，谨慎执行。↑↑↑↑↑↑ ====================

		// 获取文件分隔符
		String separator = File.separator;
		
		logger.info("Project Path: {}", projectPath);
		// 模板文件路径
		String tplPath = StringUtils.replace(generatorPath + "/src/com/gnet/template", "/", separator);
		logger.info("Template Path: {}", tplPath);
		
		String srcPath = projectPath + File.separator + "src";
		String routesModule = srcPath + File.separator + "routes";
		String modelsModule = srcPath + File.separator + "models" + separator + camelName(moduleName) + "s";
		String componentsModule = srcPath + File.separator + "components" + File.separator + moduleName + "s";
		String servicesModule = srcPath + File.separator + "services";

		// 初始化模板引擎
		TemplateKit.init();
		TemplateKit.setTmplateDirPath(tplPath);
		
		// dbinit
		Properties p = getProperties("db.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.getProperty("db.mysql.url"), p.getProperty("db.mysql.username"), p.getProperty("db.mysql.password"));
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setContainerFactory(new CaseInsensitiveContainerFactory());
		activeRecordPlugin.setShowSql(true);
		druidPlugin.start();
		activeRecordPlugin.start();
		
		// 获得表结构
		List<Record> records = Db.find("select  TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, COLUMN_KEY,COLUMN_DEFAULT,NUMERIC_PRECISION,COLUMN_COMMENT from information_schema.columns where table_name = ? and TABLE_SCHEMA = ?", tableName, dbName);

		List<Record> list = new ArrayList<>();
		for (Record record : records) {
			record.set("COLUMN_CAMEL_NAME", camelName(record.getStr("COLUMN_NAME")));
			
			// 去处注释有类别说明的备注
			String comment = record.getStr("COLUMN_COMMENT");
			if (comment.indexOf(":") > -1) {
				comment = comment.split(":")[0];
			}
			if (comment.indexOf("：") > -1) {
				comment = comment.split("：")[0];
			}
			record.set("COLUMN_COMMENT", comment);
			
			list.add(record);
		}
		Collections.reverse(list);

		// 定义模板变量
		Map<String, Object> model = new HashMap<>();

		model.put("isDev", isDev);
		model.put("tplPath", tplPath);
		model.put("currentDate", new Date());
		model.put("author", author);
		
		model.put("canDelAll", canDelAll);
		
		model.put("packageName", packageName);
		model.put("moduleName", moduleName);
		model.put("_moduleName", camelName(moduleName));
		model.put("moduleCName", moduleCName);
		
		model.put("records", records);
		model.put("uniqueName", uniqueName);
		model.put("uniqueNameCamel", camelName(uniqueName));
		model.put("noSearch", searchableList.length == 0);
		model.put("searchableList", searchableList);
		model.put("noASearch", searchableAList.length == 0);
		model.put("searchableAList", searchableAList);
		
		model.put("addFields", onlyAddFields);
		model.put("editFields", onlyEditFields);
		List<String> moreFields = new ArrayList<>();
		moreFields.addAll(Arrays.asList(onlyAddFields));
		moreFields.addAll(Arrays.asList(onlyEditFields));
		model.put("moreFields", moreFields.toArray(new String[]{}));
		
		model.put("ROOT", ROOT);
		model.put("SEARCH", SEARCH);
		model.put("ADD", ADD);
		model.put("VIEW", VIEW);
		model.put("EDIT", EDIT);
		model.put("DELETE", DELETE);
		model.put("DELETE_BATCH", DELETE_BATCH);

		String filePath = "";
		
		// 生成 List.jsx
		if (generateList) {
			filePath = componentsModule + separator + "index.jsx";
			TemplateKit.process("List.jsx", filePath, model);
			logger.info(moduleName + "List.jsx: {}", filePath);
		
			// 生成 List.less
			filePath = componentsModule + separator + "index.less";
			TemplateKit.process("List.less", filePath, model);
			logger.info(moduleName + "List.less: {}", filePath);
		}

		// 生成 Detail.jsx
		if (generateDetail) {
			filePath = componentsModule + separator + "Detail.jsx";
			TemplateKit.process("Detail.jsx", filePath, model);
			logger.info(moduleName + "Detail.jsx: {}", filePath);
		}
		
		// 生成 Add.jsx
		if (generateAdd) {
			filePath = componentsModule + separator + "Add.jsx";
			TemplateKit.process("Add.jsx", filePath, model);
			logger.info(moduleName + "Add.jsx: {}", filePath);
		}
		
		// 生成 Edit.jsx
		if (generateEdit) {
			filePath = componentsModule + separator + "Edit.jsx";
			TemplateKit.process("Edit.jsx", filePath, model);
			logger.info(moduleName + "Edit.jsx: {}", filePath);
		}
		
		// 生成 model.js
		if (generateModel) {
			filePath = modelsModule + separator + "index.js";
			TemplateKit.process("model.js", filePath, model);
			logger.info(moduleName + "model.js: {}", filePath);
		}
		
		// 生成 selectors.jsx
		if (generateSelectors) {
			filePath = modelsModule + separator + "selectors.js";
			TemplateKit.process("selectors.js", filePath, model);
			logger.info(moduleName + "selectors.js: {}", filePath);
		}
		
		// 生成 Page.jsx
		if (generatePage) { 
			filePath = routesModule + separator + moduleName + "sPage.jsx";
			TemplateKit.process("Page.jsx", filePath, model);
			logger.info(moduleName + "Page.jsx: {}", filePath);
		}
		
		// 生成 service.js
		if (generateService) {
			filePath = servicesModule + separator + camelName(moduleName) + ".js";
			TemplateKit.process("service.js", filePath, model);
			logger.info("service.js: {}", filePath);
		}

		logger.info("Generate Success.");
	}

	/**
	 * 将内容写入文件
	 * 
	 * @param content
	 * @param filePath
	 */
	public static void writeFile(String content, String filePath) {
		try {
			if (FileKit.createFile(filePath)) {
				FileOutputStream writerStream = new FileOutputStream(filePath);
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
				bufferedWriter.write(content);
				bufferedWriter.close();
				writerStream.close();
			} else {
				logger.info("生成失败，文件已存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取json data文件
	 */
	private static Map<String, JSON> readData(String dataPath) {
		Map<String, JSON> result = new TreeMap<>();
		File dataDic = new File(dataPath);
		Collection<File> files = FileUtils.listFiles(dataDic, new IOFileFilter() {

			@Override
			public boolean accept(File arg0) {
				return true;
			}

			@Override
			public boolean accept(File arg0, String arg1) {
				return true;
			}
			
		}, new IOFileFilter() {
			
			@Override
			public boolean accept(File arg0, String arg1) {
				return false;
			}
			
			@Override
			public boolean accept(File arg0) {
				return false;
			}
		});
		Iterator<File> iter = files.iterator();
		while (iter.hasNext()) {
			File file = iter.next();
			try {
				String jsonStr = FileUtils.readFileToString(file, "UTF-8");
				String key = file.getName().split("\\.")[0];
				result.put(key, jsonStr.trim().startsWith("[") ? JSONArray.parseArray(jsonStr) : JSONObject.parseObject(jsonStr));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		
	}
	
	private static Properties getProperties(String fileName) {
		try {
			return PropertiesUtil.createFromFile(PathKit.getRootClassPath() + File.separator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String camelName(String name) {
	    StringBuilder result = new StringBuilder();
	    // 快速检查
	    if (name == null || name.isEmpty()) {
	        // 没必要转换
	        return "";
	    } else if (!name.contains("_")) {
	        // 不含下划线，仅将首字母小写
	        return name.substring(0, 1).toLowerCase() + name.substring(1);
	    }
	    // 用下划线将原始字符串分割
	    String camels[] = name.split("_");
	    for (String camel :  camels) {
	        // 跳过原始字符串中开头、结尾的下换线或双重下划线
	        if (camel.isEmpty()) {
	            continue;
	        }
	        // 处理真正的驼峰片段
	        if (result.length() == 0) {
	            // 第一个驼峰片段，全部字母都小写
	            result.append(camel.toLowerCase());
	        } else {
	            // 其他的驼峰片段，首字母大写
	            result.append(camel.substring(0, 1).toUpperCase());
	            result.append(camel.substring(1).toLowerCase());
	        }
	    }
	    return result.toString();
	}

}
