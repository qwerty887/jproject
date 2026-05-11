package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
import org.jproject.domain.base.IBaseEntity;

import java.nio.file.Path;

@Entity
@Table(name = "FILE")
@Immutable
public class TFile implements IBaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fle_seq")
    @SequenceGenerator(name = "fle_seq", sequenceName = "fle_seq", allocationSize = 1)
    @Column(name = "fle_id", nullable = false)
    private Integer id;

    @Column(name = "path")
    @Convert(converter = ConverterPathToString.class)
    private Path path;

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

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public TFileHist getFileHist() {
        return fileHist;
    }

    public void setFileHist(TFileHist fileHist) {
        this.fileHist = fileHist;
    }
}
