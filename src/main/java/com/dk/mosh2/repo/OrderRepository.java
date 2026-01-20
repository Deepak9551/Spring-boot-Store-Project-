package com.dk.mosh2.repo;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.dk.mosh2.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dk.mosh2.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, BigInteger>{

    Optional<Order> findByUser(Users user);

    Optional<List<Order>> findAllByUser(Users user);
}
