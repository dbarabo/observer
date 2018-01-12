package ru.barabo.observer.config.task.p440.load.xml.impl;


public enum PayerType {

	Juric(0),
	Pboul(1),
	Physic(2),
	None(-1);

	private int valueDb;

	private PayerType(int valueDb) {
		this.valueDb = valueDb;
	}

	public int getValueDb() {
		return valueDb;
	}
	
	public static PayerType getPayerTypeByDbValue(int value) {

		for (PayerType payerType : PayerType.values()) {
			if (payerType.getValueDb() == value) {
				return payerType;
			}
		}
		return null;
	}

}
