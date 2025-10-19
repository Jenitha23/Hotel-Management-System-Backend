package com.hotelmanagement.scru19.dto;

import com.hotelmanagement.scru19.model.Schedule.Status;
import java.time.LocalDateTime;

public record ScheduleDto(
        Long id, String title, String location, Status status,
        LocalDateTime startAt, LocalDateTime endAt, String notes
) {

}