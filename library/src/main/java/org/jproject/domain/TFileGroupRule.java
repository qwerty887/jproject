package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.transaction.NotSupportedException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.nio.file.Path;
import java.time.Instant;

@Entity
@Table(name = "FILE_GROUP_RULE")
@SQLRestriction("del_date is null")
public class TFileGroupRule extends AbstractDeleteEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flgr_seq")
    @SequenceGenerator(name = "flgr_seq", sequenceName = "flgr_seq", allocationSize = 1)
    @Column(name = "flgr_id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "flat_flat_id", nullable = false)
    private EFileAttribute fileAttribute;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "flgc_flgc_id", nullable = false)
    private EFileCondition fileCondition;

    // TODO поменять в тип Object для последующе конверции в другой тип??
    @Column(name = "value", nullable = false)
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TFileGroupRule that = (TFileGroupRule) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getFileAttribute(), that.getFileAttribute())
                .append(getFileCondition(), that.getFileCondition())
                .append(getValue(), that.getValue())
                .append(getDelDate(), that.getDelDate())
                .isEquals();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public EFileAttribute getFileAttribute() {
        return fileAttribute;
    }

    public TFileGroupRule setFileAttribute(EFileAttribute fileAttribute) {
        this.fileAttribute = fileAttribute;
        return this;
    }

    public EFileCondition getFileCondition() {
        return fileCondition;
    }

    public TFileGroupRule setFileCondition(EFileCondition fileCondition) {
        this.fileCondition = fileCondition;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public <T> T getValue(Class<T> clazz) throws NotSupportedException {

        if (clazz.equals(Path.class)) {
            return (T) Path.of(String.valueOf(value));
        }

        if (clazz.equals(Instant.class)) {
            return (T) Instant.parse((String) value);
        }

        if (clazz.equals(String.class)) {
            return (T) (String) value;
        }

        throw new NotSupportedException("Class " + clazz.getName() + " not support");
    }

    public TFileGroupRule setValue(Object value) {
        this.value = value;
        return this;
    }
}
