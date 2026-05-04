package org.jproject.dto.controllers;

import org.jproject.domain.TFileGroup;

public class DtoFileGroup {

    Integer id;
    String name;
    String linkPath;

    public DtoFileGroup() {

    }

    public DtoFileGroup(Integer id, String name, String linkPath) {
        this.id = id;
        this.name = name;
        this.linkPath = linkPath;
    }

    public static DtoFileGroup of(TFileGroup entity) {
        return new DtoFileGroup(entity.getId(), entity.getName(), entity.getLinkPath());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }
}
