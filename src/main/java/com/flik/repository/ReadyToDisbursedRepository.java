package com.flik.repository;

import com.flik.entity.ReadyToDisbursedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadyToDisbursedRepository extends JpaRepository<ReadyToDisbursedEntity , Long> {
}
