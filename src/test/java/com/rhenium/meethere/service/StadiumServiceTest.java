package com.rhenium.meethere.service;

import com.rhenium.meethere.dao.StadiumDao;
import com.rhenium.meethere.domain.Stadium;
import com.rhenium.meethere.enums.StadiumTypeEnum;
import com.rhenium.meethere.service.impl.StadiumServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


/**
 * @author YueChen
 * @version 1.0
 * @date 2019/12/21 21:34
 */
@SpringBootTest
class StadiumServiceTest {
    @Mock
    private StadiumDao stadiumDao;

    @Mock
    private Stadium stadium;

    @InjectMocks
    private StadiumServiceImpl stadiumService;

    @Test
    void shouldListStadiumItems() {
        ArrayList<Stadium> stadiums = stadiumService.listStadiumItems();
        verify(stadiumDao, times(1))
                .getStadiumList();
    }

    @Test
    void shouldGetStadiumById() {
        Stadium stadium = new Stadium();
        stadium.setStadiumId(1);
        stadium.setStadiumName("Song");
        stadium.setType(1);
        stadium.setDescription("Just for test");
        stadium.setPrice(12.00);
        stadium.setLocation("ZhongShanBei road");
        when(stadiumDao.getStadiumById(1)).thenReturn(stadium);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        Map<String, String> stadiumMsg = stadiumService.getStadiumById(1);

        verify(stadiumDao, times(1)).getStadiumById(idCaptor.capture());
        assertAll(
                () -> assertEquals(1, idCaptor.getValue()),
                () -> assertEquals(stadiumMsg.get("stadiumId"), "1"),
                () -> assertEquals(stadiumMsg.get("stadiumName"), "Song"),
                () -> assertEquals(stadiumMsg.get("typeName"), StadiumTypeEnum.getByCode(1).getType()),
                () -> assertEquals(stadiumMsg.get("location"), "ZhongShanBei road"),
                () -> assertEquals(stadiumMsg.get("price"), "12.0")
        );
    }
}
