package com.gnet.utils.download;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

public class DownResponseBuilder {
	
	private DownResponseBuilder() {
		
	}
	
	public static final void buildExcelXlsx(HttpServletResponse response, Resource resource) throws IOException {
		buildExcelXlsx(response, resource, null);
	}
	
	public static final void buildExcelXlsx(HttpServletResponse response, Resource resource, String fileName) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setContentLength(Integer.valueOf(String.valueOf(resource.contentLength())));
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(StringUtils.isBlank(fileName) ? resource.getFilename() : fileName, "UTF-8"));
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
		response.flushBuffer();
	}
	
	public static final void buildFile(HttpServletResponse response, Resource resource, String fileName) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/octet-stream");
		response.setContentLength(Integer.valueOf(String.valueOf(resource.contentLength())));
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(StringUtils.isBlank(fileName) ? resource.getFilename() : fileName, "UTF-8"));
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
		response.flushBuffer();
	}
}
