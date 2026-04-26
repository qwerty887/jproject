package org.jproject.service.base;

import org.apache.commons.io.FilenameUtils;
import org.jproject.domain.EFileAttribute;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileHist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BaseResolveFileAttribute {

    private static final Logger logger = LoggerFactory.getLogger(BaseResolveFileAttribute.class);

    private final TFile tFile;

    private Map<EFileAttribute, Object> map = new HashMap<>();

    public BaseResolveFileAttribute(TFile tFile) {
        this.tFile = tFile;

        map.put(EFileAttribute.PATH, getPath());
        map.put(EFileAttribute.ABSOLUTE_PATH, getAbsolutePath());
        map.put(EFileAttribute.CREATION_TIME, getCreationTime());
        map.put(EFileAttribute.LAST_MODIFIED_TYPE, getLastModifiedTime());
        map.put(EFileAttribute.BYTES, getBytes());
        map.put(EFileAttribute.CONTENT_TYPE, getContentType());
        map.put(EFileAttribute.FILE_NAME, getFileName());
        map.put(EFileAttribute.FILE_EXTENSION, getFileExtension());
    }

    public <T> T get(EFileAttribute eFileAttribute, Class<T> clazz) {
        final T result = (clazz.cast(map.get(eFileAttribute)));
        logger.debug("Get attribute file: {} = {}", eFileAttribute, result);
        return result;
    }

    private Path getPath() {
        final Path result = Optional.ofNullable(this.tFile.getFileHist())
                .map(TFileHist::getPath)
                .orElse(null);
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.PATH, result);
        return result;
    }

    private String getAbsolutePath() {
        final String result = Optional.ofNullable(this.tFile.getFileHist())
                .map(TFileHist::getPath)
                .map(c -> c.toAbsolutePath().toString())
                .orElse(null);
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.ABSOLUTE_PATH, result);
        return result;
    }

    private Instant getCreationTime() {
        final Instant result = Optional.ofNullable(this.tFile.getFileHist())
                .map(TFileHist::getCreationTime)
                .orElse(null);
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.CREATION_TIME, result);
        return result;
    }

    private Instant getLastModifiedTime() {
        final Instant result = Optional.ofNullable(this.tFile.getFileHist())
                .map(TFileHist::getLastModifiedTime)
                .orElse(null);
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.LAST_MODIFIED_TYPE, result);
        return result;
    }

    private long getBytes() {
        final long result = this.tFile.getBytes();
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.BYTES, result);
        return result;
    }

    private String getContentType() {
        final String result = this.tFile.getContentType();
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.CONTENT_TYPE, result);
        return result;
    }

    private String getFileName() {
        final String result = FilenameUtils.getName(this.tFile.getFileHist().getPath().toString());
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.FILE_NAME, result);
        return result;
    }

    private String getFileExtension() {
        final String result = FilenameUtils.getExtension(this.tFile.getFileHist().getPath().toString());
        logger.debug("Resolve attribute file: {} = {}", EFileAttribute.FILE_EXTENSION, result);
        return result;
    }
}
