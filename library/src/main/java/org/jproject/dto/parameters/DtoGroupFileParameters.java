package org.jproject.dto.parameters;

import org.jproject.domain.TFile;

import java.nio.file.Path;

public class DtoGroupFileParameters {

    Path path;

    public DtoGroupFileParameters() {

    }

    public DtoGroupFileParameters(Path path) {
        this.path = path;
    }

    public static DtoGroupFileParameters of(TFile tfile) {
        return new DtoGroupFileParameters(tfile.getPath());
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
