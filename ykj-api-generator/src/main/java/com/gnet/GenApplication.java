package com.gnet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.gnet.generator.ControllerGen;
import com.gnet.generator.ErrorBuilderGen;
import com.gnet.generator.JavaBeanGen;
import com.gnet.generator.MapperGen;
import com.gnet.generator.OrderTypeGen;
import com.gnet.generator.ResourceAssemblerGen;
import com.gnet.generator.ResourceGen;
import com.gnet.generator.ServiceGen;
import com.gnet.generator.ValidatorGen;
import com.gnet.tools.TemplateKit;
import com.gnet.tools.ToCamelNameTemplateMethodModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.druid.DruidPlugin;

import jodd.util.PropertiesUtil;

public class GenApplication {
	
	// 模板文件路径
	private static final String templateDirPath = PathKit.getRootClassPath() + File.separator + "template";
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		/* 基本资源配置 */
		// 待生成的资源名称, 请填入
		final String resourceName = "clerk";
		// 待生成的数据库表名, 请填入
		final String tableName = "ykj_clerk";
		// 待生成资源中文名
		final String resourceChineseName = "公司人员";
		// 待生成的资源名称的复数形式
		final String resourceNameComplex = "clerks";
		
		/* 下面这些根据需要填写 */
		/* 是否使用xml管理sql */
		final boolean isUseXmlMapper = Boolean.TRUE; // 默认填是
		/* 关于删除 */
		final boolean isSoftDel = Boolean.TRUE; // 是否软删（默认硬删）
		final String softDelColumn = "is_del"; // 默认软删字段（跟硬删没关系）
		
		/* 关于错误编码 */
		// 关于基本的错误编码
		// 定义第一个errorCode的位置，一共定义7个错误编码
		// 0为编号为空，1为编号无法找到资源，2为新增，3为更新，4为删除，5排序字段不合法，6排序方向不合法
		Integer firstErrorCode = null;
		// 定义第一个数据长度校验errorCode的位置
		Integer firstLengthCode = null;
		// 不按顺序来的可以不填 firstErrorCode，然后自己add
		final List<String> baseErrorCodes = Lists.newArrayList();
		
		/* 关于非空验证字段 */
		// 创建与更新时需要验证非空的字段（不包括id），默认不生成
		// 元素格式  new String[]{传入字段名, 传入字段中文名, 错误编码, 是否为字符串，是填"true"(可选)}
		final List<String[]> needValidateCreateAndUpdate = Lists.newArrayList();
		
		/* 关于查询 */
		// 查询字段，输入数据库的对应字段
		final List<String> searchList = Lists.newArrayList();
		
		/* 关于排序 */
		// 排序字段，key为传入排序字段名，value为数据库对应的排序字段名
		final Map<String, String> sortMap = Maps.newLinkedHashMap();
		
		// 上层包名
		final String packageParentDir = "com.gnet.app";
		
		/* =================基本配置结束===================== */
		
		Properties properties = getProperties("application.properties");
		init(properties);
		
		if (StrKit.isBlank(resourceName) || StrKit.isBlank(tableName)) {
			throw new RuntimeException("资源名称和数据库表名不能为空");
		}
		
		// 创建文件夹
		String targetDirPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + ToCamelNameTemplateMethodModel.camelName(resourceName);
		File file = new File(targetDirPath);
		if (!file.exists()) {
			file.mkdir();
		}
		
		// 传入参数
		Map<String, Object> paramsMap = Maps.newHashMap();
		paramsMap.put("targetDirPath", targetDirPath);
		paramsMap.put("packageParentDir", packageParentDir);
		paramsMap.put("tableName", tableName);
		paramsMap.put("resourceName", resourceName);
		paramsMap.put("resourceNameComplex", resourceNameComplex);
		paramsMap.put("isSoftDel", isSoftDel);
		paramsMap.put("softDelColumn", softDelColumn);
		paramsMap.put("searchList", searchList);
		paramsMap.put("resourceChineseName", resourceChineseName);
		paramsMap.put("baseErrorCodes", baseErrorCodes);
		paramsMap.put("needValidateCreateAndUpdate", needValidateCreateAndUpdate);
		paramsMap.put("sortMap", sortMap);
		paramsMap.put("isUseXmlMapper", isUseXmlMapper);
		
		// 1.创建JavaBean
		String dbUrl = properties.getProperty("spring.datasource.url");
		String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1, dbUrl.indexOf("?") > -1 ? dbUrl.indexOf("?") : dbUrl.length());
		Map<String, List<Object>> columnMap = Maps.newHashMap();
		paramsMap.put("dbName", dbName);
		JavaBeanGen.process(targetDirPath, packageParentDir, dbName, tableName, resourceName, columnMap);
		paramsMap.put("columnMap", columnMap);
		
		// 2.创建Mapper
		MapperGen.process(paramsMap);
		
		// 3.创建Resource
		ResourceGen.process(targetDirPath, packageParentDir, resourceName);
		
		// 4.创建ResourceAssmbler
		ResourceAssemblerGen.process(targetDirPath, packageParentDir, resourceName);
		
		// 5.创建Controller
		ControllerGen.process(targetDirPath, packageParentDir, resourceName, resourceNameComplex, resourceChineseName, isSoftDel, searchList, columnMap);
		
		// 6.创建Service
		ServiceGen.process(targetDirPath, packageParentDir, resourceName, resourceNameComplex, resourceChineseName, isSoftDel, searchList, columnMap);
		
		// 7.创建ErrorBuilder
		if (firstErrorCode != null) {
			for (int i = 0; i < 7; i ++) {
				baseErrorCodes.add(i, firstErrorCode.toString());
				firstErrorCode ++;
			}
		} else {
			for (int i = 0; i < 7; i ++) {
				baseErrorCodes.add(null);
			}
		}
		
		if (firstLengthCode != null) {
			baseErrorCodes.add(7, firstLengthCode.toString());
		}
		
		ErrorBuilderGen.process(targetDirPath, packageParentDir, resourceName, resourceChineseName, baseErrorCodes, needValidateCreateAndUpdate, columnMap);
		
		// 8.创建Validator
		ValidatorGen.process(targetDirPath, packageParentDir, resourceName, resourceNameComplex, resourceChineseName, needValidateCreateAndUpdate, columnMap);
		
		// 9.创建OrderType
		OrderTypeGen.process(targetDirPath, packageParentDir, resourceName, resourceChineseName, sortMap);
		
	}
	
	private static void init(Properties properties) {
		// 连接数据库
		String dbUrl = properties.getProperty("spring.datasource.url");
		String dbUsername = properties.getProperty("spring.datasource.username");
		String dbPassword = properties.getProperty("spring.datasource.password");
		if (StrKit.isBlank(dbUrl) || StrKit.isBlank(dbUsername) || StrKit.isBlank(dbPassword)) {
			throw new RuntimeException("数据库连接信息不完善");
		}
		
		DruidPlugin druidPlugin = new DruidPlugin(dbUrl, dbUsername, dbPassword);
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setContainerFactory(new CaseInsensitiveContainerFactory());
		activeRecordPlugin.setShowSql(true);
		druidPlugin.start();
		activeRecordPlugin.start();
		
		// 初始化模板引擎
		TemplateKit.init();
		TemplateKit.setTmplateDirPath(templateDirPath);
	}
	
	
	private static Properties getProperties(String fileName) {
		try {
			return PropertiesUtil.createFromFile(PathKit.getRootClassPath() + File.separator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
