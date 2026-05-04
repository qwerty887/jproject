package org.jproject.dto.parameters;

import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.dto.controllers.DtoFile;
import org.jproject.dto.controllers.DtoFileGroup;

import java.util.List;

public class DtoLinkFileParameters {

    DtoFile file;
    List<DtoFileGroup> fileGroupList;

    public DtoLinkFileParameters() {

    }

    public DtoLinkFileParameters(DtoFile file, List<DtoFileGroup> fileGroupList) {
        this.file = file;
        this.fileGroupList = fileGroupList;
    }

    public static DtoLinkFileParameters of(TFile tFile, List<TFileGroup> tFileGroupList) {
        return new DtoLinkFileParameters(
                DtoFile.of(tFile),
                tFileGroupList.stream().map(DtoFileGroup::of).toList()
        );
    }

    public DtoFile getFile() {
        return file;
    }

    public void setFile(DtoFile file) {
        this.file = file;
    }

    public List<DtoFileGroup> getFileGroupList() {
        return fileGroupList;
    }

    public void setFileGroupList(List<DtoFileGroup> fileGroupList) {
        this.fileGroupList = fileGroupList;
    }
}
