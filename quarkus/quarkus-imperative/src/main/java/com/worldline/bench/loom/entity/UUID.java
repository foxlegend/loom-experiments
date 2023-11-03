package com.worldline.bench.loom.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class UUID extends PanacheEntity {

    public String uuid;

    @Override
    public String toString() {
        return "UUID [id=" + id + ", uuid=" + uuid + "]";
    }
}
