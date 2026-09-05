package com.backend.c4s.Utility;

import com.backend.c4s.Exception.BadRequestException;
import com.backend.c4s.Exception.ScheduleConflictException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class SlotValidator {

    private static final LocalTime WORK_START_TIME=LocalTime.of(9,0);
    private static final LocalTime WORK_END_TIME= LocalTime.of(18,0);

    public void validateSlotTiming(LocalDateTime startDateTime, LocalDateTime endDateTime){
        if (startDateTime==null||endDateTime==null){
            throw new BadRequestException("Start and end time must not be null.");
        }
        if (startDateTime.isBefore(LocalDateTime.now())){
            throw new BadRequestException("End time must be after start time.");
        }
        LocalTime startTime= startDateTime.toLocalTime();
        LocalTime endTime= endDateTime.toLocalTime();

        if (startTime.isBefore(WORK_START_TIME) || endTime.isBefore(WORK_END_TIME)){
            throw new BadRequestException("Requested slot is outside business working hours (09:00 AM to 06:00 PM).");
        }
    }
    public void checkSlotOverLap(LocalDateTime newStart, LocalDateTime newEnd, LocalDateTime existingStart, LocalDateTime existingEnd){
        if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)){
            throw new ScheduleConflictException("The requested time slot conflicts with an existing schedule.");
        }
    }
}
