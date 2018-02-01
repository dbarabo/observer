package ru.barabo.observer.config.task.p440.out.xml.extract;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

@XStreamAlias("���������")
public class AmountInfo {

	@XStreamAlias("�����")
	private String debet;

	@XStreamAlias("������")
	private String credit;

	public AmountInfo(Number debet, Number credit) {

		this.debet = XmlLoader.formatSum(debet, "0.00");

		this.credit = XmlLoader.formatSum(credit, "0.00");
	}
}
