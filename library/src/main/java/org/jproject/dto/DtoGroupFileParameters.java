package org.jproject.dto;

import org.jproject.domain.TFile;

import java.nio.file.Path;

public class DtoGroupFileParameters {

    String md5;
    Path path;

    public DtoGroupFileParameters() {

    }

    public DtoGroupFileParameters(String md5, Path path) {
        this.md5 = md5;
        this.path = path;
    }

    public static DtoGroupFileParameters of(TFile tfile) {
        return new DtoGroupFileParameters(tfile.getMd5(), tfile.getFileHist().getPath());
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
