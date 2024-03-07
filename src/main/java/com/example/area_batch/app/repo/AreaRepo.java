package com.example.area_batch.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.area_batch.app.model.entity.target.TargetArea;

public interface AreaRepo extends JpaRepository<TargetArea,Long>{

    
} 
