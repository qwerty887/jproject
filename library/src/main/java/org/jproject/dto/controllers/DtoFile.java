package org.jproject.dto.controllers;

import org.jproject.domain.TFile;

import java.nio.file.Path;

public class DtoFile {

    Integer id;
    Path path;

    public DtoFile() {

    }

    public DtoFile(Integer id, Path path) {
        this.id = id;
        this.path = path;
    }

    public static DtoFile of(TFile entity) {
        return new DtoFile(entity.getId(),
                           entity.getPath()
                            );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
