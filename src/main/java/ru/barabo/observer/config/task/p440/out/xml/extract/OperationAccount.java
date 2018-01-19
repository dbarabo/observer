package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.util.Date;

@XStreamAlias("Операции")
public class OperationAccount {

	final static transient private Logger logger = Logger.getLogger(OperationAccount.class
			.getName());

	@XStreamAlias("ИдБлок")
	private Integer orderFile;

	@XStreamAlias("ДатаОпер")
	private String operDate;

	@XStreamAlias("НазнПл")
	private String description;

	@XStreamAlias("РеквДок")
	private DocumentInfo documentInfo;

	@XStreamAlias("РеквБанка")
	private BankInfo bankInfo;

	@XStreamAlias("РеквПлат")
	private PayerInfo payerInfo;

	@XStreamAlias("СуммаОпер")
	private AmountInfo amountInfo;

	private OperationAccount(Integer orderFile) {
		this.orderFile = orderFile;
	}

	public static OperationAccount create(Integer orderFile, Object[] row) {

		OperationAccount operation = new OperationAccount(orderFile);

		operation.operDate = XmlLoader.formatDate((Date) row[0]);

		operation.description = (String) row[1];

		operation.documentInfo = new DocumentInfo((String) row[2], (String) row[3], (Date) row[4]);

		operation.bankInfo = new BankInfo((String) row[5], (String) row[6], (String) row[7]);

		operation.payerInfo = new PayerInfo((String) row[8], (String) row[9], (String) row[10],
				(String) row[11]);

		operation.amountInfo = new AmountInfo((Number) row[12], (Number) row[13]);

		/* logger.info("OperationAccount.create operDate=" + operation.operDate
		 * + ",amountInfo=" + operation.amountInfo); */

		return operation;
	}
}
