package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionsRepository extends JpaRepository<Options, Long> {
    Optional<Options> findById(long id);

}
