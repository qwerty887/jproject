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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.SQLRestriction;
import org.jproject.domain.base.AbstractHistEntity;

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
    @JoinColumn(name = "fle_fle_id")
    private TFile file;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "flet_flet_id")
    private EFileType fileType;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "md5")
    private String md5;

    @Column(name = "bytes")
    private Long bytes;

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

    public TFile getFile() {
        return file;
    }

    public void setFile(TFile file) {
        this.file = file;
    }

    public EFileType getFileType() {
        return fileType;
    }

    public void setFileType(EFileType fileType) {
        this.fileType = fileType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    public Instant getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Instant lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
