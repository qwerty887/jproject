package org.jproject.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FILE_GROUP")
@SQLRestriction("del_date is null")
public class TFileGroup extends AbstractDeleteEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fleg_seq")
    @SequenceGenerator(name = "fleg_seq", sequenceName = "fleg_seq", allocationSize = 1)
    @Column(name = "fleg_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "link_path", nullable = false)
    private String linkPath;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name="fleg_flgr",
            joinColumns= @JoinColumn(name="fleg_fleg_id"),
            inverseJoinColumns=@JoinColumn(name="flgr_flgr_id")
            )
    private List<TFileGroupRule> fileGroupRules = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TFileGroup that = (TFileGroup) o;

        return new EqualsBuilder()
                .append(getId(), that.getId())
                .append(getName(), that.getName())
                .append(getLinkPath(), that.getLinkPath())
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkPath() {
        return linkPath;
    }

    public void setLinkPath(String linkPath) {
        this.linkPath = linkPath;
    }

    public List<TFileGroupRule> getFileGroupRules() {
        return fileGroupRules;
    }

    public TFileGroup setFileGroupRules(List<TFileGroupRule> fileGroupRules) {
        this.fileGroupRules = fileGroupRules;
        return this;
    }
}
