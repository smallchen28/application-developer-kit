/**
 * www.yiji.com Inc.
 * Copyright (c) 2011 All Rights Reserved.
 */
package com.yiji.adk.filefront.support.client;

import com.google.common.io.Files;
import com.yiji.adk.filefront.exceptions.FileBizException;
import com.yiji.adk.filefront.support.config.FileConfigContext;
import com.yiji.adk.filefront.support.config.FileConfigParser;
import com.yiji.adk.filefront.support.consts.StatusConsts;
import com.yjf.common.log.Logger;
import com.yjf.common.log.LoggerFactory;
import com.yjf.common.util.StringUtils;
import com.yjf.common.util.ToString;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件客户端FTP client实现
 *
 * @author hasulee
 * @author karott
 */
public class FtpClient extends BizClient implements FileClient {
	
	private static final Logger logger = LoggerFactory.getLogger(FtpClient.class);
	
	private String host;
	private int port;
	private String username;
	private String password;
	private FTPClient client = new FTPClient();
	
	public FtpClient(String bizType, String host, String port, String username, String password) {
		super(bizType);
		
		try {
			this.host = host;
			this.port = Integer.parseInt(port);
			this.username = username;
			this.password = password;
			
			initConfig();
		} catch (Exception e) {
			throw new FileBizException("FtpClient初始化异常", "");
		}
	}
	
	private void initConfig() throws NumberFormatException, SocketException {
		
		client.addProtocolCommandListener(new ProtocolCommandListener() {
			
			@Override
			public void protocolReplyReceived(ProtocolCommandEvent event) {
				logger.info("FtpEvent-->command:{},message:{},replyCode:{}", event.getCommand(),
					event.getMessage().replace("\r\n", ""), event.getReplyCode());
			}
			
			@Override
			public void protocolCommandSent(ProtocolCommandEvent event) {
				logger.info("FtpEvent-->command:{},message:{},replyCode:{}", event.getCommand(),
					event.getMessage().replace("\r\n", ""), event.getReplyCode());
			}
		});
		
		String charsetName = FileConfigParser.attrByNodeSystem(bizType, FileConfigContext.FTP,
			FileConfigContext.CHARSET);
		if (StringUtils.isNotBlank(charsetName)) {
			client.setCharset(Charset.forName(charsetName));
		}
		
		String recvBufferSize = FileConfigParser.attrByNodeSystem(bizType, FileConfigContext.FTP,
			FileConfigContext.RECV_BUFFER);
		if (StringUtils.isNotBlank(recvBufferSize)) {
			client.setReceieveDataSocketBufferSize(Integer.parseInt(recvBufferSize));
		}
		
		String sendBufferSize = FileConfigParser.attrByNodeSystem(bizType, FileConfigContext.FTP,
			FileConfigContext.SEND_BUFFER);
		if (StringUtils.isNotBlank(sendBufferSize)) {
			client.setSendDataSocketBufferSize(Integer.parseInt(sendBufferSize));
		}
		
	}
	
	@Override
	public File download(String sourceDirectory, String sourceName) {
		byte[] contents = downloadBytes(sourceDirectory, sourceName);
		File targetProcessFile;
		
		try {
			String configDir = FileConfigParser.textByNodeSystem(bizType, FileConfigContext.DOWNLOAD);
			String targetDir = FileClientFactory.createDirecotryByBizAndTime(bizType, configDir);
			targetProcessFile = createFile(contents, targetDir, sourceName);
		} catch (FileBizException ce) {
			throw ce;
		} catch (Exception e) {
			throw new FileBizException(String.format("ftp文件下载过程中出现异常，详情：%s", e.getMessage()), "");
		}
		
		return targetProcessFile;
	}
	
	@Override
	public byte[] downloadBytes(String sourceDirectory, String sourceName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			//- 创建连接
			connect();
			//- 登录ftp服务器
			login();
			//校验文件
			doCheckFileFromFTP(sourceDirectory, sourceName);
			//- 下载流
			try {
				client.retrieveFile(sourceDirectory + "/" + sourceName, outputStream);
				outputStream.flush();
			} finally {
				outputStream.close();
			}
			//- 登出ftp服务器
			logout();
		} catch (FileBizException ce) {
			throw ce;
		} catch (Exception e) {
			throw new FileBizException(String.format("ftp文件下载过程中出现异常，详情：%s", e.getMessage()), "");
		} finally {
			if (client != null && (client.isConnected() || client.isAvailable())) {
				try {
					client.disconnect();
				} catch (IOException e) {
					logger.error("关闭ftp连接过程中出现异常,无后续操作", e);
				}
			}
		}
		return outputStream.toByteArray();
	}
	
	private void logout() {
		try {
			if (!client.logout()) {
				throw new RuntimeException("登出失败");
			}
		} catch (Exception e) {
			logger.error("FTP登出过程中出现错误", e);
			
			throw new FileBizException(String.format("ftp服务器登出失败 host=%s,port=%s username=%s , password=%s", host, port,
				username, ToString.mask(password)), "");
		}
		
	}
	
	private void login() {
		try {
			if (!client.login(username, password)) {
				throw new RuntimeException("登录失败");
			}
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			boolean passiveModel = Boolean.parseBoolean(
				FileConfigParser.attrByNodeSystem(bizType, FileConfigParser.FTP, FileConfigParser.PASSIVE));
			if (passiveModel) {
				client.enterLocalPassiveMode();
			}
		} catch (Exception e) {
			logger.error("FTP登录过程中出现错误", e);
			
			throw new FileBizException(String.format("ftp服务器登登录失败 host=%s,port=%s username=%s , password=%s", host,
				port, username, ToString.mask(password)), "");
		}
	}
	
	private void connect() {
		try {
			client.connect(host, port);
			//- ftp客户端预处理
			preparedConnect();
			//判断应答
			int reply = client.getReplyCode();
			
			if (!FTPReply.isPositiveCompletion(reply)) {
				client.disconnect();
				String[] replyMessages = client.getReplyStrings();
				StringBuilder sb = new StringBuilder();
				for (String msg : replyMessages) {
					sb.append("---").append(msg).append("\n");
				}
				throw new FileBizException(String.format("FTP连接过程中出现错误...详情：%s", sb.toString()), "");
			}
		} catch (Exception e) {
			if (e instanceof FileBizException) {
				throw (FileBizException) e;
			}
			throw new FileBizException(String.format("FTP连接过程中出现错误...详情：%s", e.getMessage()), "");
		}
	}
	
	private void preparedConnect() throws NumberFormatException, SocketException {
		String soLinger = FileConfigParser.attrByNodeSystem(bizType, FileConfigParser.FTP, FileConfigParser.SO_LINGER);
		String soLingerTime = FileConfigParser.attrByNodeSystem(bizType, FileConfigParser.FTP,
			FileConfigParser.SO_LINGER_TIME);
		if (StringUtils.isNotBlank(soLinger) && StringUtils.isNotBlank(soLingerTime)) {
			if (Boolean.parseBoolean(soLinger)) {
				client.setSoLinger(Boolean.parseBoolean(soLinger), Integer.parseInt(soLingerTime));
			}
		}
		
		String soTimeout = FileConfigParser.attrByNodeSystem(bizType, FileConfigParser.FTP,
			FileConfigParser.SO_TIMEOUT);
		if (StringUtils.isNotBlank(soTimeout)) {
			client.setSoTimeout(Integer.parseInt(soTimeout));
		}
		
		String tcpNoDelay = FileConfigParser.attrByNodeSystem(bizType, FileConfigParser.FTP,
			FileConfigParser.TCP_NO_DELAY);
		if (StringUtils.isNotBlank(tcpNoDelay)) {
			client.setTcpNoDelay(Boolean.parseBoolean(tcpNoDelay));
		}
	}
	
	private File createFile(byte[] contents, String targetDirectory, String targetName) {
		File targetFile;
		try {
			//- 创建目标目录
			//- File _d_targetDirectory = new File(tmpParentDirectory, targetDirectory);
			File _d_targetDirectory = new File(targetDirectory);
			
			if (!_d_targetDirectory.exists()) {
				if (!_d_targetDirectory.mkdirs()) {
					throw new FileBizException(
						String.format("创建目标文件夹(%s)失败，请手工创建！", _d_targetDirectory.getAbsolutePath()));
				}
			}
			
			//- 创建目标文件输出流
			targetFile = new File(_d_targetDirectory, targetName);
			
			//- 如果存在直接删除
			if (targetFile.exists()) {
				if (!targetFile.delete()) {
					throw new FileBizException(
						String.format("文件已经存在filePath=%s,删除过程中出现错误....", targetFile.getAbsoluteFile()), "");
				}
			}
			
			Files.write(contents, targetFile);
		} catch (Exception e) {
			logger.error("FTP创建本地文件过程中出现错误...", e);
			if (e instanceof FileBizException) {
				throw (FileBizException) e;
			}
			throw new FileBizException(String.format("FTP创建本地文件过程中出现错误...详情：%s", e.getMessage()), "");
		}
		return targetFile;
	}
	
	@Override
	public File upload(String localDirectory, String localName) {
		//- 文件
		File targetProcessFile = new File(localDirectory, localName);
		try {
			if (!targetProcessFile.exists()) {
				throw new FileBizException(
					String.format("本地目标上送文件不存在 localDir=%s , localName=%s", localDirectory, localName), "");
			}
			//- 创建连接
			connect();
			//- 登录ftp服务器
			login();
			//- 推送本地文件
			String configDir = FileConfigParser.textByNodeSystem(bizType, FileConfigContext.UPLOAD);
			String targetDirectory = FileClientFactory.createDirecotryByBizAndTime(bizType, configDir);
			push(targetProcessFile, targetDirectory, localName);
			//- 登出ftp服务器
			logout();
		} catch (Exception e) {
			
			if (e instanceof FileBizException) {
				throw (FileBizException) e;
			}
			
			throw new FileBizException(String.format("ftp文件下载过程中出现异常,详情：%s", e.getMessage()), "");
		} finally {
			if (client != null && (client.isConnected() || client.isAvailable())) {
				try {
					client.disconnect();
				} catch (IOException e) {
					logger.error("关闭ftp连接过程中出现异常,无后续操作", e);
				}
			}
			
		}
		
		return targetProcessFile;
	}
	
	/**
	 * 
	 * 检查FTP目录是否存在 不存在将依次创建目录
	 * 
	 */
	private String checkAndMakeFtpDirectory(String ftpDirectory) throws IOException {
		//目录容错处理
		if (!ftpDirectory.equals("/") && ftpDirectory.endsWith("/")) {
			ftpDirectory = ftpDirectory.substring(0, ftpDirectory.length() - 1);
		}
		
		if (!ftpDirectory.startsWith("/")) {
			ftpDirectory = "/" + ftpDirectory;
		}
		
		ftpDirectory = ftpDirectory.replaceAll("//", "/");
		ftpDirectory = ftpDirectory.replaceAll("\\\\", "/");
		
		//检查目录
		if (!dirExists(ftpDirectory)) {
			//依次检查和创建远程目录
			List<String> dirsPath = buildListDirs(ftpDirectory);
			for (String path : dirsPath) {
				if (!dirExists(path)) {
					try {
						client.makeDirectory(path);
					} catch (Exception e) {
						logger.error("抛异常,FTP远程目录创建失败:" + path, e);
						throw new FileBizException("FTP远程目录创建失败:" + path, "", e);
					}
				}
			}
		}
		
		return ftpDirectory;
	}
	
	private boolean dirExists(String dir) {
		try {
			return client.cwd(dir) < 300;
		} catch (IOException e) {
			return false;
		}
	}
	
	private static List<String> buildListDirs(String ftpDirectory) {
		LinkedList<String> pathList = new LinkedList<String>();
		pathList.add(ftpDirectory);
		while (ftpDirectory.lastIndexOf("/") != 0) {
			ftpDirectory = ftpDirectory.substring(0, ftpDirectory.lastIndexOf("/"));
			pathList.addFirst(ftpDirectory);
		}
		return pathList;
	}
	
	private void checkAndDeleteExistsFile(String ftpDirectory, final String targetName) throws IOException {
		FTPFile[] files = client.listFiles(ftpDirectory, file -> file != null && file.getName().equals(targetName));
		
		if (files != null && files.length != 0) {
			logger.warn("发现重复的上送文件，原始文件将被删除->targetDirectory={},targetName={}.", ftpDirectory, targetName);
			client.cwd(ftpDirectory);
			if (!client.deleteFile(targetName)) {
				throw new FileBizException("FTP删除远程重复文件失败..");
			}
		}
	}
	
	private void push(File targetProcessFile, String ftpDirectory, final String targetName) {
		
		InputStream input = null;
		boolean passiveModel = Boolean
			.parseBoolean(FileConfigParser.attrByNodeSystem(bizType, FileConfigParser.FTP, FileConfigParser.PASSIVE));
		if (passiveModel) {
			logger.info("FTP进入被动模式");
			client.enterLocalPassiveMode();
		}
		try {
			//检查目录是否存在，不存在将创建目录
			ftpDirectory = checkAndMakeFtpDirectory(ftpDirectory);
			if (!"/".equals(ftpDirectory)) {
				ftpDirectory += "/";
			}
			//- 检查文件是否存在,若存在同名文件，日志告警,并删除。
			checkAndDeleteExistsFile(ftpDirectory, targetName);
			
			//-上送文件
			input = new BufferedInputStream(new FileInputStream(targetProcessFile));
			if (client.cwd(ftpDirectory) >= 500) {
				throw new FileBizException(String.format("FTP远程创建过程失败->目录切换失败->ftpDirectory=%s ", ftpDirectory), "");
			}
			if (!client.storeFile(targetName, input)) {
				throw new FileBizException(
					String.format("FTP远程创建过程失败->targetDirectory=%s , targetName=%s", ftpDirectory, targetName), "");
			}
		} catch (Exception e) {
			if (e instanceof FileBizException) {
				throw (FileBizException) e;
			}
			e.printStackTrace();
			throw new FileBizException(String.format("FTP创建本地文件过程中出现错误...详情：%s", e.getMessage()), "");
		} finally {
			//- ftpclient不会自动关闭输入流
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					//nothing ...
				}
			}
		}
		
	}
	
	@Override
	public boolean checkFileFromFTP(String sourceDirectory, final String sourceName) {
		try {
			connect();
			login();
			boolean passiveModel = Boolean.parseBoolean(
				FileConfigParser.attrByNodeSystem(bizType, FileConfigParser.FTP, FileConfigParser.PASSIVE));
			if (passiveModel) {
				logger.info("FTP进入被动模式");
				client.enterLocalPassiveMode();
			}
			doCheckFileFromFTP(sourceDirectory, sourceName);
			
			logout();
		} catch (Exception e) {
			if (e instanceof FileBizException) {
				throw (FileBizException) e;
			}
			throw new FileBizException(String.format("FTP文件检查过程中出现错误...详情：%s", e.getMessage()), "", e);
		} finally {
			if (client != null && (client.isConnected() || client.isAvailable())) {
				try {
					client.disconnect();
				} catch (IOException e) {
					logger.error("关闭ftp连接过程中出现异常,无后续操作", e);
				}
			}
		}
		
		return false;
	}
	
	private void doCheckFileFromFTP(String sourceDirectory, final String sourceName) throws IOException {
		/*if (!sourceDirectory.startsWith("/")) {
			throw new FileBizException(String.format("文件路径要以/开头.sourceDirectory=%s", sourceDirectory));
		}*/
		FTPFile[] files = client.listFiles(sourceDirectory, file -> file.getName().equals(sourceName));
		if (null == files || 0 == files.length) {
			throw new FileBizException(
				String.format(
					"FTP文件不存在.host = %s ,username=%s,password=%s,port = %s ,source_directory = %s source_name = %s",
					host, username, ToString.mask(password), port, sourceDirectory, sourceName),
				StatusConsts.FTP_FILE_NOT_FOUND);
		}
	}
}
