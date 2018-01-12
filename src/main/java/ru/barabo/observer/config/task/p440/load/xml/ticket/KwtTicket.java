package ru.barabo.observer.config.task.p440.load.xml.ticket;

import java.util.List;

/**
 * квитанция на файл из ФНС
 * 
 * @author debara
 *
 */
public interface KwtTicket extends TicketInfo {

	/**
	 * @return коды ошибочных реквизитов
	 */
	List<String> getErrorCodeAttributes();

	/**
	 * @return значения ошибочных реквизитов
	 */
	List<String> getErrorValueAttributes();
}
