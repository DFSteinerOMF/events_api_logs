package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Dominik on 28.05.2017.
 */
public interface AuditRepository extends JpaRepository<Audit, Integer> {
    public Audit findById(int id);

    public List<Audit> findAllByModule(String module);

    public List<Audit> findAllByUserAction(String userAction);

    public List<Audit> findAllByModuleAndUserAction(String module, String userAction);
}
