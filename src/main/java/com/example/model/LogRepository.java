package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dominik on 22.04.2017.
 */
@Repository
public interface LogRepository extends JpaRepository<Log,Integer> {
    Log findById(int id);

    List<Log> findAllByModule(String module);

    List<Log> findAllBySeverity(String severity);

    List<Log> findAllByModuleAndSeverity(String module, String severity);
}
