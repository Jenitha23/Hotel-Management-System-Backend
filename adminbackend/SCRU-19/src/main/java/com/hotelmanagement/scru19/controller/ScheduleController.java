package com.hotelmanagement.scru19.controller;

import com.hotelmanagement.scru19.dto.*;
import com.hotelmanagement.scru19.repository.ScheduleRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "http://localhost:5173")
public class ScheduleController {

    private final ScheduleRepository repo;
    public ScheduleController(ScheduleRepository repository) { this.repo = repository; }

    @GetMapping("/me")
    public List<ScheduleDto> mySchedule(
            @RequestHeader("X-User-Id") Long staffId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return repo.findForStaffInRange(staffId, from, to).stream().map(ScheduleMapper::toDto).toList();
    }
}