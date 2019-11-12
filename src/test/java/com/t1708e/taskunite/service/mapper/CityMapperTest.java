package com.t1708e.taskunite.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CityMapperTest {

    private CityMapper cityMapper;

    @BeforeEach
    public void setUp() {
        cityMapper = new CityMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(cityMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(cityMapper.fromId(null)).isNull();
    }
}
