package com.rhenium.meethere.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

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

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer priceSum;
    private Boolean paid;

    /** For SQL **/
    private String stadiumName;
    private String customerName;
}
