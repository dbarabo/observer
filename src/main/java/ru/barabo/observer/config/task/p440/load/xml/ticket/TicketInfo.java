package ru.barabo.observer.config.task.p440.load.xml.ticket;

import java.util.Date;
import java.util.List;

/**
 * информация о квитке/извещении
 * 
 * @author debara
 *
 */
public interface TicketInfo {

	/**
	 * @return имя файла на кот. пришел этот квиток
	 */
	String getResposeFileName();

	/**
	 * @return дату-время в квитанции
	 */
	Date getDateTimeTicket();

	/**
	 * @return код(ы) рез-тата проверки
	 */
	List<String> getCodes();

	List<String> getErrorDescriptions();

	/**
	 * @return список непраильных атрибутов
	 */
	default List<String> getErrorAtributes() {

		return null;
	}

	/**
	 * @return список непраильных значений атрибутов
	 */
	default List<String> getErrorValues() {

		return null;
	}
}
