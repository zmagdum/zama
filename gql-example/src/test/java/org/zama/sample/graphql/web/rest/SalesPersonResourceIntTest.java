package org.zama.sample.graphql.web.rest;

import org.zama.sample.graphql.GqlExampleApp;

import org.zama.sample.graphql.domain.SalesPerson;
import org.zama.sample.graphql.repository.SalesPersonRepository;
import org.zama.sample.graphql.service.dto.SalesPersonDTO;
import org.zama.sample.graphql.service.mapper.SalesPersonMapper;
import org.zama.sample.graphql.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.zama.sample.graphql.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalesPersonResource REST controller.
 *
 * @see SalesPersonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GqlExampleApp.class)
public class SalesPersonResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_HIRE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HIRE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_SALARY = 1L;
    private static final Long UPDATED_SALARY = 2L;

    private static final Long DEFAULT_COMMISSION_PCT = 1L;
    private static final Long UPDATED_COMMISSION_PCT = 2L;

    @Autowired
    private SalesPersonRepository salesPersonRepository;

    @Autowired
    private SalesPersonMapper salesPersonMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalesPersonMockMvc;

    private SalesPerson salesPerson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalesPersonResource salesPersonResource = new SalesPersonResource(salesPersonRepository, salesPersonMapper);
        this.restSalesPersonMockMvc = MockMvcBuilders.standaloneSetup(salesPersonResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesPerson createEntity(EntityManager em) {
        SalesPerson salesPerson = new SalesPerson()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .hireDate(DEFAULT_HIRE_DATE)
            .salary(DEFAULT_SALARY)
            .commissionPct(DEFAULT_COMMISSION_PCT);
        return salesPerson;
    }

    @Before
    public void initTest() {
        salesPerson = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesPerson() throws Exception {
        int databaseSizeBeforeCreate = salesPersonRepository.findAll().size();

        // Create the SalesPerson
        SalesPersonDTO salesPersonDTO = salesPersonMapper.salesPersonToSalesPersonDTO(salesPerson);
        restSalesPersonMockMvc.perform(post("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesPersonDTO)))
            .andExpect(status().isCreated());

        // Validate the SalesPerson in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeCreate + 1);
        SalesPerson testSalesPerson = salesPersonList.get(salesPersonList.size() - 1);
        assertThat(testSalesPerson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSalesPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSalesPerson.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSalesPerson.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSalesPerson.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testSalesPerson.getSalary()).isEqualTo(DEFAULT_SALARY);
        assertThat(testSalesPerson.getCommissionPct()).isEqualTo(DEFAULT_COMMISSION_PCT);
    }

    @Test
    @Transactional
    public void createSalesPersonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesPersonRepository.findAll().size();

        // Create the SalesPerson with an existing ID
        salesPerson.setId(1L);
        SalesPersonDTO salesPersonDTO = salesPersonMapper.salesPersonToSalesPersonDTO(salesPerson);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesPersonMockMvc.perform(post("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesPersonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSalesPeople() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);

        // Get all the salesPersonList
        restSalesPersonMockMvc.perform(get("/api/sales-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesPerson.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(sameInstant(DEFAULT_HIRE_DATE))))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(DEFAULT_SALARY.intValue())))
            .andExpect(jsonPath("$.[*].commissionPct").value(hasItem(DEFAULT_COMMISSION_PCT.intValue())));
    }

    @Test
    @Transactional
    public void getSalesPerson() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);

        // Get the salesPerson
        restSalesPersonMockMvc.perform(get("/api/sales-people/{id}", salesPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesPerson.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.hireDate").value(sameInstant(DEFAULT_HIRE_DATE)))
            .andExpect(jsonPath("$.salary").value(DEFAULT_SALARY.intValue()))
            .andExpect(jsonPath("$.commissionPct").value(DEFAULT_COMMISSION_PCT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSalesPerson() throws Exception {
        // Get the salesPerson
        restSalesPersonMockMvc.perform(get("/api/sales-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesPerson() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);
        int databaseSizeBeforeUpdate = salesPersonRepository.findAll().size();

        // Update the salesPerson
        SalesPerson updatedSalesPerson = salesPersonRepository.findOne(salesPerson.getId());
        updatedSalesPerson
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .hireDate(UPDATED_HIRE_DATE)
            .salary(UPDATED_SALARY)
            .commissionPct(UPDATED_COMMISSION_PCT);
        SalesPersonDTO salesPersonDTO = salesPersonMapper.salesPersonToSalesPersonDTO(updatedSalesPerson);

        restSalesPersonMockMvc.perform(put("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesPersonDTO)))
            .andExpect(status().isOk());

        // Validate the SalesPerson in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeUpdate);
        SalesPerson testSalesPerson = salesPersonList.get(salesPersonList.size() - 1);
        assertThat(testSalesPerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSalesPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSalesPerson.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSalesPerson.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSalesPerson.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testSalesPerson.getSalary()).isEqualTo(UPDATED_SALARY);
        assertThat(testSalesPerson.getCommissionPct()).isEqualTo(UPDATED_COMMISSION_PCT);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesPerson() throws Exception {
        int databaseSizeBeforeUpdate = salesPersonRepository.findAll().size();

        // Create the SalesPerson
        SalesPersonDTO salesPersonDTO = salesPersonMapper.salesPersonToSalesPersonDTO(salesPerson);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesPersonMockMvc.perform(put("/api/sales-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesPersonDTO)))
            .andExpect(status().isCreated());

        // Validate the SalesPerson in the database
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalesPerson() throws Exception {
        // Initialize the database
        salesPersonRepository.saveAndFlush(salesPerson);
        int databaseSizeBeforeDelete = salesPersonRepository.findAll().size();

        // Get the salesPerson
        restSalesPersonMockMvc.perform(delete("/api/sales-people/{id}", salesPerson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SalesPerson> salesPersonList = salesPersonRepository.findAll();
        assertThat(salesPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesPerson.class);
    }
}
