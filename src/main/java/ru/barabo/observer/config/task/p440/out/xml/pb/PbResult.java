package ru.barabo.observer.config.task.p440.out.xml.pb;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Результат")
public class PbResult {

	@XStreamAlias("КодРезПроверки")
	private String code;
	
	@XStreamAlias("Пояснение")
	private String description;

	@XStreamAlias("КодРекв")
	private String attributeCode;

	@XStreamAlias("ЗначРекв")
	private String attributeValue;

	public PbResult(String code, String description, String attributeCode, String attributeValue) {

		this.code = code;
		this.description = description;
		this.attributeCode = attributeCode;
		this.attributeValue = attributeValue;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getAttributeCode() {
		return attributeCode;
	}

	public String getAttributeValue() {
		return attributeValue;
	}
}
