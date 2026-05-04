package org.jproject.dto.controllers;

import org.jproject.domain.TFile;
import org.jproject.domain.TFileHist;

import java.nio.file.Path;
import java.util.Optional;

public class DtoFile {

    Integer id;
    String md5;
    Path path;

    public DtoFile() {

    }

    public DtoFile(Integer id, String md5, Path path) {
        this.id = id;
        this.md5 = md5;
        this.path = path;
    }

    public static DtoFile of(TFile entity) {
        return new DtoFile(entity.getId(),
                           entity.getMd5(),
                           Optional.of(entity.getFileHist()).map(TFileHist::getPath).orElse(null)
                            );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
