package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.Story;
import org.springframework.data.repository.CrudRepository;

public interface StoryRepository extends CrudRepository<Story, Integer> {
}
