package com.dk.mosh2.repo;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dk.mosh2.model.OrderItem;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, BigInteger> {

}
