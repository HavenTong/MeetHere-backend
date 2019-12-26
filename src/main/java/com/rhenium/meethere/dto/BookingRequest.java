package com.rhenium.meethere.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/24 18:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
    private Integer bookingId;

    private Integer customerId;
    private Integer stadiumId;

    private Integer daysAfterToday;
    private Integer startTime;
    private Integer endTime;

    private BigDecimal priceSum;
    private Boolean paid;
}
