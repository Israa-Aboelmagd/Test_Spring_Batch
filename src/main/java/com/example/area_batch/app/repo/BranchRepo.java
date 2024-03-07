package com.example.area_batch.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.area_batch.app.model.entity.target.TargetBranch;

public interface BranchRepo extends JpaRepository<TargetBranch,Long>{
    Optional<TargetBranch> findFirstByName(String name);

}
