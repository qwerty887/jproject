package org.jproject.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.regex.Pattern;

@Converter
public class ConverterBooleanToBoolean implements AttributeConverter<Boolean, Boolean> {


    @Override
    public Boolean convertToDatabaseColumn(Boolean aBoolean) {
        if (aBoolean == null) {
            return false;
        }
        return aBoolean;
    }

    @Override
    public Boolean convertToEntityAttribute(Boolean aBoolean) {
        if (aBoolean == null) {
            return false;
        }
        return aBoolean;

    }
}
