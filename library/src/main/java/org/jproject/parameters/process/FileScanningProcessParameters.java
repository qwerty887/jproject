package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.dto.parameters.DtoScanFileParameters;
import org.jproject.parameters.process.base.BaseProcessParam;

import java.util.List;

public class FileScanningProcessParameters extends BaseProcessParam {

    List<DtoScanFileParameters> files;

    public FileScanningProcessParameters() {
        super(EProcessType.FILE_SCANNING);
    }

    public FileScanningProcessParameters(List<DtoScanFileParameters> files) {
        super(EProcessType.FILE_SCANNING);
        this.files = files;
    }

    public List<DtoScanFileParameters> getFiles() {
        return files;
    }

    public void setFiles(List<DtoScanFileParameters> files) {
        this.files = files;
    }
}
