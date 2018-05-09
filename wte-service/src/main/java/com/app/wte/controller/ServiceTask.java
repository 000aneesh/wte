package com.app.wte.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.app.wte.model.ExecutionContext;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

@Controller
public class ServiceTask {

	@Value("${jsonPath}")
	private String jsonPath;

	@Value("${soapFileName}")
	private String soapFileName;

	@Value("${soapTemplatePath}")
	private String soapTemplatePath;

	@Value("${soapTemplateSource}")
	private String soapTemplateSource;

	@Value("${wsdl}")
	private String wsdl;

	private static final Logger logger = LoggerFactory.getLogger(ServiceTask.class);

	@RequestMapping(value = "/testService")
	public void testService() {
		try {
			displayDirectoryContents(new File(jsonPath), "CONSREQ_207616413.TXT");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executeP4(ExecutionContext executionContext, String fileName) {

		try {
			displayDirectoryContents(new File(jsonPath), fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void displayDirectoryContents(File dir, String fileName) {
		String status = null;
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {

					displayDirectoryContents(file, fileName);
				} else {
					logger.info("file name:" + file.getParentFile());
					String path = file.getParentFile().toString();
					if (path.contains("success")) {
						status = "success";
					} else if (path.contains("failure")) {
						status = "failure";
					} else if (path.contains("retry")) {
						status = "retry";
					}
					try {

						readJson(file.getCanonicalPath(), status, fileName);

					} catch (JsonProcessingException e) {
						logger.error(file.getName() + " : json is INVALID");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readJson(String file, String status, String fileName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		logger.info("status : " + status);
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String currentLine;
		while ((currentLine = bufferedReader.readLine()) != null) {
			if (currentLine.contains(fileName)) {
				Map<?, ?> map = mapper.readValue(currentLine, Map.class);
				for (Map.Entry<?, ?> entry : map.entrySet()) {
					logger.info(entry.getKey() + "=" + entry.getValue() + "\n");
					processResponseInfo(mapper, entry);

				}
			}
		}
		bufferedReader.close();

	}

	private void processResponseInfo(ObjectMapper mapper, Map.Entry<?, ?> entry)
			throws IOException, JsonParseException, JsonMappingException {
		if (entry.getKey().equals("response_info")) {

			JsonNode rootNode = mapper.readTree(entry.getValue().toString());
			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
			while (fieldsIterator.hasNext()) {

				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				processIdentifiers(field);

			}

		}
	}

	private void processIdentifiers(Map.Entry<String, JsonNode> field)
			throws IOException, JsonParseException, JsonMappingException {
		if (field.getKey().equalsIgnoreCase("identifiers")) {
			final ObjectNode node = new ObjectMapper().readValue(field.getValue().toString(), ObjectNode.class);
			if (node.has("contactPointId")) {
				logger.info("contactPointId: " + node.get("contactPointId"));

				JsonNode jsonNode = node.get("contactPointId");
				String leadID = jsonNode.asText();
				callWebService(leadID);
			}
			if (node.has("consumerId")) {
				logger.info("consumerId: " + node.get("consumerId"));
			}
		}
	}

	public void callWebService(String leadId) {

		Charset charset = StandardCharsets.UTF_8;
		String content;
		try {

			templateProcess(leadId);
			Path path = Paths.get(soapFileName);
			content = new String(Files.readAllBytes(path), charset);
			logger.info("Request" + content);
			StringEntity stringEntity = new StringEntity(content, "UTF-8");
			stringEntity.setChunked(true);

			HttpPost httpPost = new HttpPost(wsdl);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "text/xml");
			httpPost.addHeader("SOAPAction", "");
			httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			String strResponse = null;
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
				logger.info("Response" + strResponse);
			}
		} catch (IOException e) {
			logger.info("IO Exception" + e.getMessage());
			e.printStackTrace();
		}

		String response;
		try {
			response = mockSOAPResponse();
			response.trim();
			//getFullNameFromXml(response, "lead");
			parseXML(response, "lead");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String mockSOAPResponse() throws IOException {
		Charset charset = StandardCharsets.UTF_8;
		Path path = Paths.get("D:\\Json\\response.xml");
		String content = new String(Files.readAllBytes(path), charset);
		return content;

	}

	public void parseXML(String response, String tagName) {
		Document doc;
		try {
			doc = loadXMLString(response);
			NodeList nList = doc.getElementsByTagName(tagName);

			for (int i = 0; i < nList.getLength(); i++) {

				Element e = (Element) nList.item(i);
				logger.info("Response" + e.getFirstChild().getNodeName().trim());
				/*if (e.getAttribute("rollno").equals("393")) {
					String s = e.getFirstChild().getNodeName().trim();
				}*/
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	

	public Document loadXMLString(String response) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(response));

		return db.parse(is);
	}

	public List<String> getFullNameFromXml(String response, String tagName) throws Exception {
		Document xmlDoc = loadXMLString(response);
		NodeList nodeList = xmlDoc.getElementsByTagName(tagName);
		List<String> ids = new ArrayList<String>(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node x = nodeList.item(i);
			ids.add(x.getFirstChild().getNodeValue());
			System.out.println(nodeList.item(i).getFirstChild().getNodeValue());
		}
		return ids;
	}

	private void templateProcess(String leadId) {
		Writer file = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		Configuration cfg = new Configuration();
		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		Map<String, Object> parameterMap = new HashMap<String, Object>();

		try {
			file = new FileWriter(soapFileName, false);
			bw = new BufferedWriter(file);
			pw = new PrintWriter(bw);
			cfg.setDirectoryForTemplateLoading(new File(soapTemplateSource));
			Template template = cfg.getTemplate(soapTemplatePath);
			parameterMap.put("leadId", leadId);
			template.process(parameterMap, file);
			pw.println("");
		} catch (IOException | TemplateException e) {
			System.out.println("Exception occurred-Template not found" + e);

		} finally {
			pw.close();
			try {
				bw.close();
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
