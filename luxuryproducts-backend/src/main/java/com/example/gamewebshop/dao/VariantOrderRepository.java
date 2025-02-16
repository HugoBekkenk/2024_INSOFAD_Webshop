package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.VariantOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantOrderRepository extends JpaRepository<VariantOrder, Long> {
}
