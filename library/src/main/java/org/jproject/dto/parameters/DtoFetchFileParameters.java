package org.jproject.dto.parameters;

import java.nio.file.Path;

public class DtoFetchFileParameters {

    Path path;
    Path excludePath;

    public DtoFetchFileParameters() {

    }

    public DtoFetchFileParameters(Path path, Path excludePath) {
        this.path = path;
        this.excludePath = excludePath;
    }

    public static DtoFetchFileParameters of(Path path, Path excludePath) {
        return new DtoFetchFileParameters(path, excludePath);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(Path excludePath) {
        this.excludePath = excludePath;
    }
}
