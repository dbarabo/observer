package ru.barabo.observer.config.task.p440.out.xml.extract;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.barabo.p440.out.data.AdditionalResponseData;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("ВЫПБНДОПОЛ")
public class AddExtractInfoPart {

	@XStreamAlias("ПорНом")
	private String orderAccountFile;

	@XStreamAlias("ПорНомДФ")
	private String orderNumberFile;

	@XStreamAlias("НомСч")
	private String account;

	@XStreamImplicit(itemFieldName = "Операции")
	private List<OperationAccount> operationAccountList;

	public AddExtractInfoPart( AdditionalResponseData additionalResponseData) {

		this.orderAccountFile = String.format("%06d", additionalResponseData.getOrderAccount() );

		this.orderNumberFile = String.format("%06d", additionalResponseData.getOrderNumberFile());

		this.account = additionalResponseData.getAccount().getCode();

		operationAccountList = initOperation(additionalResponseData);
	}

	private List<OperationAccount> initOperation(AdditionalResponseData additionalResponseData) {

		List<Object[]> rows = additionalResponseData.getMainResponseData().getOperationAccount(
				additionalResponseData.getAccount(), additionalResponseData.getPositionOperation());

		if(rows.isEmpty()) {
			return null;
		}

		List<OperationAccount> operations = new ArrayList<>();

		for (int index = 0; index < rows.size() && index < MAX_OPER_COUNT_EXTRACT; index++) {

			Object[] row = rows.get(index);

			OperationAccount operation = OperationAccount.create(index + 1, row);

			operations.add(operation);
		}

		return operations;
	}

	static private  int MAX_OPER_COUNT_EXTRACT = 1000;

	public int getOperationAccountListSize() {
		return operationAccountList == null ? 0 : operationAccountList.size();
	}

	public String getOrderAccountFile() {
		return orderAccountFile;
	}

	public String getOrderNumberFile() {
		return orderNumberFile;
	}

}
