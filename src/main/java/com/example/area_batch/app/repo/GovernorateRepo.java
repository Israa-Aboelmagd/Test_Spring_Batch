package com.example.area_batch.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.area_batch.app.model.entity.target.TargetGovernorate;

public interface GovernorateRepo extends JpaRepository<TargetGovernorate,Long> {
    Optional<TargetGovernorate> findFirstByName(String name);

}
