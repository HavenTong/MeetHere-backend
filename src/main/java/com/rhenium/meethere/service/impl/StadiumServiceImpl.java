package com.rhenium.meethere.service.impl;

import com.rhenium.meethere.dao.AdminDao;
import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Admin;
import com.rhenium.meethere.domain.Booking;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.dto.StadiumRequest;
import com.rhenium.meethere.enums.ResultEnum;
import com.rhenium.meethere.enums.StadiumTypeEnum;
import com.rhenium.meethere.exception.MyException;
import com.rhenium.meethere.service.StadiumService;
import com.rhenium.meethere.util.SaveImageFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/17 22:53
 */
@Service
@Slf4j

public class StadiumServiceImpl implements StadiumService {
    @Autowired
    private StadiumDao stadiumDao;

    @Autowired
    private AdminDao adminDao;

    @Override
    public ArrayList<Stadium> listStadiumItems() {
        return stadiumDao.getStadiumList();
    }

    /**
     * @param id
     * @return 返回场馆相关信息数据
     */
    @Override
    public Map<String, String> getStadiumById(Integer id) {
        Stadium stadium = stadiumDao.getStadiumById(id);
        Map<String, String> stadiumMsg = new HashMap<>();
        stadiumMsg.put("stadiumId", String.valueOf(stadium.getStadiumId()));
        stadiumMsg.put("stadiumName", stadium.getStadiumName());
        StadiumTypeEnum stadiumType = StadiumTypeEnum.getByCode(stadium.getType());
        String stadiumTypeName = stadiumType.getType();
        stadiumMsg.put("typeName", stadiumTypeName);
        stadiumMsg.put("location", stadium.getLocation());
        stadiumMsg.put("description", stadium.getDescription());
        stadiumMsg.put("price", String.valueOf(stadium.getPrice()));
        return stadiumMsg;
    }

    @Override
    public List<Map<String, Object>> findStadiumsForAdmin(int offset, int limit) {
        if (offset < 0) {
            throw new MyException(ResultEnum.INVALID_OFFSET);
        }
        if (limit < 1) {
            throw new MyException(ResultEnum.INVALID_LIMIT);
        }
        List<Stadium> stadiums = stadiumDao.findAllStadiumsForAdmin(offset, limit);
        List<Map<String, Object>> stadiumInfoList = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Stadium stadium : stadiums){
            LocalDate current = LocalDate.now();
            Map<String, Object> stadiumEntry = new HashMap<>();
            List<Booking> bookingList = stadium.getBookingList();
            Map<String, Object> freeTimeMap = new HashMap<>();

            for (int i = 0; i < 3; i++){
                // 通过private函数获取空闲时间
                List<String> freeTime =
                        getSpareTimeFromBookingList(bookingList, current);
                freeTimeMap.put(dateFormatter.format(current), freeTime);
                current = current.plusDays(1);
            }
            stadiumEntry.put("freeTime", freeTimeMap);
            stadiumEntry.put("stadiumId", stadium.getStadiumId());
            stadiumEntry.put("stadiumName", stadium.getStadiumName());
            stadiumEntry.put("price", stadium.getPrice());
            stadiumEntry.put("location", stadium.getLocation());
            stadiumEntry.put("description", stadium.getDescription());
            stadiumEntry.put("type", StadiumTypeEnum.getByCode(stadium.getType()).getType());
            stadiumInfoList.add(stadiumEntry);
        }
        return stadiumInfoList;
    }

    @Override
    public Map<String, String> getStadiumCount() {
        Map<String, String> data = new HashMap<>();
        data.put("count", String.valueOf(stadiumDao.getStadiumCount()));
        return data;
    }

    @Override
    public void deleteStadium(StadiumRequest stadiumRequest) {
        stadiumDao.deleteStadium(stadiumRequest);
    }

    @Override
    public void createStadium(StadiumRequest stadiumRequest) throws Exception {
        Admin admin = adminDao.findAdminById(stadiumRequest.getAdminId());
        if (admin == null) {
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        String imgStr = stadiumRequest.getPictureRaw();
        if (imgStr != null) {
            stadiumRequest.setPicture(SaveImageFileUtil.saveImage(imgStr));
        }

        stadiumDao.createStadium(stadiumRequest);
    }

    @Override
    public void updateStadium(StadiumRequest stadiumRequest) {
        Admin admin = adminDao.findAdminById(stadiumRequest.getAdminId());
        if (admin == null) {
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }

        String imgStr = stadiumRequest.getPictureRaw();

        System.out.println(imgStr);
        if (imgStr != null && !"nil".equals(imgStr)) {
            stadiumRequest.setPicture(SaveImageFileUtil.saveImage(imgStr));
            stadiumDao.updateStadiumWithPicture(stadiumRequest);
        } else {
            stadiumDao.updateStadium(stadiumRequest);
        }
    }

    @Override
    public List<Map<String, Object>> getStadiumTypes() {
        return StadiumTypeEnum.getTypes();
    }

    // TODO: 需要确定返回哪一天的空闲时间
    public List<String> getSpareTimeFromBookingList(List<Booking> bookingList,
                                                     LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        List<String> spareTime = new ArrayList<>();

        // timeSlotTable为一个flag数组，表示该时间槽是否被预约，每小时算一个时间槽
        // e.g. timeSlotTable[0] -> 8:00-9:00，共12个
        boolean[] timeSlotTable = new boolean[12];
        for (boolean used : timeSlotTable) {
            used = false;
        }

        // 只考虑一天内的预约
        for (Booking booking : bookingList) {
            LocalDateTime startTime = booking.getStartTime();
            if (date.isEqual(startTime.toLocalDate())) {
                int start = startTime.getHour();
                int end = booking.getEndTime().getHour();
                for (int i = start; i < end; i++) {
                    timeSlotTable[i - 8] = true;
                }
            }
        }

        // TODO: 通过timeSlotTable中的占用情况返回一个空闲时间的list
        int startIndex = (timeSlotTable[0]) ? 0 : -1;
        int endIndex = 0;
        for (int i = 1; i <= 11; i++) {
            // 若全部空闲则循环没有操作
            if (timeSlotTable[i]) {
                if (!timeSlotTable[i - 1]) {
                    endIndex = i;
                    StringBuilder builder = new StringBuilder();
                    LocalTime startTime = LocalTime.of(startIndex + 9, 0);
                    LocalTime endTime = LocalTime.of(endIndex + 8, 0);
                    builder.append(formatter.format(startTime))
                            .append("-")
                            .append(formatter.format(endTime));
                    spareTime.add(builder.toString());
                }
                if (i <= 10 && !timeSlotTable[i + 1]) {
                    startIndex = i;
                }
            }
        }
        if (!timeSlotTable[11]) {
            StringBuilder builder = new StringBuilder();
            LocalTime startTime = LocalTime.of(startIndex + 9, 0);
            builder.append(formatter.format(startTime))
                    .append("-")
                    .append("20:00");
            spareTime.add(builder.toString());
        }
        return spareTime;
    }

    @Override
    public void updateStadiumInfoByAdmin(StadiumRequest stadiumRequest) {
        Admin admin = adminDao.findAdminById(stadiumRequest.getAdminId());
        if (Objects.isNull(admin)){
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        stadiumDao.updateStadiumInfoByAdmin(stadiumRequest);
    }

    @Override
    public void deleteStadiumInfoByAdmin(StadiumRequest stadiumRequest) {
        Admin admin = adminDao.findAdminById(stadiumRequest.getAdminId());
        if (Objects.isNull(admin)){
            throw new MyException(ResultEnum.ADMIN_NOT_EXIST);
        }
        stadiumDao.deleteStadiumByAdmin(stadiumRequest);
    }
}
