package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.dto.DtoGroupFileParameters;
import org.jproject.parameters.process.base.BaseProcessParam;

import java.util.List;

public class FileGroupingProcessParameters extends BaseProcessParam {

    List<DtoGroupFileParameters> files;

    public FileGroupingProcessParameters() {
        super(EProcessType.FILE_GROUPING);
    }

    public FileGroupingProcessParameters(List<DtoGroupFileParameters> files) {
        super(EProcessType.FILE_GROUPING);
        this.files = files;
    }

    public List<DtoGroupFileParameters> getFiles() {
        return files;
    }

    public void setFiles(List<DtoGroupFileParameters> files) {
        this.files = files;
    }
}
