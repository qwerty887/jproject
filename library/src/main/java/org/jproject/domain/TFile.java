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
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Immutable // entity was modified, but it won't be updated because the property is immutable.
@Table(name = "FILE")
@SQLRestriction("del_date is null")
public class TFile extends AbstractDeleteEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fle_seq")
    @SequenceGenerator(name = "fle_seq", sequenceName = "fle_seq", allocationSize = 1)
    @Column(name = "fle_id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "flet_flet_id", nullable = false)
    private EFileType fileType;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "md5", nullable = false)
    private String md5;

    @Column(name = "bytes")
    private Long bytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fle_id", referencedColumnName = "fle_fle_id", nullable = false, insertable=false, updatable=false)
    private TFileHist fileHist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TFile that = (TFile) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getFileType(), that.getFileType())
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

    public EFileType getFileType() {
        return fileType;
    }

    public void setFileType(EFileType fileType) {
        this.fileType = fileType;
    }

    public String getContentType() {
        return contentType;
    }

    public TFile setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public TFile setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public Long getBytes() {
        return bytes;
    }

    public TFile setBytes(Long bytes) {
        this.bytes = bytes;
        return this;
    }

    public TFileHist getFileHist() {
        return fileHist;
    }

    public TFile setFileHist(TFileHist fileHist) {
        this.fileHist = fileHist;
        return this;
    }
}
