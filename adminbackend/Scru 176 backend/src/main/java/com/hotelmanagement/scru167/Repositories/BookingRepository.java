package com.hotelmanagement.scru167.Repositories;


import com.hotelmanagement.scru167.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {}
