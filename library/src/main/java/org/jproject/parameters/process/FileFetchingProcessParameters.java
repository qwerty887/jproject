package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.dto.parameters.DtoFetchFileParameters;
import org.jproject.parameters.process.base.BaseProcessParam;

import java.util.List;

public class FileFetchingProcessParameters extends BaseProcessParam {

    List<DtoFetchFileParameters> files;

    public FileFetchingProcessParameters() {
        super(EProcessType.FILE_FETCHING);
    }

    public FileFetchingProcessParameters(List<DtoFetchFileParameters> files) {
        super(EProcessType.FILE_FETCHING);
        this.files = files;
    }

    public List<DtoFetchFileParameters> getFiles() {
        return files;
    }

    public void setFiles(List<DtoFetchFileParameters> files) {
        this.files = files;
    }
}
