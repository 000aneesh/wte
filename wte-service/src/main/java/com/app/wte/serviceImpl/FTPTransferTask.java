package com.app.wte.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

import com.app.wte.constants.WTEConstants;
import com.app.wte.model.TestResult;
import com.app.wte.service.Task;
import com.app.wte.util.WTEUtils;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service(value = "fTPTransferTask")
public class FTPTransferTask implements Task {

	@Override
	public void execute(TestResult testResult) {
		try {
			String uploadPath = WTEUtils.getUploadPath();
			String srcFile = uploadPath + testResult.getFileLocation() + File.separator + WTEConstants.TEST_INPUT_FILE;
			String ftpFilePath = WTEUtils.getExternalProperty("ftpFilePath");
			File sourceFile = new File(srcFile);
			File destFile = new File(ftpFilePath);
			Files.copy(sourceFile.toPath(), destFile.toPath());
			testResult.getTaskSatusMap().put("FTPTransferTask", "Success");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			testResult.getTaskSatusMap().put("FTPTransferTask", "Failure");
			e.printStackTrace();
		}

	}

	private void copyToServer(TestResult testResult) {
		int portNumber = Integer.parseInt("port");
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		boolean copyToFtp = false;
		try {
			String srcFile = testResult.getFileLocation() + File.separator + WTEConstants.TEST_INPUT_FILE;
			String ftpFilePath = WTEUtils.getExternalProperty("ftpFilePath");
			JSch jsch = new JSch();
			session = jsch.getSession("userName", "host", portNumber);
			session.setPassword("password");
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications", "password");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			File file = new File(srcFile);
			if (file.isFile()) {
				String filename = file.getAbsolutePath();
				channelSftp.put(filename, ftpFilePath, ChannelSftp.OVERWRITE);
				System.out.println(filename + " transferred to ");
				copyToFtp = true;
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			channelSftp.exit();
			channel.disconnect();
			session.disconnect();
		}
	}
}
