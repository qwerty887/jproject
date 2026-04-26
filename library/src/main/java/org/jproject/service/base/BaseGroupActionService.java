package org.jproject.service.base;

import jakarta.transaction.NotSupportedException;
import org.jproject.dao.DaoWorker;
import org.jproject.domain.EFileAttribute;
import org.jproject.domain.EFileCondition;
import org.jproject.domain.FlegFleh;
import org.jproject.domain.TFile;
import org.jproject.domain.TFileGroup;
import org.jproject.domain.TFileGroupRule;
import org.jproject.domain.TFileHist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BaseGroupActionService implements IBaseGroupActionService {

    private static final Logger logger = LoggerFactory.getLogger(BaseGroupActionService.class);

    private final DaoWorker dao;
    private final BaseResolveFileAttribute fileAttribute;
    private final List<TFileGroup> tFileGroups;
    private final TFileHist tFileHist;
    private final Optional<TFileGroup> tFileGroupDefault;

    // TODO добавить результаты валидации правил
    // TODO предусмотреть специальные группы: 30 последних файлов, 10 самых больших файлов и т.д.
    // TODO предусмотреть макроподстановку для времени как ${{ now() }}, ${{ now() - 1d }}

    public BaseGroupActionService(DaoWorker dao, TFile tFile, List<TFileGroup> tFileGroups, Optional<TFileGroup> tFileGroupDefault) {
        logger.debug("Group service: init");
        this.dao = dao;
        this.fileAttribute = new BaseResolveFileAttribute(tFile);
        this.tFileGroups = tFileGroups;
        this.tFileHist = tFile.getFileHist();
        this.tFileGroupDefault = tFileGroupDefault;
    }

    @Override
    public List<TFileGroup> apply() throws NotSupportedException {
        logger.debug("Group service: start");

        final List<TFileGroup> results = getMatches();

        for (TFileGroup entity: results) {
            final FlegFleh.PK pk = new FlegFleh.PK();
            pk.setFlegFlegId(entity.getId());
            pk.setFlehFlehId(this.tFileHist.getId());

            final FlegFleh flegFleh = new FlegFleh();
            flegFleh.setId(pk);

            logger.info("flegFlegId={} flehFlehId={}", entity.getId(), this.tFileHist.getId());
            /* TODO возникают дубли: когда дубли в одной транзиации, падаем
            flegFlegId=6 flehFlehId=1029
            flegFlegId=6 flehFlehId=1031
            flegFlegId=6 flehFlehId=1031
            flegFlegId=6 flehFlehId=1031
             */

            dao.persist(flegFleh); // TODO возникает исключение
        }

        logger.debug("Group service: complete");

        return results;
    }

    private List<TFileGroup> getMatches() throws NotSupportedException {
        final List<TFileGroup> results = new ArrayList<>();

        for (TFileGroup tFileGroup : this.tFileGroups) {
            if (isMatches(tFileGroup)) {
                results.add(tFileGroup);
            }
        }

        // если не нашли группу для файла, то назначаем ему группу по умолчанию
        if (results.isEmpty()) {
            this.tFileGroupDefault.map(results::add);
        }

        return results;
    }

    private boolean isMatches(TFileGroup tFileGroup) throws NotSupportedException {
        logger.debug("Resolve: fleg_id = {}", tFileGroup.getId());

        final List<TFileGroupRule> fileGroupRules = Optional.ofNullable(tFileGroup)
                .map(TFileGroup::getFileGroupRules)
                .orElse(new ArrayList<>());

        boolean result = isMatches(fileGroupRules);
        logger.debug("Result: fleg_id = {}, result = {}", tFileGroup.getId(), result);
        return result;
    }

    private boolean isMatches(List<TFileGroupRule> tFileGroupRules) throws NotSupportedException {
        long trueCnt = 0;
        for (TFileGroupRule tFileGroupRule : tFileGroupRules) {
            if (isMatches(tFileGroupRule)) {
                trueCnt++;
            }
        }

        return tFileGroupRules.stream().count() == trueCnt;
    }

    private boolean isMatches(TFileGroupRule tFileGroupRule) throws NotSupportedException {
        logger.debug("Resolve group rule: flgr_id = {}", tFileGroupRule.getId());

        final EFileAttribute eFileAttribute = tFileGroupRule.getFileAttribute();
        final EFileCondition eFileCondition = tFileGroupRule.getFileCondition();

        logger.debug("Resolve type attribute file: {}", eFileAttribute);
        logger.debug("Resolve type condition file: {}", eFileCondition);
        logger.debug("Resolve group value: {}", tFileGroupRule.getValue());

        boolean result = switch (eFileAttribute) {
            // TODO преобразовать это в STRING для работы как со строкой/с регулярными выражениями
            case PATH -> isMatcher(
                            fileAttribute.get(eFileAttribute, Path.class),
                            eFileCondition,
                            tFileGroupRule.getValue(Path.class)
                            );
            case CREATION_TIME, LAST_MODIFIED_TYPE -> isMatcher(
                            fileAttribute.get(eFileAttribute, Instant.class),
                            eFileCondition,
                            tFileGroupRule.getValue(Instant.class) // "2024-11-20T15:30:00Z";
                            );
            case BYTES -> isMatcher(
                            fileAttribute.get(eFileAttribute, Integer.class),
                            eFileCondition,
                            tFileGroupRule.getValue(Integer.class)
                            );
            case CONTENT_TYPE, FILE_NAME, FILE_EXTENSION, ABSOLUTE_PATH -> isMatcher(
                            fileAttribute.get(eFileAttribute, String.class),
                            eFileCondition,
                            tFileGroupRule.getValue(String.class)
                            );
            default -> throw new NotSupportedException("File attribute " + eFileCondition + " not support");
            };

        logger.debug("Result: flgr_id = {}, result = {}", tFileGroupRule.getId(), result);
        return result;
    }

    private <T extends String> boolean isMatcher(T attrValue, EFileCondition eFileCondition, T targetValue) throws NotSupportedException {
        return switch (eFileCondition) {
            case REGEXP -> getRegExpResult(targetValue, attrValue);
            case EQUAL -> attrValue.compareTo(targetValue) == 0;
            case NOT_EQUAL -> attrValue.compareTo(targetValue) != 0;
            default -> throw new NotSupportedException("File condition " + eFileCondition + " not support");
        };
    }

    private <T extends Integer> boolean isMatcher(T attrValue, EFileCondition eFileCondition, T targetValue) throws NotSupportedException {
        return switch (eFileCondition) {
            case EQUAL -> attrValue.compareTo(targetValue) == 0;
            case NOT_EQUAL -> attrValue.compareTo(targetValue) != 0;
            case LESS -> attrValue.compareTo(targetValue) < 0;
            case GREATER -> attrValue.compareTo(targetValue) > 0;
            default -> throw new NotSupportedException("File condition " + eFileCondition + " not support");
        };
    }

    private <T extends Instant> boolean isMatcher(T attrValue, EFileCondition eFileCondition, T targetValue) throws NotSupportedException {
        return switch (eFileCondition) {
            case EQUAL -> attrValue.compareTo(targetValue) == 0;
            case NOT_EQUAL -> attrValue.compareTo(targetValue) != 0;
            case LESS -> attrValue.compareTo(targetValue) < 0;
            case GREATER -> attrValue.compareTo(targetValue) > 0;
            default -> throw new NotSupportedException("File condition " + eFileCondition + " not support");
        };
    }

    private <T extends Path> boolean isMatcher(T attrValue, EFileCondition eFileCondition, T targetValue) throws NotSupportedException {
        return switch (eFileCondition) {
            case EQUAL -> attrValue.compareTo(targetValue) == 0;
            case NOT_EQUAL -> attrValue.compareTo(targetValue) != 0;
            default -> throw new NotSupportedException("File condition " + eFileCondition + " not support");
        };
    }

    private boolean getRegExpResult(String regexp, String value) {
        final Pattern pattern = Pattern.compile(regexp);
        final Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
