package com.amine.clinic;

import com.amine.clinic.model.Specialty;
import com.amine.clinic.service.SpecialtyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    @Test
    void createAndFindSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("Test Specialty");
        specialty.setDescription("Test description");

        Specialty created = specialtyService.create(specialty);

        assertThat(created.getId()).isNotNull();
        assertThat(specialtyService.findById(created.getId()).getName()).isEqualTo("Test Specialty");
    }
}
