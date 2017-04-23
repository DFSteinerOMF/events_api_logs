package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Dominik on 22.04.2017.
 */
@Repository
public interface LogRepository extends JpaRepository<Log,Integer    > {
}
