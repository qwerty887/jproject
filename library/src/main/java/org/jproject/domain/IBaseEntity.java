package org.jproject.domain;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

public interface IBaseEntity<K> extends Serializable {

    K getId();

    void setId(K id);

    @Nullable
    static <K, E extends IBaseEntity<K>> E mapId(@Nullable K id, @Nonnull Supplier<E> entityProvider) {
        if (id == null) {
            return null;
        }

        E entity = Objects.requireNonNull(entityProvider.get());
        entity.setId(id);
        return entity;
    }
}
