package com.dk.mosh2.repo;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dk.mosh2.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

}
