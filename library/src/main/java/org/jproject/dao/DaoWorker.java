package org.jproject.dao;

import com.google.common.collect.Lists;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.jproject.domain.EFileStatus;
import org.jproject.domain.EFileType;
import org.jproject.domain.EProcessStatus;
import org.jproject.domain.EProcessType;
import org.jproject.domain.FlegFleh;
import org.jproject.domain.FlegFleh_;
import org.jproject.domain.TError;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileGroup_;
import org.jproject.domain.TFileHist;
import org.jproject.domain.TFileHist_;
import org.jproject.domain.TFile_;
import org.jproject.domain.TLink;
import org.jproject.domain.TLinkHist;
import org.jproject.domain.TLinkHist_;
import org.jproject.domain.TLink_;
import org.jproject.domain.TProcess;
import org.jproject.domain.TProcessLock;
import org.jproject.domain.TProcessLock_;
import org.jproject.domain.TProcess_;
import org.jproject.dto.DtoGroupFileParameters;
import org.jproject.exception.NotSupportExceptionApp;
import org.jproject.utils.TimeUtils;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoWorker extends DaoBase {

    private final Integer DEFAULT_FILE_GROUP = 0;

    public DaoWorker(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public boolean isFileExist(File file) {
        return isFileExist(file.getName());
    }

    public boolean isFileExist(String md5) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        final Root<TFile> root = cq.from(TFile.class);
        cq.select(cb.count(root));
        cq.where(cb.equal(root.get(TFile_.MD5), md5));

        final Long result = getEntityManager().createQuery(cq).getSingleResult();
        if (result == 0L) {
            return false;
        } else if (result == 1L) {
            return true;
        }

        throw new RuntimeException("More that one result");
    }

    public List<TFileGroup> getFileGroups() {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TFileGroup> cq = cb.createQuery(TFileGroup.class);
        final Root<TFileGroup> root = cq.from(TFileGroup.class);
        root.fetch(TFileGroup_.FILE_GROUP_RULES,  JoinType.INNER);
        cq.where(cb.greaterThan(root.get(TFileGroup_.ID), DEFAULT_FILE_GROUP));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public Optional<TFileGroup> getFileGroupDefault() {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TFileGroup> cq = cb.createQuery(TFileGroup.class);
        final Root<TFileGroup> root = cq.from(TFileGroup.class);
        cq.where(cb.equal(root.get(TFileGroup_.ID), DEFAULT_FILE_GROUP));
        return getSingleResult(getEntityManager().createQuery(cq));
    }

    // TODO доделать
    /*
    public List<TFileGroup> getFileGroups(TFile tfile) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TFileGroup> cq = cb.createQuery(TFileGroup.class);
        final Root<TFileGroup> root = cq.from(TFileGroup.class);

        final Join<Object, Object> joinHist = (Join<Object, Object>) root.fetch(TFileGroup_.FILE_GROUP_HIST, JoinType.LEFT);
        joinHist.on(cb.equal(joinHist.get(TFileGroupHist_.FILE_HIST), tfile.getFileHist()));

        // root.fetch(TFileGroup_.FILE_GROUP_RULE_LIST, JoinType.INNER);
        return getEntityManager().createQuery(cq).getResultList();
    }
    */

    public Optional<TFile> getFile(Path path, String md5) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TFile> cq = cb.createQuery(TFile.class);
        final Root<TFile> root = cq.from(TFile.class);

        final Join<Object, Object> joinHist = (Join<Object, Object>) root.fetch(TFile_.FILE_HIST, JoinType.LEFT);
        joinHist.on(cb.equal(joinHist.get(TFileHist_.PATH), path));

        cq.where(cb.equal(root.get(TFile_.MD5), md5));
        return getSingleResult(getEntityManager().createQuery(cq));
    }

    public List<TFile> getFiles(List<DtoGroupFileParameters> listParam) {
        final List<List<DtoGroupFileParameters>> partitions = Lists.partition(listParam, 1000); // TODO размер вынести в параметры

        final List<TFile> result = new ArrayList<>();
        for (List<DtoGroupFileParameters> list: partitions) {
            final List<Path> pathList = list.stream().map(DtoGroupFileParameters::getPath).toList();
            final List<String> md5List = list.stream().map(DtoGroupFileParameters::getMd5).toList();
            // result.addAll(getFiles(pathList, md5List)); // TODO
            result.addAll(getFiles2(pathList, md5List));
        }

        return result;
    }

    public List<TFile> getFiles(List<Path> pathList, List<String> md5List) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TFile> cq = cb.createQuery(TFile.class);
        final Root<TFile> root = cq.from(TFile.class);

        final Join<Object, Object> joinHist = (Join<Object, Object>) root.fetch(TFile_.FILE_HIST, JoinType.LEFT);
        joinHist.on(joinHist.get(TFileHist_.PATH).in(pathList));

        cq.where(root.get(TFile_.MD5).in(md5List));
        return getEntityManager().createQuery(cq).getResultList();
        // TODO ручной маппинг сделать
    }

    public List<TFile> getFiles() {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TFile> cq = cb.createQuery(TFile.class);
        final Root<TFile> root = cq.from(TFile.class);
        root.fetch(TFile_.FILE_HIST, JoinType.LEFT);
        return getEntityManager().createQuery(cq).getResultList();
    }

    public Optional<TProcess> getProcess(EProcessType processType) {
        return getProcess(EProcessStatus.WAIT, processType);
    }

    public Optional<TProcess> getProcess(EProcessStatus processStatus, EProcessType processType) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TProcess> cq = cb.createQuery(TProcess.class);
        final Root<TProcess> root = cq.from(TProcess.class);

        cq.where(cb.and(
                        cb.equal(root.get(TProcess_.PROCESS_STATUS), processStatus),
                        cb.equal(root.get(TProcess_.PROCESS_TYPE), processType)
                        )
                );

        return getSingleResult(getEntityManager().createQuery(cq).setMaxResults(1));
    }

    public Integer getPrcdLock(EProcessType processType) {
        Query query = getEntityManager().createNativeQuery("select getLockProcess(?)");
        query.setParameter(1, processType.getId());
        return (Integer) query.getSingleResult();
    }

    public Optional<TProcessLock> getProcessLock(TProcess process) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaUpdate<TProcess> update = cb.createCriteriaUpdate(TProcess.class);
        final Root<TProcess> root = update.from(TProcess.class);

        update.set(root.get(TProcess_.processStatus), EProcessStatus.LOCK);
        update.set(root.get(TProcess_.startDate), TimeUtils.getCurrentTime());
        update.where(cb.equal(root.get(TProcess_.id), process.getId()));

        if (getEntityManager().createQuery(update).executeUpdate() > 0) {
            final TProcessLock processLock = new TProcessLock();
            processLock.setProcess(process);
            return Optional.ofNullable(processLock);
        }

        return Optional.empty();
    }

    public void deleteProcessLock(TProcess process) {
        if (process == null) {
            return;
        }

        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaDelete<TProcessLock> cd = cb.createCriteriaDelete(TProcessLock.class);
        final Root<TProcessLock> root = cd.from(TProcessLock.class);
        cd.where(cb.equal(root.get(TProcessLock_.process).get(TProcess_.id), process.getId()));
        getEntityManager().createQuery(cd).executeUpdate();
    }

    public void updateProcessStatus(TProcess process, EProcessStatus processStatus, Integer objectCount, TError error) {
        if (process == null) {
            return;
        }

        {
            final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            final CriteriaUpdate<TProcess> update = cb.createCriteriaUpdate(TProcess.class);
            final Root<TProcess> root = update.from(TProcess.class);

            switch (processStatus) {
                case LOCK -> {
                    update.set(root.get(TProcess_.processStatus), processStatus);
                    update.set(root.get(TProcess_.startDate), TimeUtils.getCurrentTime());
                }
                case PROCESS -> {
                    update.set(root.get(TProcess_.processStatus), processStatus);
                    update.set(root.get(TProcess_.startDate), TimeUtils.getCurrentTime());
                    update.set(root.get(TProcess_.error), cb.nullLiteral(TError.class));
                }
                case ERROR -> {
                    update.set(root.get(TProcess_.processStatus), processStatus);
                    if (error != null) {
                        update.set(root.get(TProcess_.error), error);
                    }
                    update.set(root.get(TProcess_.endDate), TimeUtils.getCurrentTime());
                }
                case COMPLETE -> {
                    update.set(root.get(TProcess_.objectCount), objectCount);
                    update.set(root.get(TProcess_.processStatus), processStatus);
                    update.set(root.get(TProcess_.endDate), TimeUtils.getCurrentTime());
                }
                default -> {
                    throw new NotSupportExceptionApp("Process status " + processStatus + " not support");
                }
            };

            update.where(cb.equal(root.get(TProcess_.id), process.getId()));
            getEntityManager().createQuery(update).executeUpdate();
        }
    }

    // TODO возможно придется переделать так же как то реализовано для файлов
    public Optional<TLink> getLink(TFile file) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TLink> cq = cb.createQuery(TLink.class);
        final Root<TLink> root = cq.from(TLink.class);
        root.fetch(TLink_.LINK_HIST, JoinType.INNER)
            .fetch(TLinkHist_.ERROR, JoinType.LEFT);
        cq.where(cb.equal(root.get(TLink_.FILE), file));
        return getSingleResult(getEntityManager().createQuery(cq));
    }

    public void deleteFlegFleh(TFile tFile) {
        final TFileHist tFileHist = Optional.ofNullable(tFile)
                .map(TFile::getFileHist)
                .orElse(null);

        if (tFileHist == null) {
            return;
        }

        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaDelete<FlegFleh> cd = cb.createCriteriaDelete(FlegFleh.class);
        final Root<FlegFleh> root = cd.from(FlegFleh.class);

        cd.where(cb.equal(root.get(FlegFleh_.fileHist), tFileHist));
        getEntityManager().createQuery(cd).executeUpdate();
    }

    public Optional<TFileGroup> getFileGroup(TFileGroup entity) {
        return findEntity(
                    (root, cq, cb) -> cb.equal(root.get(TFileGroup_.id), entity),
                    TFileGroup.class);
    }

    /*
    public Optional<TFileGroupHist> getFileGroupMember(TFileGroupHist entity) {
        return findEntity(
                    (root, cq, cb) -> cb.equal(root.get(TFileGroupHist_.id), entity),
                    TFileGroupHist.class);
    }
    */

    /*
    public void deleteFileGroup(TFileGroup entity) {
        deleteEntity(entity,
                    (root, cq, cb) -> cb.equal(root.get(TFileGroup_.ID), entity.getId()),
                    TFileGroup.class);

        // TODO подумать как улучшить
        {
            final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            final CriteriaUpdate<TFileGroupHist> update = cb.createCriteriaUpdate(TFileGroupHist.class);
            final Root<TFileGroupHist> root = update.from(TFileGroupHist.class);
            update.set(root.get(DeleteEntity_.delDate), TimeUtils.getCurrentTime());
            update.where(cb.equal(root.get(TFileGroupMember_.fileGroup).get(TFileGroup_.id), entity.getId()));
            getEntityManager().createQuery(update).executeUpdate();
        }
    }
    */

    /*
    public void deleteFileGroupMember(TFileGroupHist entity) {

        deleteEntity(entity,
                    (root, cq, cb) -> cb.equal(root.get(TFileGroupMember_.ID), entity.getId()),
                    TFileGroupHist.class);
    }
    */

    public void closeFileHist(TFileHist entity) {
        closeEntity(entity,
                   (root, cq, cb) -> cb.equal(root.get(TFileHist_.ID), entity.getId()),
                    TFileHist.class);
    }

    public void closeLinkHist(TLinkHist entity) {
        closeEntity(entity,
                    (root, cq, cb) -> cb.equal(root.get(TLinkHist_.ID), entity.getId()),
                    TLinkHist.class);
    }


    public Optional<TProcess> getProcess(Integer prcdId, EProcessStatus processStatus) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TProcess> cq = cb.createQuery(TProcess.class);
        final Root<TProcess> root = cq.from(TProcess.class);

        cq.where(
                cb.and(
                        cb.equal(root.get(TProcess_.id), prcdId),
                        cb.equal(root.get(TProcess_.processStatus), processStatus)
                    )
                );

        return getSingleResult(getEntityManager().createQuery(cq));
    }

    public List<TFile> getFiles2(List<Path> pathList, List<String> md5List) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
        final Root<TFile> root = cq.from(TFile.class);

        final Join<TFile, TFileHist> joinHist = root.join(TFile_.FILE_HIST, JoinType.LEFT);
        joinHist.on(joinHist.get(TFileHist_.PATH).in(pathList));

        cq.where(root.get(TFile_.MD5).in(md5List));

        cq.multiselect(
                root.get(TFile_.ID).alias(TFile_.ID),
                root.get(TFile_.FILE_TYPE).alias(TFile_.FILE_TYPE),
                root.get(TFile_.CONTENT_TYPE).alias(TFile_.CONTENT_TYPE),
                root.get(TFile_.MD5).alias(TFile_.MD5),
                root.get(TFile_.BYTES).alias(TFile_.BYTES),

                joinHist.get(TFileHist_.ID).alias(TFileHist_.ID + "_HIST"),
                joinHist.get(TFileHist_.PATH).alias(TFileHist_.PATH),
                joinHist.get(TFileHist_.FILE_STATUS).alias(TFileHist_.FILE_STATUS),
                joinHist.get(TFileHist_.CREATION_TIME).alias(TFileHist_.CREATION_TIME),
                joinHist.get(TFileHist_.LAST_MODIFIED_TIME).alias(TFileHist_.LAST_MODIFIED_TIME),
                joinHist.get(TFileHist_.START_DATE).alias(TFileHist_.START_DATE),
                joinHist.get(TFileHist_.END_DATE).alias(TFileHist_.END_DATE)
        );

        return getEntityManager()
                .createQuery(cq)
                .getResultStream()
                .map(entity -> {
                    final TFile tfile = new TFile();

                    tfile.setId(entity.get(TFile_.ID, Integer.class));
                    tfile.setFileType(entity.get(TFile_.FILE_TYPE, EFileType.class));
                    tfile.setContentType(entity.get(TFile_.CONTENT_TYPE, String.class));
                    tfile.setMd5(entity.get(TFile_.MD5, String.class));
                    tfile.setBytes(entity.get(TFile_.BYTES, Long.class));

                    final TFileHist tFileHist = new TFileHist();
                    tFileHist.setFile(tfile);
                    tFileHist.setId(entity.get(TFileHist_.ID + "_HIST", Integer.class));
                    tFileHist.setPath(entity.get(TFileHist_.PATH, Path.class));
                    tFileHist.setFileStatus(entity.get(TFileHist_.FILE_STATUS, EFileStatus.class));
                    tFileHist.setCreationTime(entity.get(TFileHist_.CREATION_TIME, Instant.class));
                    tFileHist.setLastModifiedTime(entity.get(TFileHist_.LAST_MODIFIED_TIME, Instant.class));
                    tFileHist.setStartDate(entity.get(TFileHist_.START_DATE, Instant.class));
                    tFileHist.setEndDate(entity.get(TFileHist_.END_DATE, Instant.class));
                    tfile.setFileHist(tFileHist);

                    return tfile;
                })
                .toList();
    }
}
