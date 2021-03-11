package ru.barabo.observer.config.task.p440.load.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract public class AbstractFromFns implements MainParamsQuery {

	final static transient protected Logger logger = Logger.getLogger(AbstractFromFns.class
			.getName());

	@XStreamAlias("ДолжнОтпр")
	protected String fnsPost;

	@XStreamAlias("ТелОтпр")
	protected String fnsPhone;

	@XStreamAlias("ФамОтпр")
	protected String fnsFio;

	@XStreamAlias("ТипИнф")
	protected String typeInfo;

	abstract protected FromFnsInfo getFromFnsInfo();

	private static final String COLUMNS = "FNS_POST, FNS_PHONE, FNS_FIO, TYPE_440P, BANK_BIK, BANK_NAME, FNS_CODEID, FNS_NAME, "
			+ "MAIN_NUMBER, MAIN_DATE, MAIN_CODE, MAIN_DESCRIPTION, MAIN_SUM, MAIN_TYPE, MAIN_STATUS, "
			+ "ACCOUNTS, CARDS, CARDS_CURRENCY, ADD_NUMBER, ADD_DATE, SUB_NUMBER, SUB_DATE, ACCOUNTS_START_DATE, ACCOUNTS_END_DATE, "
			+ "FILE_NAME, ID";

	@Override
	public String getListColumns() {

		return COLUMNS;
	}

	@Override
	public ParamsQuery getPayer() {

		return getFromFnsInfo() == null ? null : getFromFnsInfo().getPayer();
	}

	@Override
	public List<Object> getParams() {

		FromFnsInfo fromFnsInfo = getFromFnsInfo();

		if (fromFnsInfo == null || fromFnsInfo.getBank() == null || fromFnsInfo.getFns() == null
				/*|| fromFnsInfo.getPayer() == null*/) {
			return null;
		}

		return new ArrayList<Object>(Arrays.asList(
				fnsPost == null ? String.class : fnsPost,
				fnsPhone == null ? String.class : fnsPhone,
				fnsFio == null ? String.class : fnsFio,
				typeInfo == null ? String.class : typeInfo,
				fromFnsInfo.getBank().getBik() == null ? String.class : fromFnsInfo.getBank()
						.getBik(),
				fromFnsInfo.getBank().getName() == null ? String.class : fromFnsInfo.getBank()
						.getName(),
				fromFnsInfo.getFns().getFnsCode() == null ? String.class : fromFnsInfo.getFns()
						.getFnsCode(),
				fromFnsInfo.getFns().getNameFns() == null ? String.class : fromFnsInfo.getFns()
						.getNameFns(),
				fromFnsInfo.getMainNumber() == null ? String.class : fromFnsInfo.getMainNumber(),
				fromFnsInfo.getMainDate() == null ? Date.class : fromFnsInfo.getMainDate(),
				fromFnsInfo.getMainCode() == null ? String.class : fromFnsInfo.getMainCode(),
				fromFnsInfo.getMainDescription() == null ? String.class : fromFnsInfo
						.getMainDescription(),
				fromFnsInfo.getMainSum() == null ? Double.class : fromFnsInfo.getMainSum(),
				fromFnsInfo.getMainType() == null ? String.class : fromFnsInfo.getMainType(),
				fromFnsInfo.getMainStatus() == null ? String.class : fromFnsInfo.getMainStatus(),
				fromFnsInfo.getAccounts() == null ? String.class : fromFnsInfo.getAccounts(),
				fromFnsInfo.getCards() == null ? String.class : fromFnsInfo.getCards(),
				fromFnsInfo.getCardsCurrency() == null ? String.class : fromFnsInfo.getCardsCurrency(),
				fromFnsInfo.getAddNumber() == null ? String.class : fromFnsInfo.getAddNumber(),
				fromFnsInfo.getAddDate() == null ? Date.class : fromFnsInfo.getAddDate(),
				fromFnsInfo.getSubNumber() == null ? String.class : fromFnsInfo.getSubNumber(),
				fromFnsInfo.getSubDate() == null ? Date.class : fromFnsInfo.getSubDate(),

				fromFnsInfo.getAccountsStartDates(),
				fromFnsInfo.getAccountsEndDates()
		));
	}

	public String getFnsPost() {
		return fnsPost;
	}

	public void setFnsPost(String fnsPost) {
		this.fnsPost = fnsPost;
	}

	public String getTypeInfo() {
		return typeInfo;
	}

	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}

	public String getFnsPhone() {
		return fnsPhone;
	}

	public String getFnsFio() {
		return fnsFio;
	}
}

