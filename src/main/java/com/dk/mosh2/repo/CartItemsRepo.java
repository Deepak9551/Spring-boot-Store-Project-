package com.dk.mosh2.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dk.mosh2.model.CartItem;

public interface CartItemsRepo extends JpaRepository<CartItem, UUID>{

}
