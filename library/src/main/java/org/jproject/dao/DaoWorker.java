package org.jproject.dao;

import com.google.common.collect.Lists;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.jproject.domain.EProcessStatus;
import org.jproject.domain.EProcessType;
import org.jproject.domain.TFileGroupMember;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileGroupMember_;
import org.jproject.domain.TFileGroup_;
import org.jproject.domain.TFileHist;
import org.jproject.domain.TFileHist_;
import org.jproject.domain.TFile_;
import org.jproject.domain.TProcess;
import org.jproject.domain.TProcessLock;
import org.jproject.domain.TProcessLock_;
import org.jproject.domain.TProcess_;
import org.jproject.dto.parameters.DtoGroupFileParameters;
import org.jproject.dto.parameters.DtoLinkFileParameters;
import org.jproject.dto.parameters.DtoScanFileParameters;
import org.jproject.exception.NotSupportExceptionApp;
import org.jproject.utils.TimeUtils;
import org.springframework.data.jpa.domain.Specification;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoWorker extends DaoBase {

    private final Integer PARTITION_SIZE = 1000;

    public DaoWorker(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    // TODO адаптировать под findEntities
    public List<TFileGroup> getFileGroups(Specification<TFileGroup> spec) {
        List<TFileGroup> fileGroupList = null;

        {
            final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<TFileGroup> cq = cb.createQuery(TFileGroup.class);
            final Root<TFileGroup> root = cq.from(TFileGroup.class);
            root.fetch(TFileGroup_.fileGroupRules, JoinType.INNER);
            if (spec != null) {
                cq.where(spec.toPredicate(root, cq, cb));
            }
            fileGroupList = getEntityManager().createQuery(cq).getResultList();
        }

        {
            final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            final CriteriaQuery<TFileGroup> cq = cb.createQuery(TFileGroup.class);
            final Root<TFileGroup> root = cq.from(TFileGroup.class);
            root.fetch(TFileGroup_.fileGroupMembers, JoinType.LEFT)
                .fetch(TFileGroupMember_.file, JoinType.LEFT);

            cq.distinct(true);
            cq.where(root.in(fileGroupList));
            fileGroupList = getEntityManager().createQuery(cq).getResultList();
        }

        return fileGroupList;
    }

    public <T> List<TFile> getFiles(List<T> paramList, Class<T> clazz) {
        final List<List<T>> partitions = Lists.partition(paramList, PARTITION_SIZE);

        final List<TFile> result = new ArrayList<>();

        for (List<T> list: partitions) {

            if (clazz.equals(DtoGroupFileParameters.class)) {
                final List<DtoGroupFileParameters> list2 = list.stream().map(c -> (DtoGroupFileParameters) c).toList();
                final List<Path> pathList = list2.stream().map(DtoGroupFileParameters::getPath).toList();
                result.addAll(getFiles(getFileSpec(pathList)));
                continue;
            }

            if (clazz.equals(DtoScanFileParameters.class)) {
                final List<DtoScanFileParameters> list2 = list.stream().map(c -> (DtoScanFileParameters) c).toList();
                final List<Path> pathList = list2.stream().map(DtoScanFileParameters::getPath).toList();
                result.addAll(getFiles(getFileSpec(pathList)));
                continue;
            }

            if (clazz.equals(DtoLinkFileParameters.class)) {
                final List<DtoLinkFileParameters> list2 = list.stream().map(c -> (DtoLinkFileParameters) c).toList();
                final List<Path> pathList = list2.stream().map(DtoLinkFileParameters::getPath).toList();
                result.addAll(getFiles(getFileSpec(pathList)));
                continue;
            }

            if (clazz.equals(Path.class)) {
                final List<Path> list2 = list.stream().map(c -> (Path) c).toList();
                result.addAll(getFiles(getFileSpec(list2)));
                continue;
            }

            throw new NotSupportExceptionApp("Not support class");
        }

        return result;
    }

    public Optional<TFile> getFile(Path path) {
        return getFiles(getFileSpec(path)).stream().findAny();
    }

    private Specification<TFile> getFileSpec(Path path) {
        return (root, query, cb) -> cb.equal(root.get(TFile_.path), path);
    }

    private Specification<TFile> getFileSpec(List<Path> pathList) {
        return (root, query, cb) -> root.get(TFile_.path).in(pathList);
    }

    public List<TFile> getFiles(Specification<TFile> spec) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<TFile> cq = cb.createQuery(TFile.class);
        final Root<TFile> root = cq.from(TFile.class);
        root.fetch(TFile_.fileHist, JoinType.INNER);
        if (spec != null) {
            cq.where(spec.toPredicate(root, cq, cb));
        }

        return getEntityManager().createQuery(cq).getResultList();
    }

    public Integer getPrcdLock(EProcessType processType) {
        return (Integer) getEntityManager()
                .createNativeQuery("select getLockProcess(?)")
                .setParameter(1, processType.getId())
                .getSingleResult();
    }

    public void deleteProcessLock(TProcess process) {
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaDelete<TProcessLock> cd = cb.createCriteriaDelete(TProcessLock.class);
        final Root<TProcessLock> root = cd.from(TProcessLock.class);
        cd.where(cb.equal(root.get(TProcessLock_.process), process));
        getEntityManager().createQuery(cd).executeUpdate();
    }

    public void updateProcessStatus(TProcess process, EProcessStatus processStatus, Integer objectCount, String errMsg) {
        {
            final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            final CriteriaUpdate<TProcess> update = cb.createCriteriaUpdate(TProcess.class);
            final Root<TProcess> root = update.from(TProcess.class);

            switch (processStatus) {
                case LOCK -> {
                    update.set(root.get(TProcess_.processStatus), processStatus);
                    update.set(root.get(TProcess_.startDate), TimeUtils.getCurrentTime());
                    update.set(root.get(TProcess_.errMsg), cb.nullLiteral(String.class));
                }
                case ERROR -> {
                    update.set(root.get(TProcess_.processStatus), processStatus);
                    if (errMsg != null) {
                        update.set(root.get(TProcess_.errMsg), errMsg);
                    }
                    update.set(root.get(TProcess_.endDate), TimeUtils.getCurrentTime());
                }
                case COMPLETE -> {
                    update.set(root.get(TProcess_.objectCount), objectCount);
                    update.set(root.get(TProcess_.processStatus), processStatus);
                    update.set(root.get(TProcess_.endDate), TimeUtils.getCurrentTime());
                    update.set(root.get(TProcess_.errMsg), cb.nullLiteral(String.class));
                }
                default -> {
                    throw new NotSupportExceptionApp("Process status " + processStatus + " not support");
                }
            };

            update.where(cb.equal(root.get(TProcess_.id), process.getId()));
            getEntityManager().createQuery(update).executeUpdate();
        }
    }

    public int closeGroupMember(TFileGroupMember entity) {
        return closeEntity(
                (root, cq, cb) -> cb.equal(root.get(TFileGroupMember_.ID), entity.getId()),
                TFileGroupMember.class);
    }

    public int closeGroupMember(TFile file) {
        return closeEntity(
                (root, cq, cb) -> cb.equal(root.get(TFileGroupMember_.file), file),
                TFileGroupMember.class);
    }

    public int closeFileHist(TFileHist entity) {
        return closeEntity(
                (root, cq, cb) -> cb.equal(root.get(TFileHist_.ID), entity.getId()),
                TFileHist.class);
    }

    public Optional<TProcess> getProcess(Integer id) {
        return findEntity(
                (root, cq, cb) -> cb.equal(root.get(TProcess_.id), id),
                TProcess.class);
    }

    public Optional<TProcess> getProcess(Integer id, EProcessStatus processStatus) {
        return findEntity(
                (root, cq, cb) -> cb.and( cb.equal(root.get(TProcess_.id), id), cb.equal(root.get(TProcess_.processStatus), processStatus)),
                TProcess.class);
    }

    public List<TProcess> getProcesses(EProcessStatus processStatus) {
        return findEntities(
                (root, cq, cb) -> cb.equal(root.get(TProcess_.processStatus), processStatus),
                TProcess.class);
    }

    public List<TProcess> getProcesses(List<EProcessType> processTypeList, List<EProcessStatus> processStatusList) {
        return findEntities(
                (root, query, cb) -> cb.and(
                        root.get(TProcess_.processStatus).in(processStatusList),
                        root.get(TProcess_.processType).in(processTypeList)),
                TProcess.class);
    }

    public List<TFileGroupMember> getFileGroupMembers(List<Path> pathList) {
        return findEntities(
                (root, cq, cb) ->
                {
                    root.fetch(TFileGroupMember_.file, JoinType.INNER);
                    root.fetch(TFileGroupMember_.fileGroup, JoinType.INNER);
                    return root.get(TFileGroupMember_.file).get(TFile_.path).in(pathList);
                },
                TFileGroupMember.class);
    }
}
