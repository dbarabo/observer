package ru.barabo.observer.config.cbr.f101;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.log4j.Logger;
import ru.barabo.observer.config.task.p440.load.XmlLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class XmlLoaderForm101<E> {

    final static transient private Logger logger = Logger.getLogger(XmlLoader.class.getName());

    static private XStream getXStream() {
        XStream xstream = new XStream(new DomDriver("UTF-8"));


        xstream.processAnnotations(F101Xml.class);

        xstream.processAnnotations(Creator.class);
        //xstream.alias("Составитель", Creator.class);

        xstream.processAnnotations(Balance.class);
        //xstream.alias("Баланс", Balance.class);

        xstream.processAnnotations(Data101.class);
        xstream.processAnnotations(OffBalance.class);
        xstream.processAnnotations(OffBalanceGroup.class);
        xstream.processAnnotations(RowDataBalance.class);
        xstream.processAnnotations(TotalBalance.class);
        xstream.processAnnotations(TotalOffBalance.class);
        xstream.processAnnotations(TotalOffBalanceGroup.class);
        xstream.processAnnotations(TotalTime.class);
        xstream.processAnnotations(TotalTrust.class);
        xstream.processAnnotations(TimeAccount.class);

        xstream.processAnnotations(Trust.class);
        xstream.processAnnotations(InfoPc.class);

        xstream.processAnnotations(Mainer.class);
        xstream.processAnnotations(MainBuh.class);
        xstream.processAnnotations(Executor.class);

        xstream.useAttributeFor(String.class);
        xstream.useAttributeFor(Integer.class);

        return xstream;
    }

    public E load(File file) {

        FileInputStream fl = null;
        E objectXml = null;

        try {
            fl = new FileInputStream(file);

            //logger.error("file.getName=" + file.getName());

            XStream xstream = getXStream();

            objectXml = (E) xstream.fromXML(fl);

            fl.close();
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException load", e);
            return null;

        } catch (IOException e) {
            logger.error("IOException load", e);
            return null;
        }

        return objectXml;
    }
}
