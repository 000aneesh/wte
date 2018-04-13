package com.app.wte.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.app.wte.model.TestRunRequest;
import com.app.wte.service.Task;

@Service(value = "fTPTransferTask")
public class FTPTransferTask implements Task {

	@Value("${destFile}")
	private String destFile;

	@Value("${ftpFilePath}")
	private String ftpFilePath;

	@Override
	public void execute(TestRunRequest testRunRequest) {
		try {
			File sourceFile = new File(destFile);
			File destFile = new File(ftpFilePath);
			Files.copy(sourceFile.toPath(), destFile.toPath());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void copyToServer() {
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
			File file = new File(destFile);
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
