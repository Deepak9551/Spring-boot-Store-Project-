package com.dk.mosh2.repo;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dk.mosh2.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
