package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.SupportActivity;
import org.springframework.data.repository.CrudRepository;

public interface SupportActivityRepository extends CrudRepository<SupportActivity, Integer> {
    public Iterable<SupportActivity> findAllBySprintId(Integer sprintId);
}
