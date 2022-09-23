package ru.barabo.observer.config.task.p311.v512;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("ИННИО")
@XStreamConverter(value= ToAttributedValueConverter.class, strings={"value"})
public class InnIo {

     String value;

     public InnIo(String value) {
         this.value = value;
     }
}
