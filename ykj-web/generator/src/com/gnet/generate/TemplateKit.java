package com.gnet.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.gnet.kit.PathKit;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jodd.util.PropertiesUtil;

public class TemplateKit {

	private transient static final Configuration config = new Configuration();

	private TemplateKit() {
	};

	public static void setProperties(Properties properties) {
		try {
			config.setSettings(properties);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setTmplateDirPath(File file) {
		try {
			config.setDirectoryForTemplateLoading(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void setTmplateDirPath(String filePath) {
		try {
			config.setDirectoryForTemplateLoading(new File(filePath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 初始化FreeMarker
	 */
	public static void init() {
		config.setClassicCompatible(true);
		config.setTemplateUpdateDelay(0);
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		// 自动 AUTO_DETECT_TAG_SYNTAX
		// 尖括号 ANGLE_BRACKET_TAG_SYNTAX
		// 方括号 SQUARE_BRACKET_TAG_SYNTAX
		config.setTagSyntax(Configuration.ANGLE_BRACKET_TAG_SYNTAX);// 使用 <>
		config.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
		config.setDefaultEncoding("UTF-8");
		config.setOutputEncoding("UTF-8");
		config.setLocale(Locale.CHINA); // config.setLocale(Locale.US);
		config.setLocalizedLookup(false);
		config.setNumberFormat("#0.#####");
	}

	@SuppressWarnings("unchecked")
	public static String process(String templetFilename, Map rootMap) {
		Writer out = null;
		try {
			Template temp = config.getTemplate(templetFilename);
			out = new StringWriter();
			temp.process(rootMap, out);
			String string = out.toString();
			return string;
		} catch (IOException e) {
			throw new GenException("not found template file :" + templetFilename, e);
		} catch (TemplateException e) {
			throw new GenException("Problem pasre template file :" + templetFilename, e);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void process(String templetFilename, String outputFilename, Map rootMap) {
		Writer out = null;
		try {
			Template temp = config.getTemplate(templetFilename);
			File file = new File(StringUtils.substringBeforeLast(outputFilename, File.separator));
			if (!file.exists()) {
				file.mkdirs();
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilename), "UTF-8"));
			temp.process(rootMap, out);
		} catch (UnsupportedEncodingException e) {
			throw new GenException("Problem writer output  file:" + outputFilename, e);
		} catch (IOException e) {
			throw new GenException("not found template file :" + templetFilename, e);
		} catch (TemplateException e) {
			throw new GenException("Problem pasre template file :" + templetFilename, e);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 104010Z200.xml
	public static void main(String[] args) {

	}

	@SuppressWarnings("unused")
	private static Properties getProperties(String fileName) {
		try {
			return PropertiesUtil.createFromFile(PathKit.getRootClassPath() + File.separator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
