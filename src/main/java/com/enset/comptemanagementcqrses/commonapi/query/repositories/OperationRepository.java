package com.enset.comptemanagementcqrses.commonapi.query.repositories;

import com.enset.comptemanagementcqrses.commonapi.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
}
