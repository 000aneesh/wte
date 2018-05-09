package com.app.wte.type;

public enum TemplatePatternType {

	EQFA_7260_RESULTS("fresh address email quality"),
	STR_FLG_API("stars flag"),
	Membership("Membership");
	
	private String templatePatternType;
	
	public String getTemplatePatternType()
    {
        return this.templatePatternType;
    }
 
    private TemplatePatternType(String templatePatternType)
    {
        this.templatePatternType = templatePatternType;
    }
}
