package com.gnet.tools.plugin.poi;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 编译文件查找器
 * 
 * @type utils
 * @description 编译文件查找器 提供了查找一个或一组编译文件的操作
 * @version 1.0
 */
public class ClassSearcher {

	/**
	 * 递归查找文件
	 * 
	 * @description 传入查找的文件夹路径与需要查找的文件名 若查找的是一个文件夹，则之间返回文件夹中的所有文件
	 *              若查找的是一个文件，遍历文件夹中的文件，找到对应名字的文件添加到列表并返回
	 * @call {@linkplain ClassSearcher this}
	 *       {@linkplain ClassSearcher#wildcardMatch(String, String)
	 *       wildcardMatch}
	 * @version 1.0
	 * @param baseDirName
	 *            查找的文件夹路径
	 * @param targetFileName
	 *            需要查找的文件名
	 */
	private static List<String> findFiles(String baseDirName, String targetFileName) {
		/**
		 * 算法简述： 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，
		 * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。 队列不空，重复上述操作，队列为空，程序结束，返回结果。
		 */
		List<String> classFiles = new ArrayList<String>();
		String tempName = null;
		// 判断目录是否存在
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");
		} else {
			String[] filelist = baseDir.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(baseDirName + File.separator + filelist[i]);
				if (!readfile.isDirectory()) {
					tempName = readfile.getName();
					if (ClassSearcher.wildcardMatch(targetFileName, tempName)) {
						String classname;
						String tem = readfile.getAbsoluteFile().toString().toString().replaceAll("\\\\", "/");
						classname = tem.substring(tem.indexOf("/classes") + "/classes".length(), tem.indexOf(".class"));
						if (classname.startsWith("/")) {
							classname = classname.substring(classname.indexOf("/") + 1);
						}
						classname = className(classname, "/classes");
						classFiles.add(classname);
					}
				} else if (readfile.isDirectory()) {
					classFiles.addAll(findFiles(baseDirName + File.separator + filelist[i], targetFileName));
				}
			}
		}
		return classFiles;
	}

	/**
	 * 查找jar包中的class
	 * 
	 * @description 传入jar路径与jar文件地址 查找一个文件，遍历文件夹中的文件，找到对应名字的文件添加到列表并返回
	 * @version 1.0
	 * @param baseDirName
	 *            jar路径
	 * @param jarFileURL
	 *            jar文件地址
	 *            <a href="http://my.oschina.net/u/556800" target="_blank" rel=
	 *            "nofollow">@return</a>
	 */
	public static List<String> findjarFiles(String baseDirName, URL... jarFileURL) {
		List<String> classFiles = new ArrayList<String>();
		try {
			// 判断目录是否存在
			File baseDir = new File(baseDirName);
			if (!baseDir.exists() || !baseDir.isDirectory()) {
				System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");
			} else {
				String[] filelist = baseDir.list();
				for (int i = 0; i < filelist.length; i++) {
					if (filelist[i].contains("zfplugin")) {// 查找含有zfplugin的jar包
						JarFile localJarFile = new JarFile(new File(baseDirName + File.separator + filelist[i]));
						Enumeration<JarEntry> entries = localJarFile.entries();
						while (entries.hasMoreElements()) {
							JarEntry jarEntry = entries.nextElement();
							String entryName = jarEntry.getName();
							if (jarEntry.isDirectory()) {
								// System.out.println(entryName);
							} else if (entryName.endsWith(".class")) {
								String className = entryName.replaceAll("/", ".").substring(0, entryName.length() - 6);
								classFiles.add(className);
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return classFiles;

	}

	/**
	 * 获得注解类的编译文件
	 * 
	 * @description 传入注解类的类信息 获得注解类的所有编译文件，将符合类信息的文件加入列表并返回
	 * @param clazz
	 * @return
	 * @throws MalformedURLException
	 */
	@SuppressWarnings({ "deprecation" })
	public static List<Class<?>> findClasses(Class<? extends Annotation> clazz) throws MalformedURLException {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		URL classPathUrl = ClassSearcher.class.getResource("/");
		List<String> classFileList = findFiles(classPathUrl.getFile(), "*.class");
		String lib = new File(classPathUrl.getFile()).getParent() + "/lib/";
		List<String> jarclassFiles = findjarFiles(lib, new File(lib).toURL());
		classFileList.addAll(jarclassFiles);
		for (String classFile : classFileList) {
			// String className = className(classFile, "/classes");
			try {
				Class<?> classInFile = Class.forName(classFile);
				Annotation[] annotations = classInFile.getAnnotations();
				for (Annotation anno : annotations) {
					if (anno.annotationType() == clazz) {
						classList.add(classInFile);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classList;
	}

	private static String className(String classFile, String pre) {
		String objStr = classFile.replaceAll("\\\\", "/");
		return objStr.replaceAll("/", ".");
	}

	/**
	 * 通配符匹配
	 * 
	 * @description 传入通配符模式与待匹配的字符串 对字符串进行匹配 匹配成功则返回true，否则返回false
	 * @call {@linkplain ClassSearcher this}
	 *       {@linkplain ClassSearcher#wildcardMatch(String, String)
	 *       wildcardMatch}
	 * @version 1.0
	 * @param pattern
	 *            通配符模式
	 * @param str
	 *            待匹配的字符串
	 * @return 匹配成功则返回true，否则返回false
	 */
	private static boolean wildcardMatch(String pattern, String str) {
		int patternLength = pattern.length();
		int strLength = str.length();
		int strIndex = 0;
		char ch;
		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
			ch = pattern.charAt(patternIndex);
			if (ch == '*') {
				// 通配符星号*表示可以匹配任意多个字符
				while (strIndex < strLength) {
					if (wildcardMatch(pattern.substring(patternIndex + 1), str.substring(strIndex))) {
						return true;
					}
					strIndex++;
				}
			} else if (ch == '?') {
				// 通配符问号?表示匹配任意一个字符
				strIndex++;
				if (strIndex > strLength) {
					// 表示str中已经没有字符匹配?了。
					return false;
				}
			} else {
				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
					return false;
				}
				strIndex++;
			}
		}
		return (strIndex == strLength);
	}

	public static void main(String[] args) throws MalformedURLException {
		// URL classPathUrl = ClassSearcher.class.getResource("/");
		// String baseDirName = new File(classPathUrl.getFile()).getParent()
		// + "/lib/";
		// URL url = new File(baseDirName).toURL();
		// ClassSearcher.findjarFiles(baseDirName, url);
		// ClassSearcher.findClasses(Demo.class);
		// List<Class> clazzes = ClassSearcher.findClasses(ExcelModel.class);
		// for(Class clazz : clazzes) {
		// System.err.println(clazz.getName());
		// }
	}
}