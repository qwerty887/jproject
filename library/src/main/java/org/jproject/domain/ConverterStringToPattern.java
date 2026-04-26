package org.jproject.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.regex.Pattern;

@Converter
public class ConverterStringToPattern implements AttributeConverter<String, Pattern> {

    @Override
    public Pattern convertToDatabaseColumn(String s) {
        return s != null ? Pattern.compile(Pattern.quote(s), Pattern.CASE_INSENSITIVE) : null;
    }

    @Override
    public String convertToEntityAttribute(Pattern pattern) {
        return pattern != null ? pattern.pattern() : null;
    }
}
