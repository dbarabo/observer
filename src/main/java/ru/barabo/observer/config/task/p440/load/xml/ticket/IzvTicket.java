package ru.barabo.observer.config.task.p440.load.xml.ticket;

import java.util.Date;

/**
 * извещение на архив из ЦБ
 * 
 * @author debara
 *
 */
public interface IzvTicket extends TicketInfo {

	/**
	 * @return Дата и время окончания периода согласно графику, установленному
	 *         Положением № 440-П, в течение и по завершении которого банку
	 *         предоставляется возможность получить архивный файл
	 */
	Date getAccessDateTime();
}
