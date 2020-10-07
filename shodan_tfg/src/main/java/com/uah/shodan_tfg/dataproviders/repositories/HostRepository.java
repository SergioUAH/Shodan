package com.uah.shodan_tfg.dataproviders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uah.shodan_tfg.dataproviders.dao.Host;

@Repository
public interface HostRepository extends JpaRepository<Host, Integer> {

}
