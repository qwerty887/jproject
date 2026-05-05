package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.dto.parameters.DtoLinkFileParameters;
import org.jproject.parameters.process.base.BaseProcessParam;

import java.util.List;

public class FileLinkingProcessParameters extends BaseProcessParam {

    List<DtoLinkFileParameters> files;

    public FileLinkingProcessParameters() {
        super(EProcessType.LINK);
    }

    public FileLinkingProcessParameters(List<DtoLinkFileParameters> files) {
        super(EProcessType.LINK);
        this.files = files;
    }

    public List<DtoLinkFileParameters> getFiles() {
        return files;
    }

    public void setFiles(List<DtoLinkFileParameters> files) {
        this.files = files;
    }
}
