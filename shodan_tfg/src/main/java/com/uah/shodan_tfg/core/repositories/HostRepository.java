package com.uah.shodan_tfg.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uah.shodan_tfg.core.entities.Host;

@Repository
public interface HostRepository extends JpaRepository<Host, Integer> {

}
