package com.hotelmanagement.scrum11.rooms;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoomRepository
        extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
}
