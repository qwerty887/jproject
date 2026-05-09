package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.nio.file.Path;

@Entity
@Table(name = "LINK")
@SQLRestriction("del_date is null")
public class TLink extends AbstractDeleteEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lnk_seq")
    @SequenceGenerator(name = "lnk_seq", sequenceName = "lnk_seq", allocationSize = 1)
    @Column(name = "lnk_id", nullable = false)
    private Integer id;

    @Column(name = "path")
    @Convert(converter = ConverterPathToString.class)
    private Path path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TLink that = (TLink) o;

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
}
