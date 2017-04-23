package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Dominik on 22.04.2017.
 */
@Repository
public interface LogRepository extends JpaRepository<Log,Integer    > {
    public Log findById(int id);

    public List<Log> findAllByDescriptionLike(String descriptionPart);

    public List<Log> findAllByModule(String module);

    public List<Log> findAllByDateBetween(String date1, String date2);

    public List<Log> findAllByDescriptionLikeAndModule(String module, String descriptionPart);

    public List<Log> findAllByDescriptionLikeAndDateBetween(String descriptionPart, String date1, String date2);

    public List<Log> findAllByDescriptionLikeAndModuleAndDateBetween(String module, String descriptionPart, String date1, String date2);
}
