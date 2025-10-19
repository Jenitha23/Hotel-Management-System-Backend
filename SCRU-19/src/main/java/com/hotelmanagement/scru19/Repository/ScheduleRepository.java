package com.hotelmanagement.scru19.Repository;

import com.hotelmanagement.scru19.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("""
    select s from Schedule s
    where s.staff.id = :staffId
      and s.startAt < :to
      and s.endAt > :from
    order by s.startAt asc
  """)
    List<Schedule> findForStaffInRange(Long staffId, LocalDateTime from, LocalDateTime to);
}