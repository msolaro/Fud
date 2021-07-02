package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PojoRepository extends JpaRepository<Pojo, Long> {

}
