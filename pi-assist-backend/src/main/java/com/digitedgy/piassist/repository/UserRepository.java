package com.digitedgy.piassist.repository;

import com.digitedgy.piassist.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    public Iterable<User> findAllByTeam(String team);
}
