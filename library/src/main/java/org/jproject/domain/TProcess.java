package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.Instant;

@Entity
@Table(name = "PROCESS")
public class TProcess extends AbstractHistEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prcd_seq")
    @SequenceGenerator(name = "prcd_seq", sequenceName = "prcd_seq", allocationSize = 1)
    @Column(name = "prcd_id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prcd_prcd_id")
    private TProcess process;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "pctp_pctp_id", nullable = false)
    private EProcessType processType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "pcst_pcst_id", nullable = false)
    private EProcessStatus processStatus;

    @Column(name = "object_count")
    private Integer objectCount;

    @Column(name = "attempts_remaining")
    private Integer attemptsRemaining;

    @Column(name = "param", nullable = false)
    private String param;

    @Column(name = "create_date")
    private Instant createDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "err_err_id")
    private TError error;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TProcess that = (TProcess) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getProcessStatus(), that.getProcessStatus())
                .append(getProcessType(), that.getProcessType())
                .append(getParam(), that.getParam())
                .append(getStartDate(), that.getStartDate())
                .append(getEndDate(), that.getEndDate())
                .append(getCreateDate(), that.getCreateDate())
                .append(getObjectCount(), that.getObjectCount())
                .isEquals()
                && getError().equals(that.getError());
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public TProcess getProcess() {
        return process;
    }

    public void setProcess(TProcess process) {
        this.process = process;
    }

    public EProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(EProcessType processType) {
        this.processType = processType;
    }

    public EProcessStatus getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(EProcessStatus processStatus) {
        this.processStatus = processStatus;
    }

    public Integer getObjectCount() {
        return objectCount;
    }

    public void setObjectCount(Integer objectCount) {
        this.objectCount = objectCount;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public TError getError() {
        return error;
    }

    public void setError(TError error) {
        this.error = error;
    }

    public Integer getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public void setAttemptsRemaining(Integer attemptsRemaining) {
        this.attemptsRemaining = attemptsRemaining;
    }
}
