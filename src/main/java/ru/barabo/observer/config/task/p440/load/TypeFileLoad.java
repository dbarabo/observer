package ru.barabo.observer.config.task.p440.load;


import ru.barabo.observer.config.task.p440.load.xml.apx.ApnFromFns;
import ru.barabo.observer.config.task.p440.load.xml.apx.ApoFromFns;
import ru.barabo.observer.config.task.p440.load.xml.apx.ApzFromFns;
import ru.barabo.observer.config.task.p440.load.xml.decision.RooFromFns;
import ru.barabo.observer.config.task.p440.load.xml.decision.RpoFromFns;
import ru.barabo.observer.config.task.p440.load.xml.pno.PnoFromFns;
import ru.barabo.observer.config.task.p440.load.xml.request.ZsnFromFns;
import ru.barabo.observer.config.task.p440.load.xml.request.ZsoFromFns;
import ru.barabo.observer.config.task.p440.load.xml.request.ZsvFromFns;
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.IzvFromFns;
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.KwtFromFns;

/**
 * типы загружаемых файлов из ФНС
 * 
 * @author debara
 *
 */
public enum TypeFileLoad {


	PNO(PnoFromFns.class, "PNO"),

	RPO(RpoFromFns.class, "RPO"),

	ROO(RooFromFns.class, "ROO"),

	ZSN(ZsnFromFns.class, "ZSN"),

	ZSO(ZsoFromFns.class, "ZSO"),

	ZSV(ZsvFromFns.class, "ZSV"),

	IZV(IzvFromFns.class, "IZV"),

	APN(ApnFromFns.class, "APN"),

	APZ(ApzFromFns.class, "APZ"),

	KWT(KwtFromFns.class, "KWT"),

	APO(ApoFromFns.class, "APO");

	private Class clazz;

	private String prefixFile;

	private TypeFileLoad(Class clazz, String prefixFile) {
		this.clazz = clazz;
		this.prefixFile = prefixFile;
	}

	public Class getClazz() {
		return clazz;
	}

	public String getPrefixFile() {
		return prefixFile;
	}

}
