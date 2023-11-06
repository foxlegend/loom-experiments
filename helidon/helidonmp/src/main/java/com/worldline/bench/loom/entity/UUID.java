package com.worldline.bench.loom.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Access(AccessType.FIELD)
public class UUID {

    @Id
    public int id;
    
    public String uuid;

    public UUID() {
    }

    @Override
    public String toString() {
        return "UUID [id=" + id + ", uuid=" + uuid + "]";
    }
}
