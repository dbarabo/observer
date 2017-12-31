package ru.barabo.observer.config.cbr.other.task.cec;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;


/**
 * // Файл
 * @author debara
 *
 */

@XStreamAlias("Файл")
public class FileXml {

	@XStreamAlias("ИдФайл")
	private String idFile; // ИдФайл
	
	private String versNo;// ВерсФорм
	private String typeInfo; // ТипИнф
	private String versProga;// ВерсПрог 

	@XStreamAlias("Запрос")
	private Zapros zapros; // --Запрос
	
	@XStreamImplicit(itemFieldName="Персона")
	private List<Person> persons;//Персона +
	
	
	public String getIdFile() {
		return idFile;
	}

	public String getVersNo() {
		return versNo;
	}

	public String getTypeInfo() {
		return typeInfo;
	}

	public String getVersProga() {
		return versProga;
	}

	public Zapros getZapros() {
		return zapros;
	}
	
	public List<Person> getPersons() {
		return persons;
	}
}
