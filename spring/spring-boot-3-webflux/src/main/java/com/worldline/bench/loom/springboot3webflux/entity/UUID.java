package com.worldline.bench.loom.springboot3webflux.entity;

import org.springframework.data.annotation.Id;

public class UUID {

    @Id
    private Long id;

    private String uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
