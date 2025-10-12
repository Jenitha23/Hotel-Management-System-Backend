package com.hotelmanagement.scru167.Repositories;


import com.hotelmanagement.scru167.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
