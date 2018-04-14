package com.app.wte.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

import com.app.wte.constants.WTEConstants;
import com.app.wte.model.TestRunRequest;
import com.app.wte.service.Task;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Service(value = "fTPTransferTask")
public class FTPTransferTask implements Task {

	@Override
	public void execute(TestRunRequest testRunRequest) {
		try {
			String inputFile = testRunRequest.getGeneratedFile();
			int indexOfUnderScore = inputFile.lastIndexOf(WTEConstants.TXT_FILE_EXTN);
			String dummyFtpPath = inputFile.substring(0, indexOfUnderScore) + WTEConstants.UNDERSCORE + "ftp"
					+ inputFile.substring(indexOfUnderScore);
			File sourceFile = new File(inputFile);
			File destFile = new File(dummyFtpPath);
			Files.copy(sourceFile.toPath(), destFile.toPath());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// inputFile = testRunRequest.getGeneratedFile()
	private void copyToServer(String inputFilePath, String ftpFilePath) {
		int portNumber = Integer.parseInt("port");
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		boolean copyToFtp = false;
		try {
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
			File file = new File(inputFilePath);
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
