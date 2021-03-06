package com.vbtn.taskunite.web.rest;

import com.vbtn.taskunite.TaskUniteApp;
import com.vbtn.taskunite.domain.UserInformation;
import com.vbtn.taskunite.repository.UserInformationRepository;
import com.vbtn.taskunite.service.UserInformationService;
import com.vbtn.taskunite.service.dto.UserInformationDTO;
import com.vbtn.taskunite.service.mapper.UserInformationMapper;
import com.vbtn.taskunite.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.vbtn.taskunite.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserInformationResource} REST controller.
 */
@SpringBootTest(classes = TaskUniteApp.class)
public class UserInformationResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_BDAY = 1;
    private static final Integer UPDATED_BDAY = 2;

    private static final Integer DEFAULT_BMONTH = 1;
    private static final Integer UPDATED_BMONTH = 2;

    private static final Integer DEFAULT_BYEAR = 1;
    private static final Integer UPDATED_BYEAR = 2;

    private static final Integer DEFAULT_GENDER = 1;
    private static final Integer UPDATED_GENDER = 2;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private UserInformationMapper userInformationMapper;

    @Autowired
    private UserInformationService userInformationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUserInformationMockMvc;

    private UserInformation userInformation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserInformationResource userInformationResource = new UserInformationResource(userInformationService);
        this.restUserInformationMockMvc = MockMvcBuilders.standaloneSetup(userInformationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInformation createEntity(EntityManager em) {
        UserInformation userInformation = new UserInformation()
            .address(DEFAULT_ADDRESS)
            .bday(DEFAULT_BDAY)
            .bmonth(DEFAULT_BMONTH)
            .byear(DEFAULT_BYEAR)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .image(DEFAULT_IMAGE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .deletedAt(DEFAULT_DELETED_AT);
        return userInformation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInformation createUpdatedEntity(EntityManager em) {
        UserInformation userInformation = new UserInformation()
            .address(UPDATED_ADDRESS)
            .bday(UPDATED_BDAY)
            .bmonth(UPDATED_BMONTH)
            .byear(UPDATED_BYEAR)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        return userInformation;
    }

    @BeforeEach
    public void initTest() {
        userInformation = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserInformation() throws Exception {
        int databaseSizeBeforeCreate = userInformationRepository.findAll().size();

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);
        restUserInformationMockMvc.perform(post("/api/user-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInformationDTO)))
            .andExpect(status().isCreated());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeCreate + 1);
        UserInformation testUserInformation = userInformationList.get(userInformationList.size() - 1);
        assertThat(testUserInformation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserInformation.getBday()).isEqualTo(DEFAULT_BDAY);
        assertThat(testUserInformation.getBmonth()).isEqualTo(DEFAULT_BMONTH);
        assertThat(testUserInformation.getByear()).isEqualTo(DEFAULT_BYEAR);
        assertThat(testUserInformation.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUserInformation.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserInformation.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testUserInformation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserInformation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserInformation.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserInformation.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testUserInformation.getDeletedAt()).isEqualTo(DEFAULT_DELETED_AT);
    }

    @Test
    @Transactional
    public void createUserInformationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userInformationRepository.findAll().size();

        // Create the UserInformation with an existing ID
        userInformation.setId(1L);
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInformationMockMvc.perform(post("/api/user-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInformationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserInformations() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        // Get all the userInformationList
        restUserInformationMockMvc.perform(get("/api/user-informations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].bday").value(hasItem(DEFAULT_BDAY)))
            .andExpect(jsonPath("$.[*].bmonth").value(hasItem(DEFAULT_BMONTH)))
            .andExpect(jsonPath("$.[*].byear").value(hasItem(DEFAULT_BYEAR)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].deletedAt").value(hasItem(DEFAULT_DELETED_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        // Get the userInformation
        restUserInformationMockMvc.perform(get("/api/user-informations/{id}", userInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userInformation.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.bday").value(DEFAULT_BDAY))
            .andExpect(jsonPath("$.bmonth").value(DEFAULT_BMONTH))
            .andExpect(jsonPath("$.byear").value(DEFAULT_BYEAR))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.deletedAt").value(DEFAULT_DELETED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserInformation() throws Exception {
        // Get the userInformation
        restUserInformationMockMvc.perform(get("/api/user-informations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();

        // Update the userInformation
        UserInformation updatedUserInformation = userInformationRepository.findById(userInformation.getId()).get();
        // Disconnect from session so that the updates on updatedUserInformation are not directly saved in db
        em.detach(updatedUserInformation);
        updatedUserInformation
            .address(UPDATED_ADDRESS)
            .bday(UPDATED_BDAY)
            .bmonth(UPDATED_BMONTH)
            .byear(UPDATED_BYEAR)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .image(UPDATED_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deletedAt(UPDATED_DELETED_AT);
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(updatedUserInformation);

        restUserInformationMockMvc.perform(put("/api/user-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInformationDTO)))
            .andExpect(status().isOk());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
        UserInformation testUserInformation = userInformationList.get(userInformationList.size() - 1);
        assertThat(testUserInformation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserInformation.getBday()).isEqualTo(UPDATED_BDAY);
        assertThat(testUserInformation.getBmonth()).isEqualTo(UPDATED_BMONTH);
        assertThat(testUserInformation.getByear()).isEqualTo(UPDATED_BYEAR);
        assertThat(testUserInformation.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserInformation.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserInformation.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testUserInformation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserInformation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserInformation.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserInformation.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testUserInformation.getDeletedAt()).isEqualTo(UPDATED_DELETED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingUserInformation() throws Exception {
        int databaseSizeBeforeUpdate = userInformationRepository.findAll().size();

        // Create the UserInformation
        UserInformationDTO userInformationDTO = userInformationMapper.toDto(userInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInformationMockMvc.perform(put("/api/user-informations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInformationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserInformation in the database
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserInformation() throws Exception {
        // Initialize the database
        userInformationRepository.saveAndFlush(userInformation);

        int databaseSizeBeforeDelete = userInformationRepository.findAll().size();

        // Delete the userInformation
        restUserInformationMockMvc.perform(delete("/api/user-informations/{id}", userInformation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserInformation> userInformationList = userInformationRepository.findAll();
        assertThat(userInformationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
