package org.jproject.service.base;

import org.apache.commons.codec.digest.DigestUtils;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileType;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileHist;
import org.jproject.exception.AppException;
import org.jproject.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Optional;

public class BaseFileActionService implements IBaseFileActionService {

    private static final Logger logger = LoggerFactory.getLogger(BaseFileActionService.class);

    private final DaoWorker dao;
    private final Path path;
    private final String md5;
    private final Long bytes;
    private final String contentType;
    private final Instant creationTime;
    private final Instant lastModifiedTime;
    private final EFileType fileType;
    private final TFile tgfile;

    public BaseFileActionService(DaoWorker dao, TFile tfile) {
        logger.debug("File service: init");

        this.dao = dao;
        this.path = tfile.getPath();
        this.md5 = Optional.ofNullable(tfile.getFileHist()).map(TFileHist::getMd5).orElse(null);
        this.bytes = Optional.ofNullable(tfile.getFileHist()).map(TFileHist::getBytes).orElse(null);
        this.contentType = Optional.ofNullable(tfile.getFileHist()).map(TFileHist::getContentType).orElse(null);

        this.tgfile = tfile;
        this.creationTime = Optional.ofNullable(tfile.getFileHist()).map(TFileHist::getCreationTime).orElse(null);
        this.lastModifiedTime = Optional.ofNullable(tfile.getFileHist()).map(TFileHist::getLastModifiedTime).orElse(null);
        this.fileType = Optional.ofNullable(tfile.getFileHist()).map(TFileHist::getFileType).orElse(null);
    }

    public BaseFileActionService(DaoWorker dao, Path path) {
        logger.debug("File service: init");

        // TODO предусмотреть вариант сканирования директорий
        this.dao = dao;
        this.path = path;
        this.md5 = null; // TODO реализовать управление в параметрах //getMD5(path);
        this.bytes = getSize(path);
        this.contentType = getContentType(path); // TODO не всегда определяет тип контента

        this.tgfile = null;

        final BasicFileAttributes baseFileAttributes = getBaseFileAttributes(path);
        this.creationTime = getCreationTime(baseFileAttributes);
        this.lastModifiedTime = getLastModifiedTime(baseFileAttributes);
        this.fileType = getFileType(baseFileAttributes);
    }

    @Override
    public TFile apply() {
        logger.debug("File service: start: path " + this.path);
        final TFile result = updateFile(this.path);
        logger.debug("File service: complete");
        return result;
    }

    @Override
    public void action(TFile tfile) {
        throw new RuntimeException("You need to override the run action method");
    }

    private String getMD5(Path path) {
        try (InputStream is = Files.newInputStream(path)) {
            return DigestUtils.md5Hex(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Long getSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getContentType(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private BasicFileAttributes getBaseFileAttributes(Path path) {
        try {
            return Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Instant getCreationTime(BasicFileAttributes basicFileAttributes) {
        if (basicFileAttributes == null) {
            return null;
        }

        return Optional.ofNullable(basicFileAttributes.creationTime())
                .map(FileTime::toInstant)
                .orElse(null);
    }

    private Instant getLastModifiedTime(BasicFileAttributes basicFileAttributes) {
        if (basicFileAttributes == null) {
            return null;
        }

        return Optional.ofNullable(basicFileAttributes.lastModifiedTime())
                .map(FileTime::toInstant)
                .orElse(null);
    }

    private EFileType getFileType(BasicFileAttributes basicFileAttributes) {
        if (basicFileAttributes == null) {
            return EFileType.UNKNOWN;
        }

        final EFileType eFileType = basicFileAttributes.isDirectory()
                ? EFileType.DIRECTORY
                : EFileType.FILE;

        if (eFileType.equals(EFileType.FILE) && basicFileAttributes.isSymbolicLink()) {
            return EFileType.LINK;
        }

        return eFileType;
    }

    private TFile updateFile(Path path) {
        TFile tfile = null;
        if (this.tgfile != null) {
            tfile = this.tgfile;
        } else {
            tfile = this.dao.getFile(path).orElse(null);
        }

        if (tfile != null) {
            logger.debug("Action: file updated: fle_id = {}", tfile.getId());

            final TFileHist fileHist = tfile.getFileHist();

            if (fileHist != null) {
                this.dao.closeFileHist(fileHist);
            }

            tfile.setFileHist(createFileHist(tfile, path));
            return tfile;
        } else {
            // файл новый, не найден >> предварительные действия (preAction) с ним выполнять не нужно
            final TFile result = createFile(path);
            logger.debug("Action: file created: fle_id = {}", result.getId());
            return result;
        }
    }

    private TFile createFile(Path path) {
        final TFile tfile = new TFile();
        tfile.setPath(path);
        this.dao.persist(tfile);

        final TFileHist fileHist = createFileHist(tfile, path.toAbsolutePath());
        tfile.setFileHist(fileHist);
        return tfile;
    }

    private TFileHist createFileHist(TFile tfile, Path path) {
        final TFileHist fileHist = new TFileHist();

        try {
            fileHist.setFile(tfile);
            fileHist.setStartDate(TimeUtils.getCurrentTime());
            fileHist.setEndDate(TimeUtils.getMaxTime());
            fileHist.setFileType(this.fileType);
            fileHist.setMd5(this.md5);
            fileHist.setContentType(this.contentType);
            fileHist.setBytes(this.bytes);
            fileHist.setCreationTime(this.creationTime);
            fileHist.setLastModifiedTime(this.lastModifiedTime);
            action(tfile);
        } catch (AppException e) {
            e.printStackTrace();
            throw e;
        }

        return this.dao.persist(fileHist);
    }

    protected DaoWorker getDao() {
        return dao;
    }
}
