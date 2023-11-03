package com.worldline.bench.loom.springboot3web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.worldline.bench.loom.springboot3web.entity.UUID;

@Repository
public interface UUIDRepository extends JpaRepository<UUID, Long> {
    
    @Query(value = "SELECT floor(random()*10000+1) as id, gen_random_uuid() as uuid, pg_sleep(:sleep)", nativeQuery = true)
    UUID generateRandomUUID(double sleep);
}
