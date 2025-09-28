package com.hotelmanagement.bookingadmin.repository;

import com.hotelmanagement.bookingadmin.domain.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    @Override
    @EntityGraph(attributePaths = {"customer", "room"})
    Page<Booking> findAll(Specification<Booking> spec, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"customer", "room"})
    java.util.Optional<Booking> findById(Long id);
}
