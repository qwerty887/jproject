package org.jproject.parameters.process;

import org.jproject.domain.EProcessType;
import org.jproject.parameters.process.base.BaseProcessParam;

import java.nio.file.Path;

public class FileFetchingProcessParameters extends BaseProcessParam {

    Path path;
    Path excludePath;

    public FileFetchingProcessParameters() {
        super(EProcessType.FILE_FETCHING);
    }

    public FileFetchingProcessParameters(Path path, Path excludePath) {
        super(EProcessType.FILE_FETCHING);
        this.path = path;
        this.excludePath = excludePath;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(Path excludePath) {
        this.excludePath = excludePath;
    }
}
