package com.backend.c4s.Utility;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class PurchaseIdGenerator {

    private final String PREFIX= "C4S-ORD";
    public final DateTimeFormatter DATE_FORMATTER= DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generatePurchaseId(){
        String datePart= LocalDate.now().format(DATE_FORMATTER);
        String randomPart= UUID.randomUUID().toString().substring(0,8).toUpperCase();
        return String.format("%s-%s-%S", PREFIX, datePart,randomPart);
    }
}
