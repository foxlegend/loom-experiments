package com.worldline.bench.loom.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class UUID extends PanacheEntity {

    public String uuid;

    @Override
    public String toString() {
        return "UUID [uuid=" + uuid + "]";
    }
}
