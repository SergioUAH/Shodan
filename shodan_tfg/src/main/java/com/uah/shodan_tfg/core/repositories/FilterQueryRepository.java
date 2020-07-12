package com.uah.shodan_tfg.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uah.shodan_tfg.core.entities.FilterQuery;

@Repository
public interface FilterQueryRepository extends JpaRepository<FilterQuery, Integer> {
    List<FilterQuery> findFirst3ByOrderByIdDesc();
}
