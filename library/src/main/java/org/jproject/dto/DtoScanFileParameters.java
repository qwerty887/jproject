package org.jproject.dto;

import java.nio.file.Path;

public class DtoScanFileParameters {

    Path path;

    public DtoScanFileParameters() {

    }

    public DtoScanFileParameters(Path path) {
        this.path = path;
    }

    public static DtoScanFileParameters of(Path path) {
        return new DtoScanFileParameters(path);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
