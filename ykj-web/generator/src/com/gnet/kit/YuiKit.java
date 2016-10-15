package com.gnet.kit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.log4j.Logger;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class YuiKit {
	
	private static final Logger logger = Logger.getLogger(YuiKit.class);
	static int linebreakpos = -1;  
    static boolean munge=true;  
    static boolean verbose=true;  
    static boolean preserveAllSemiColons=false;  
    static boolean disableOptimizations=false;

	public static void compressJsAndCss() {
		
	}
	
	public static void compressDirectory(File directory, boolean deleteSource) {
		Collection<File> files = FileUtils.listFiles(directory, new IOFileFilter() {

			@Override
			public boolean accept(File arg0) {
				String arg1 = arg0.getName();
				String extension = FilenameUtils.getExtension(arg1);
				if (!extension.equalsIgnoreCase("js") && !extension.equalsIgnoreCase("css")) {
					return false;
				}
				if (arg1.contains(".min.")) {
					return false;
				}
				return true;
			}

			@Override
			public boolean accept(File arg0, String arg1) {
				String extension = FilenameUtils.getExtension(arg1);
				if (!extension.equalsIgnoreCase("js") && !extension.equalsIgnoreCase("css")) {
					return false;
				}
				if (arg1.contains("min")) {
					return false;
				}
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
			String extension = FilenameUtils.getExtension(file.getName());
			if (extension.equalsIgnoreCase("js")) {
				compressJS(file, deleteSource);
			} else if (extension.equalsIgnoreCase("css")) {
				compressCSS(file, deleteSource);
			}
		}
	}
	
	public static void compressCSS(File file, boolean deleteSource) {
		if (file.isDirectory()) {
			logger.error("CSS压缩：无法压缩文件夹");
			return;
		}
		
		String inFileName = file.getName();
		String outFileName = FilenameUtils.getBaseName(inFileName) + FilenameUtils.EXTENSION_SEPARATOR_STR + "min" + FilenameUtils.EXTENSION_SEPARATOR_STR + FilenameUtils.getExtension(inFileName);
		
		if (!FilenameUtils.getExtension(inFileName).equalsIgnoreCase("css")) {
			logger.warn(inFileName + "不是css文件，无法压缩");
			return;
		}
		
		File outFile = new File(file.getParent() + File.separator + outFileName);
		
		FileWriter writer = null;
		FileReader reader = null;
		
		try {
			writer = new FileWriter(outFile);
			reader = new FileReader(file);
			CssCompressor cssCompressor = new CssCompressor(reader);
			cssCompressor.compress(writer, linebreakpos);
		} catch (EvaluatorException | IOException e) {
			logger.error(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
					reader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			
			if (deleteSource) {
				file.delete();
			}
		}
	}
	
	public static void compressJS(File file, boolean deleteSource) {
		if (file.isDirectory()) {
			logger.error("JS压缩：无法压缩文件夹");
			return;
		}
		
		String inFileName = file.getName();
		String outFileName = FilenameUtils.getBaseName(inFileName) + FilenameUtils.EXTENSION_SEPARATOR_STR + "min" + FilenameUtils.EXTENSION_SEPARATOR_STR + FilenameUtils.getExtension(inFileName);
		
		if (!FilenameUtils.getExtension(inFileName).equalsIgnoreCase("js")) {
			logger.warn(inFileName + "不是js文件，无法压缩");
			return;
		}
		
		File outFile = new File(file.getParent() + File.separator + outFileName);
		
		FileWriter writer = null;
		FileReader reader = null;
		try {
			writer = new FileWriter(outFile);
			reader = new FileReader(file);
			JavaScriptCompressor javaScriptCompressor = new JavaScriptCompressor(reader, new ErrorReporter() {
				
				@Override
				public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
					if (line < 0) {  
						logger.warn("\n[WARNING] " + message);  
                    } else {  
                    	logger.warn("\n[WARNING] " + line + ':' + lineOffset + ':' + message);  
                    }
				}
				
				@Override
				public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
					error(message, sourceName, line, lineSource, lineOffset);  
                    return new EvaluatorException(message);
				}
				
				@Override
				public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
					if (line < 0) {
                        logger.error("\n[ERROR] " + message);
                    } else {
                    	logger.error("\n[ERROR] " + line + ':' + lineOffset + ':' + message);
                    }
				}
			});
			
			javaScriptCompressor.compress(writer, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
		} catch (EvaluatorException | IOException e) {
			logger.error(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
					reader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			
			if (deleteSource) {
				file.delete();
			}
		}
	}
	
	private YuiKit() {}
	
}
