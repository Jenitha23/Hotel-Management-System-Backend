package com.hotelmanagement.bookingadmin.repository;

import com.hotelmanagement.bookingadmin.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}
