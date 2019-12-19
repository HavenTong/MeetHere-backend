package com.rhenium.meethere.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HavenTong
 * @date 2019/11/22 5:21 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Serializable {
    private static final long serialVersionUID = 7212388806114748909L;

    private Integer bookingId;
    private Integer customerId;
    private Integer stadiumId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    private Integer priceSum;
    private Boolean paid;

    /** For SQL **/
    private String stadiumName;
    private String customerName;
}
