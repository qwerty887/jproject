package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.nio.file.Path;
import java.time.Instant;

@Entity
@Table(name = "FILE_HIST")
@SQLRestriction("now() between start_date and end_date and start_date < end_date") // TODO подумать как сделать более универсально
public class TFileHist extends AbstractHistEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fleh_seq")
    @SequenceGenerator(name = "fleh_seq", sequenceName = "fleh_seq", allocationSize = 1)
    @Column(name = "fleh_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fle_fle_id", nullable = false)
    private TFile file;

    @Column(name = "path", nullable = false)
    @Convert(converter = ConverterPathToString.class)
    private Path path;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "fles_fles_id", nullable = false)
    private EFileStatus fileStatus;

    @Column(name = "creation_time")
    private Instant creationTime;

    @Column(name = "last_modified_time")
    private Instant lastModifiedTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TFileHist that = (TFileHist) o;

        return new EqualsBuilder()
                    .append(getId(), that.getId())
                    .isEquals()
                    && that.getFile().equals(getFile());
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public TFile getFile() {
        return file;
    }

    public void setFile(TFile file) {
        this.file = file;
    }

    public Path getPath() {
        return path;
    }

    public TFileHist setPath(Path path) {
        this.path = path;
        return this;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public TFileHist setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
        return this;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public TFileHist setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public EFileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(EFileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

}
