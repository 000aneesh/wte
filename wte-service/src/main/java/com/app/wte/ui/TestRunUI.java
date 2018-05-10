package com.app.wte.ui;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
public class TestRunUI implements Serializable {

    @NotNull
    private int id = -1;

    @NotNull
    @Size(min = 2, message = "TestCase name must have at least two characters")
    private String testCase = "";
    

//    private Set<String> templateName = new HashSet<>();

    @NotNull
    private String templateName;

    public int getId() {
        return id;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }


   /* public Set<String> getTemplateName() {
        return templateName;
    }

    public void setTemplateName(Set<String> templateName) {
        this.templateName = templateName;
    }*/

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
