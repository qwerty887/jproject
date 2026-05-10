package org.jproject.dto.parameters;

import org.jproject.domain.TFile;

import java.nio.file.Path;

public class DtoLinkFileParameters {

    Path path;

    public DtoLinkFileParameters() {

    }

    public DtoLinkFileParameters(Path path) {
        this.path = path;
    }

    public static DtoLinkFileParameters of(TFile file) {
        return new DtoLinkFileParameters(file.getPath());
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
