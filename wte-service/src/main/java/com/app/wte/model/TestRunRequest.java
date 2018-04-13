package com.app.wte.model;

public class TestRunRequest {

	private String testCase;
	private String inputFile;
	private String template;
	private String generatedFile;

	/**
	 * @return the testCase
	 */
	public String getTestCase() {
		return testCase;
	}

	/**
	 * @param testCase
	 *            the testCase to set
	 */
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	/**
	 * @return the inputFile
	 */
	public String getInputFile() {
		return inputFile;
	}

	/**
	 * @param inputFile
	 *            the inputFile to set
	 */
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @param template
	 *            the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * @return the generatedFile
	 */
	public String getGeneratedFile() {
		return generatedFile;
	}

	/**
	 * @param generatedFile
	 *            the generatedFile to set
	 */
	public void setGeneratedFile(String generatedFile) {
		this.generatedFile = generatedFile;
	}

}
