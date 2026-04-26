package org.jproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.Instant;

@Entity
@Table(name = "Error")
public class TError {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "err_seq")
    @SequenceGenerator(name = "err_seq", sequenceName = "err_seq", allocationSize = 1)
    @Column(name = "err_id", nullable = false)
    private Integer id;

    @Column(name = "error_date", nullable = false)
    private Instant date;

    @Column(name = "message", nullable = false)
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TError that = (TError) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(date, that.date)
                .append(message, that.message)
                .isEquals();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
