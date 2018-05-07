package com.app.wte.ui;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TestCaseUI implements Serializable {

    @NotNull
    private int id = -1;

    @NotNull
    @Size(min = 2, message = "TestCase name must have at least two characters")
    private String testCase = "";
    

//    private Set<String> templateName = new HashSet<>();

    @Min(value = 0, message = "Can't have negative amount in stock")
    private int stockCount = 0;
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

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
