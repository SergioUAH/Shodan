package com.uah.shodan_tfg.dataproviders.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uah.shodan_tfg.dataproviders.dao.FilterQuery;

@Repository
public interface FilterQueryRepository extends JpaRepository<FilterQuery, Integer> {
    List<FilterQuery> findFirst3ByOrderByIdDesc();
}
