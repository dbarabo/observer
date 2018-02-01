package ru.barabo.observer.config.task.p440.load.xml.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import ru.barabo.observer.config.task.p440.load.xml.impl.Account;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


@XStreamAlias("����������")
public final class ExtractRequest extends AbstractRequest {

	@XStreamAlias("��������")
	private Period period;

	@XStreamAlias("������")
	private Integer isAll;

	@XStreamImplicit(itemFieldName = "�����������")
	private List<Account> accounts;

	@Override
	public String getAccounts() {
		if ((isAll != null && isAll.intValue() == 1) || accounts == null || accounts.size() == 0) {
			return null;
		}

		return accounts.stream().map(Account::getCode).collect(Collectors.joining(";"));
	}

	@Override
	public Date getAddDate() {
		return period == null ? null : period.getFromDate();
	}

	@Override
	public Date getSubDate() {
		return period == null ? null : period.getToDate();
	}

}
