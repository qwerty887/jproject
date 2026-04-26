package org.jproject.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.file.Path;

@Converter
public class ConverterPathToString implements AttributeConverter<Path, String> {


    @Override
    public String convertToDatabaseColumn(Path path) {
        if (path == null) {
            return null;
        }

        return path.toFile().getAbsolutePath();
    }

    @Override
    public Path convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return Path.of(s);
    }
}
