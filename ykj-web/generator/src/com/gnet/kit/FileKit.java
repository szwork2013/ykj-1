package com.gnet.kit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作工具类 实现文件的创建、删除、复制、压缩、解压以及目录的创建、删除、复制、压缩解压等功能
 * 
 * @type utils
 * @description 文件操作工具类 提供了文件的创建、删除、复制、压缩、解压以及文件目录的创建、删除、复制、压缩解压之类操作
 * @version 1.0
 */
public class FileKit extends FileUtils {

	private static Logger log = LoggerFactory.getLogger(FileKit.class);

	/**
	 * 复制单个文件，如果目标文件存在，则不覆盖
	 * 
	 * @description 传入代复制文件的文件路径和目标文件路径， 若目标文件已存在，不进行覆盖，重复直接返回false
	 *              如果复制成功，则返回true，否则返回false
	 * @version 1.0
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFile(String srcFileName, String descFileName) {
		return FileKit.copyFileCover(srcFileName, descFileName, false);
	}

	/**
	 * 复制单个文件
	 * 
	 * @description 传入代复制文件的文件路径和目标文件路径 判断源文件是否存在，若不存在日志中记录错误，返回false
	 *              判断源文件是否为合法文件，若文件不合法在日志中记录错误，返回false 判断目标文件是否存在
	 *              若存在，根据传入的是否覆盖的信息进行判断 若允许覆盖，则在目标文件存在的情况下删除原目标文件，新建目标文件
	 *              若不允许覆盖，直接返回false
	 *              若不存在，则先判断目标文件是否存在。若不存在进行创建，然后在创建文件。若存在直接创建文件
	 *              创建目标文件成功后返回true，否则返回false
	 * @version 1.0
	 * @param srcFileName
	 *            待复制的文件名
	 * @param descFileName
	 *            目标文件名
	 * @param coverlay
	 *            如果目标文件已存在
	 * @return 如果复制成功，则返回true，否则返回false
	 */
	public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
		File srcFile = new File(srcFileName);
		// 判断源文件是否存在
		if (!srcFile.exists()) {
			log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
			return false;
		}
		// 判断源文件是否是合法的文件
		else if (!srcFile.isFile()) {
			log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
			return false;
		}
		File descFile = new File(descFileName);
		// 判断目标文件是否存在
		if (descFile.exists()) {
			// 如果目标文件存在，并且允许覆盖
			if (coverlay) {
				log.debug("目标文件已存在，准备删除!");
				if (!FileKit.delFile(descFileName)) {
					log.debug("删除目标文件 " + descFileName + " 失败!");
					return false;
				}
			} else {
				log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
				return false;
			}
		} else {
			if (!descFile.getParentFile().exists()) {
				// 如果目标文件所在的目录不存在，则创建目录
				log.debug("目标文件所在的目录不存在，创建目录!");
				// 创建目标文件所在的目录
				if (!descFile.getParentFile().mkdirs()) {
					log.debug("创建目标文件所在的目录失败!");
					return false;
				}
			}
		}

		// 准备复制文件
		// 读取的位数
		int readByte = 0;
		InputStream ins = null;
		OutputStream outs = null;
		try {
			// 打开源文件
			ins = new FileInputStream(srcFile);
			// 打开目标文件的输出流
			outs = new FileOutputStream(descFile);
			byte[] buf = new byte[1024];
			// 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
			while ((readByte = ins.read(buf)) != -1) {
				// 将读取的字节流写入到输出流
				outs.write(buf, 0, readByte);
			}
			log.debug("复制单个文件 " + srcFileName + " 到" + descFileName + "成功!");
			return true;
		} catch (Exception e) {
			log.debug("复制文件失败：" + e.getMessage());
			return false;
		} finally {
			// 关闭输入输出流，首先关闭输出流，然后再关闭输入流
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException oute) {
					oute.printStackTrace();
				}
			}
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException ine) {
					ine.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制整个目录的内容，如果目标目录存在，则不覆盖
	 * 
	 * @description 复制整个目录的内容，如果目标目录存在，则不覆盖,存在重复直接返回false
	 *              若复制目录成功则返回true，若复制目录不成功返回false
	 * @version 1.0
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectory(String srcDirName, String descDirName) {
		return FileKit.copyDirectoryCover(srcDirName, descDirName, false);
	}

	/**
	 * 复制整个目录的内容
	 * 
	 * @description 传入代复制目录的目录路径和目标目录路径 判断源目录是否存在，若不存在日志中记录错误，返回false
	 *              判断源目录是否为合法目录，若目录不合法在日志中记录错误，返回false 判断目标目录是否存在
	 *              若存在，根据传入的是否覆盖的信息进行判断 若允许覆盖，则在目标目录存在的情况下删除原目标目录，新建目标目录
	 *              若不允许覆盖，直接返回false
	 *              若不存在，则先判断目标目录是否存在。若不存在进行创建，然后在创建目录。若存在直接创建目录
	 *              创建目标目录成功后返回true，否则返回false
	 * @version 1.0
	 * @param srcDirName
	 *            源目录名
	 * @param descDirName
	 *            目标目录名
	 * @param coverlay
	 *            如果目标目录存在，是否覆盖
	 * @return 如果复制成功返回true，否则返回false
	 */
	public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
		File srcDir = new File(srcDirName);
		// 判断源目录是否存在
		if (!srcDir.exists()) {
			log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
			return false;
		}
		// 判断源目录是否是目录
		else if (!srcDir.isDirectory()) {
			log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
			return false;
		}
		// 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		// 如果目标文件夹存在
		if (descDir.exists()) {
			if (coverlay) {
				// 允许覆盖目标目录
				log.debug("目标目录已存在，准备删除!");
				if (!FileKit.delFile(descDirNames)) {
					log.debug("删除目录 " + descDirNames + " 失败!");
					return false;
				}
			} else {
				log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
				return false;
			}
		} else {
			// 创建目标目录
			log.debug("目标目录不存在，准备创建!");
			if (!descDir.mkdirs()) {
				log.debug("创建目标目录失败!");
				return false;
			}

		}

		boolean flag = true;
		// 列出源目录下的所有文件名和子目录名
		File[] files = srcDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 如果是一个单个文件，则直接复制
			if (files[i].isFile()) {
				flag = FileKit.copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 如果是子目录，则继续复制目录
			if (files[i].isDirectory()) {
				flag = FileKit.copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
				// 如果拷贝目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
			return false;
		}
		log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
		return true;

	}

	/**
	 * 
	 * 删除文件，可以删除单个文件或文件夹
	 * 
	 * @description 传入需要删除的文件的文件路径或文件夹路径 判断是文件还是文件夹后，删除文件或是文件夹
	 *              删除成功返回true，失败返回false 若对应的文件或是文件夹不存在，记录信息到日志，返回true
	 * @version 1.0
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否是返回false
	 */
	public static boolean delFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			log.debug(fileName + " 文件不存在!");
			return true;
		} else {
			if (file.isFile()) {
				return FileKit.deleteFile(fileName);
			} else {
				return FileKit.deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 
	 * 删除单个文件
	 * 
	 * @description 传入需要删除的文件路径 若文件找不到或不是文件，则记录文件不存在的日志，返回true 若存在且是文件，则直接删除
	 *              删除成功返回true，否则返回false
	 * @version 1.0
	 * @param fileName
	 *            被删除的文件名
	 * @return 如果删除成功，则返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				log.debug("删除单个文件 " + fileName + " 成功!");
				return true;
			} else {
				log.debug("删除单个文件 " + fileName + " 失败!");
				return false;
			}
		} else {
			log.debug(fileName + " 文件不存在!");
			return true;
		}
	}

	/**
	 * 
	 * 删除目录及目录下的文件
	 * 
	 * @description 传入某个目录路径，自动给结尾加分割符 若目录路径不存在，日志记录信息，返回true
	 *              先遍历目录中的文件以及子目录，删除文件，通过递归调用删除子目录,若删除失败设置标志位为false
	 *              最后删除目录，若删除成功返回true 若删除失败或标志位为false,返回false
	 * @call {@linkplain FileKit this}{@linkplain FileKit#deleteFile(String)
	 *       deleteFile}
	 * @call {@linkplain FileKit this}
	 *       {@linkplain FileKit#deleteDirectory(String) deleteDirectory}
	 * @version 1.0
	 * @param dirName
	 *            被删除的目录所在的文件路径
	 * @return 如果目录删除成功，则返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dirName) {
		String dirNames = dirName;
		if (!dirNames.endsWith(File.separator)) {
			dirNames = dirNames + File.separator;
		}
		File dirFile = new File(dirNames);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			log.debug(dirNames + " 目录不存在!");
			return true;
		}
		boolean flag = true;
		// 列出全部文件及子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = FileKit.deleteFile(files[i].getAbsolutePath());
				// 如果删除文件失败，则退出循环
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = FileKit.deleteDirectory(files[i].getAbsolutePath());
				// 如果删除子目录失败，则退出循环
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			log.debug("删除目录失败!");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			log.debug("删除目录 " + dirName + " 成功!");
			return true;
		} else {
			log.debug("删除目录 " + dirName + " 失败!");
			return false;
		}

	}

	/**
	 * 创建单个文件
	 * 
	 * @description 传入需要创建文件的路径 若文件已存在或是已分隔符结尾，则无法创建，返回false 若文件所在的目录不存在，则创建目录
	 *              创建文件，成功返回true 若文件或目录未创建成功，返回false
	 * @version 1.0
	 * @param descFileName
	 *            文件名，包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createFile(String descFileName) {
		File file = new File(descFileName);
		if (file.exists()) {
			log.debug("文件 " + descFileName + " 已存在!");
			return false;
		}
		if (descFileName.endsWith(File.separator)) {
			log.debug(descFileName + " 为目录，不能创建目录!");
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				log.debug("创建文件所在的目录失败!");
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				log.debug(descFileName + " 文件创建成功!");
				return true;
			} else {
				log.debug(descFileName + " 文件创建失败!");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(descFileName + " 文件创建失败!");
			return false;
		}

	}

	/**
	 * 创建目录
	 * 
	 * @description 传入创建目录的路径 若已存在目录，则返回false 创建目录成功返回true，不成功返回false
	 * @version 1.0
	 * @param descDirName
	 *            目录名,包含路径
	 * @return 如果创建成功，则返回true，否则返回false
	 */
	public static boolean createDirectory(String descDirName) {
		String descDirNames = descDirName;
		if (!descDirNames.endsWith(File.separator)) {
			descDirNames = descDirNames + File.separator;
		}
		File descDir = new File(descDirNames);
		if (descDir.exists()) {
			log.debug("目录 " + descDirNames + " 已存在!");
			return false;
		}
		// 创建目录
		if (descDir.mkdirs()) {
			log.debug("目录 " + descDirNames + " 创建成功!");
			return true;
		} else {
			log.debug("目录 " + descDirNames + " 创建失败!");
			return false;
		}

	}

	/**
	 * 压缩文件或目录
	 * 
	 * @description 传入压缩根目录、待压缩的文件或文件夹路径以及zip压缩包目标路径 若压缩根目录和待压缩的文件或文件夹不存在，返回
	 *              对文件或文件夹进行压缩，若失败捕获打印异常
	 * @call {@linkplain FileKit this}
	 *       {@linkplain FileKit#zipFilesToZipFile(String, File, ZipOutputStream)
	 *       zipFilesToZipFile}
	 * @call {@linkplain FileKit this}
	 *       {@linkplain FileKit#zipDirectoryToZipFile(String, File, ZipOutputStream)
	 *       zipDirectoryToZipFile}
	 * @version 1.0
	 * @param srcDirName
	 *            压缩的根目录
	 * @param fileName
	 *            根目录下的待压缩的文件名或文件夹名，其中*或""表示跟目录下的全部文件
	 * @param descFileName
	 *            目标zip文件
	 */
	public static void zipFiles(String srcDirName, String fileName, String descFileName) {
		// 判断目录是否存在
		if (srcDirName == null) {
			log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
			return;
		}
		File fileDir = new File(srcDirName);
		if (!fileDir.exists() || !fileDir.isDirectory()) {
			log.debug("文件压缩失败，目录 " + srcDirName + " 不存在!");
			return;
		}
		String dirPath = fileDir.getAbsolutePath();
		File descFile = new File(descFileName);
		try {
			ZipOutputStream zouts = new ZipOutputStream(new FileOutputStream(descFile));
			if ("*".equals(fileName) || "".equals(fileName)) {
				FileKit.zipDirectoryToZipFile(dirPath, fileDir, zouts);
			} else {
				File file = new File(fileDir, fileName);
				if (file.isFile()) {
					FileKit.zipFilesToZipFile(dirPath, file, zouts);
				} else {
					FileKit.zipDirectoryToZipFile(dirPath, file, zouts);
				}
			}
			zouts.close();
			log.debug(descFileName + " 文件压缩成功!");
		} catch (Exception e) {
			log.debug("文件压缩失败：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下
	 * 
	 * @description 传入需要解压的zip文件的路径和解压后文件的目标路径，根据路径创建ZipFile对象
	 *              获取ZIP文件里所有的entry，遍历所有entry 如果entry是一个目录，则创建目录
	 *              如果entry是一个文件，则创建父目录 从ZipFile对象中打开entry的输入流进行解压
	 *              解压成功返回true，不成功返回false
	 * @param zipFileName
	 *            需要解压的ZIP文件
	 * @param descFileName
	 *            目标文件
	 */
	public static boolean unZipFiles(String zipFileName, String descFileName) {
		String descFileNames = descFileName;
		if (!descFileNames.endsWith(File.separator)) {
			descFileNames = descFileNames + File.separator;
		}
		try {
			// 根据ZIP文件创建ZipFile对象
			ZipFile zipFile = new ZipFile(zipFileName);
			ZipEntry entry = null;
			String entryName = null;
			String descFileDir = null;
			byte[] buf = new byte[4096];
			int readByte = 0;
			// 获取ZIP文件里所有的entry
			@SuppressWarnings("rawtypes")
			Enumeration enums = zipFile.getEntries();
			// 遍历所有entry
			while (enums.hasMoreElements()) {
				entry = (ZipEntry) enums.nextElement();
				// 获得entry的名字
				entryName = entry.getName();
				descFileDir = descFileNames + entryName;
				if (entry.isDirectory()) {
					// 如果entry是一个目录，则创建目录
					new File(descFileDir).mkdirs();
					continue;
				} else {
					// 如果entry是一个文件，则创建父目录
					new File(descFileDir).getParentFile().mkdirs();
				}
				File file = new File(descFileDir);
				// 打开文件输出流
				OutputStream os = new FileOutputStream(file);
				// 从ZipFile对象中打开entry的输入流
				InputStream is = zipFile.getInputStream(entry);
				while ((readByte = is.read(buf)) != -1) {
					os.write(buf, 0, readByte);
				}
				os.close();
				is.close();
			}
			zipFile.close();
			log.debug("文件解压成功!");
			return true;
		} catch (Exception e) {
			log.debug("文件解压失败：" + e.getMessage());
			return false;
		}
	}

	/**
	 * 将目录压缩到ZIP输出流
	 * 
	 * @description 传入目录路径、文件信息和zip输出流 如果是目录是空的文件夹，获取目录信息，直接压缩到输出流中
	 *              如果目录非空，遍历目录中的子目录和文件 如果是文件则添加到压缩文件、如果是目录进行递归
	 * @call {@linkplain FileKit this}
	 *       {@linkplain FileKit#zipFilesToZipFile(String, File, ZipOutputStream)
	 *       zipFilesToZipFile}
	 * @call {@linkplain FileKit this}
	 *       {@linkplain FileKit#zipDirectoryToZipFile(String, File, ZipOutputStream)
	 *       zipDirectoryToZipFile}
	 * @version 1.0
	 * @param dirPath
	 *            目录路径
	 * @param fileDir
	 *            文件信息
	 * @param zouts
	 *            输出流
	 */
	public static void zipDirectoryToZipFile(String dirPath, File fileDir, ZipOutputStream zouts) {
		if (fileDir.isDirectory()) {
			File[] files = fileDir.listFiles();
			// 空的文件夹
			if (files.length == 0) {
				// 目录信息
				ZipEntry entry = new ZipEntry(getEntryName(dirPath, fileDir));
				try {
					zouts.putNextEntry(entry);
					zouts.closeEntry();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}

			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					// 如果是文件，则调用文件压缩方法
					FileKit.zipFilesToZipFile(dirPath, files[i], zouts);
				} else {
					// 如果是目录，则递归调用
					FileKit.zipDirectoryToZipFile(dirPath, files[i], zouts);
				}
			}

		}

	}

	/**
	 * 将文件压缩到ZIP输出流
	 * 
	 * @description 传入文件路径、文件信息和输出流 创建一个文件输入流，再创建一个Entry，存储信息、复制字节到压缩文件
	 *              若压缩失败，捕获并打印异常
	 * @version 1.0
	 * @param dirPath
	 *            目录路径
	 * @param file
	 *            文件
	 * @param zouts
	 *            输出流
	 */
	public static void zipFilesToZipFile(String dirPath, File file, ZipOutputStream zouts) {
		FileInputStream fin = null;
		ZipEntry entry = null;
		// 创建复制缓冲区
		byte[] buf = new byte[4096];
		int readByte = 0;
		if (file.isFile()) {
			try {
				// 创建一个文件输入流
				fin = new FileInputStream(file);
				// 创建一个ZipEntry
				entry = new ZipEntry(getEntryName(dirPath, file));
				// 存储信息到压缩文件
				zouts.putNextEntry(entry);
				// 复制字节到压缩文件
				while ((readByte = fin.read(buf)) != -1) {
					zouts.write(buf, 0, readByte);
				}
				zouts.closeEntry();
				fin.close();
				System.out.println("添加文件 " + file.getAbsolutePath() + " 到zip文件中!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 获取待压缩文件在ZIP文件中entry的名字，即相对于根目录的相对路径名
	 * 
	 * @description 传入文件或文件夹路径和文件信息，获取文件的绝对路径。若文件为目录则在结尾加分隔符
	 *              获得传入的文件路径的位置，获取文件绝对路径传入路径长度的部分，及文件相对根目录的路径，并返回
	 * @version 1.0
	 * @param dirPath
	 *            目录名
	 * @param file
	 *            entry文件名
	 * @return
	 */
	private static String getEntryName(String dirPath, File file) {
		String dirPaths = dirPath;
		if (!dirPaths.endsWith(File.separator)) {
			dirPaths = dirPaths + File.separator;
		}
		String filePath = file.getAbsolutePath();
		// 对于目录，必须在entry名字后面加上"/"，表示它将以目录项存储
		if (file.isDirectory()) {
			filePath += "/";
		}
		int index = filePath.indexOf(dirPaths);

		return filePath.substring(index + dirPaths.length());
	}


	/**
	 * 将内容写入文件
	 * 
	 * @description 传入内容和文件目标路径 根据路径新建文件，新建文件字符流，将内容写入文件 若文件已存在则打印信息文件生成失败
	 *              若出现异常，则捕获并打印异常
	 * @call {@linkplain FileKit this}{@linkplain FileKit#createFile(String)
	 *       createFile}
	 * @version 1.0
	 * @param content
	 * @param filePath
	 */
	public static void writeFile(String content, String filePath) {
		try {
			if (FileKit.createFile(filePath)) {
				FileWriter fileWriter = new FileWriter(filePath, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(content);
				bufferedWriter.close();
				fileWriter.close();
			} else {
				log.info("生成失败，文件已存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除多个文件
	 * 
	 * @description 传入前缀和文件目标路径字符串数组 遍历数组获得文件目标路径进行删除 如果前缀不为空则在路径前加上前缀
	 *              都删除成功，打印信息删除多个文件成功，返回true 否则打印删除多个文件失败，返回false
	 * @param prefix
	 *            前缀
	 * @param fileNames
	 *            多个文件名
	 * @return 是否删除成功
	 */
	public static boolean delFiles(String prefix, String... fileNames) {
		boolean result = true;
		for (String fileName : fileNames) {
			result &= deleteFile(StringUtils.isNotBlank(prefix) ? prefix + fileName : fileName);
		}
		if (result) {
			log.debug("删除多个文件成功!");
		} else {
			log.debug("删除多个文件失败!");
		}
		return result;
	}

}
