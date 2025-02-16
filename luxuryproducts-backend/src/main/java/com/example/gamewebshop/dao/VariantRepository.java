package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {
    Optional<Variant> findById(long id);
}
