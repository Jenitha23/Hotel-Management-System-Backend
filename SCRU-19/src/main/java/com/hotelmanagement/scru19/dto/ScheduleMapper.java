package com.hotelmanagement.scru19.dto;

import com.hotelmanagement.scru19.model.Schedule;

public class ScheduleMapper {
    public static ScheduleDto toDto(Schedule s) {
        return new ScheduleDto(
                s.getId(), s.getTitle(), s.getLocation(), s.getStatus(),
                s.getStartAt(), s.getEndAt(), s.getNotes()
        );
    }
}