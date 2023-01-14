package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.Feature;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends CrudRepository<Feature, String> {
    public Iterable<Feature> findAllByTeamAndPi(String team, String pi);
}
