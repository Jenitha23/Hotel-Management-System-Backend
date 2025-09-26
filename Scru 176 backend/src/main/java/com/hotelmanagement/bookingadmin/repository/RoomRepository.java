package com.hotelmanagement.bookingadmin.repository;


import com.hotelmanagement.bookingadmin.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}
