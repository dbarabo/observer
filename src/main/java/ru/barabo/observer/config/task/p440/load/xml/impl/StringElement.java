package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamConverter(value= ToAttributedValueConverter.class, strings={"value"})
public class StringElement {
    public String value;

    public StringElement(String value) {
        this.value = value;
    }
}
