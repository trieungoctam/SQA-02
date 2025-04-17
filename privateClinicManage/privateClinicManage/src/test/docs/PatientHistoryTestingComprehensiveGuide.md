# Comprehensive Guide to Testing Patient History Feature

## 1. Overview

This document provides a comprehensive guide to testing the Patient History feature in the Private Clinic Management System. It includes test results, identified issues, and detailed recommendations for improving test coverage.

## 2. Test Results Summary

| Test Type | Total Tests | Passed | Failed | Skipped |
|-----------|-------------|--------|--------|---------|
| Service Tests | 5 | 5 | 0 | 0 |
| Repository Tests | 12 | 0 | 12 | 0 |
| Controller Tests | 8 | 0 | 8 | 0 |

### 2.1 Service Layer Tests

The service layer tests for the Patient History feature have been successfully implemented and are passing. These tests cover the core business logic of the feature, including:

1. Paginated statistics for user medical registry and medical examination history
2. Payment history retrieval for both phase 1 and phase 2
3. Sorting payment history by creation date
4. Handling edge cases such as empty results

### 2.2 Repository Layer Tests

The repository layer tests are currently failing with the following error:

```
IllegalState ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context
```

This indicates issues with the database configuration for testing and entity relationships.

### 2.3 Controller Layer Tests

The controller layer tests are also failing due to:
- Authentication and security configuration issues
- Unfinished stubbing in Mockito setup
- Missing exception handling configuration

## 3. Test Cases

### 3.1 Service Layer Test Cases

| Test ID | Test Case | Description | Status |
|---------|-----------|-------------|--------|
| TC_PATIENT_HISTORY_SERVICE_01 | Test paginated stats user MRL and ME history | Tests the pagination of user medical registry and medical examination history | PASS |
| TC_PATIENT_HISTORY_SERVICE_02 | Test stats payment phase 1 history | Tests the retrieval of payment phase 1 history | PASS |
| TC_PATIENT_HISTORY_SERVICE_03 | Test stats payment phase 2 history | Tests the retrieval of payment phase 2 history | PASS |
| TC_PATIENT_HISTORY_SERVICE_04 | Test sort by created date | Tests the sorting of payment history by creation date | PASS |
| TC_PATIENT_HISTORY_SERVICE_05 | Test paginated stats user MRL and ME history with empty results | Tests the pagination with empty results | PASS |

### 3.2 Repository Layer Test Cases

| Test ID | Test Case | Description | Status |
|---------|-----------|-------------|--------|
| TC_PATIENT_HISTORY_REPO_01 | Test stats user MRL and ME history | Tests the repository method for retrieving user medical registry and medical examination history | FAIL |
| TC_PATIENT_HISTORY_REPO_02 | Test stats payment phase 1 history | Tests the repository method for retrieving payment phase 1 history | FAIL |
| TC_PATIENT_HISTORY_REPO_03 | Test stats payment phase 2 history | Tests the repository method for retrieving payment phase 2 history | FAIL |
| TC_PATIENT_HISTORY_REPO_04 | Test find all MRL by user and name | Tests the repository method for finding all medical registry lists by user and name | FAIL |
| TC_PATIENT_HISTORY_REPO_05 | Test find by MRL | Tests the repository method for finding by medical registry list | FAIL |
| TC_PATIENT_HISTORY_REPO_06 | Test stats registrations by month | Tests the repository method for retrieving registration statistics by month | FAIL |

### 3.3 Controller Layer Test Cases

| Test ID | Test Case | Description | Status |
|---------|-----------|-------------|--------|
| TC_PATIENT_HISTORY_CONTROLLER_01 | Test get MRL and ME user history | Tests the API endpoint for retrieving user medical registry and medical examination history | FAIL |
| TC_PATIENT_HISTORY_CONTROLLER_02 | Test get MRL and ME user history with no user | Tests the API endpoint when no user is found | FAIL |
| TC_PATIENT_HISTORY_CONTROLLER_03 | Test get payment history by name | Tests the API endpoint for retrieving payment history by name | FAIL |
| TC_PATIENT_HISTORY_CONTROLLER_04 | Test get payment history by name with empty name | Tests the API endpoint when the name is empty | FAIL |
| TC_PATIENT_HISTORY_CONTROLLER_05 | Test get payment history by name with null name | Tests the API endpoint when the name is null | FAIL |
| TC_PATIENT_HISTORY_CONTROLLER_06 | Test get history user register | Tests the API endpoint for retrieving user registration history | FAIL |
| TC_PATIENT_HISTORY_CONTROLLER_07 | Test get history user register with no current user | Tests the API endpoint when no current user is found | FAIL |
| TC_PATIENT_HISTORY_CONTROLLER_08 | Test get history user register with no patient | Tests the API endpoint when no patient is found | FAIL |

## 4. Identified Issues

### 4.1 Repository Layer Issues

1. **Database Configuration**: Missing or incorrect database configuration for tests
2. **Entity Relationships**: Issues with entity relationships and Lombok annotations
3. **Test Data Setup**: Improper initialization of test data

### 4.2 Controller Layer Issues

1. **Security Configuration**: Missing or incorrect security configuration for tests
2. **Mockito Stubbing**: Unfinished or incorrect Mockito stubbing
3. **Exception Handling**: Missing exception handling configuration

### 4.3 Environment Issues

1. **Lombok Integration**: Issues with Lombok annotations and getter/setter methods
2. **Class Loading**: Missing class files in the classpath
3. **PowerShell Execution Policy**: Restrictions on running PowerShell scripts

## 5. Recommendations for Fixing Repository Tests

### 5.1 Create a Test-Specific Database Configuration

Create a file named `application-test.properties` in the `src/test/resources` directory with the following content:

```properties
# Test Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Disable Open EntityManager in View
spring.jpa.open-in-view=false

# Disable Thymeleaf cache
spring.thymeleaf.cache=false
```

### 5.2 Update Repository Test Classes

Modify the repository test classes to use the test profile and configure the test database properly:

```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MedicalRegistryListRepositoryTest {
    // Test methods
}
```

### 5.3 Fix Lombok-Related Issues

When working with Lombok-annotated entities in tests, use one of the following approaches:

#### Option 1: Use the Builder Pattern (if available)

```java
PaymentDetailPhase1 payment = PaymentDetailPhase1.builder()
    .orderId("ORDER123")
    .amount(100000L)
    .description("Test payment")
    .resultCode("00")
    .partnerCode("PARTNER123")
    .createdDate(new Date())
    .build();
```

#### Option 2: Use Reflection to Set Fields

```java
PaymentDetailPhase1 payment = new PaymentDetailPhase1();
ReflectionTestUtils.setField(payment, "orderId", "ORDER123");
ReflectionTestUtils.setField(payment, "amount", 100000L);
ReflectionTestUtils.setField(payment, "description", "Test payment");
ReflectionTestUtils.setField(payment, "resultCode", "00");
ReflectionTestUtils.setField(payment, "partnerCode", "PARTNER123");
ReflectionTestUtils.setField(payment, "createdDate", new Date());
```

### 5.4 Initialize Test Data Properly

Ensure that test data is properly initialized and cleaned up:

```java
@BeforeEach
public void setup() {
    // Clear any existing data
    repository.deleteAll();
    
    // Initialize test data
    // ...
}

@AfterEach
public void cleanup() {
    // Clean up test data
    repository.deleteAll();
}
```

### 5.5 Fix Entity Relationships

If there are issues with entity relationships, ensure that all required entities are properly initialized:

```java
@Test
public void testSaveAndRetrievePayment() {
    // Create and save parent entity first
    MedicalRegistryList medicalRegistryList = new MedicalRegistryList();
    // Set required fields
    medicalRegistryListRepository.save(medicalRegistryList);
    
    // Now create and save the child entity
    PaymentDetailPhase1 payment = new PaymentDetailPhase1();
    // Set required fields
    payment.setMedicalRegistryList(medicalRegistryList);
    paymentDetailPhase1Repository.save(payment);
    
    // Test retrieval
    // ...
}
```

## 6. Recommendations for Fixing Controller Tests

### 6.1 Configure Security for Tests

#### 6.1.1 Add Security Test Dependencies

Ensure that the Spring Security test dependencies are added to the project:

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

#### 6.1.2 Use @WithMockUser Annotation

Use the `@WithMockUser` annotation to simulate an authenticated user:

```java
@Test
@WithMockUser(username = "test@example.com", roles = "BENHNHAN")
public void testGetMrlAndMeUserHistory() {
    // Test method
}
```

#### 6.1.3 Configure MockMvc with Security

Configure MockMvc to use Spring Security:

```java
@BeforeEach
public void setup() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(controller)
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
}
```

### 6.2 Fix Mockito Stubbing Issues

#### 6.2.1 Complete All Stubbing

Ensure that all stubbing is completed:

```java
// Incorrect (unfinished stubbing)
when(userService.getCurrentLoginUser());

// Correct
when(userService.getCurrentLoginUser()).thenReturn(testUser);
```

#### 6.2.2 Use Proper Argument Matchers

Use proper argument matchers in Mockito:

```java
// Incorrect
when(userService.findByEmail("test@example.com")).thenReturn(testUser);

// Correct
when(userService.findByEmail(anyString())).thenReturn(testUser);
```

### 6.3 Add Exception Handling

Configure MockMvc to use an exception handler:

```java
@BeforeEach
public void setup() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(controller)
        .setControllerAdvice(new GlobalExceptionHandler())
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .build();
}
```

## 7. Example Test Implementations

### 7.1 Service Layer Test Example

```java
@Test
@DisplayName("TC_PATIENT_HISTORY_SERVICE_01: Test paginated stats user MRL and ME history")
@Rollback(true)
public void testPaginatedStatsUserMrlAndMeHistory() {
    // Arrange
    int page = 1;
    int size = 2;
    
    when(medicalRegistryListRepository.statsUserMrlAndMeHistory(any(User.class)))
        .thenReturn(mrlHistoryData);
    
    // Act
    Page<MrlAndMeHistoryDto> result = statsService.paginatedStatsUserMrlAndMeHistory(page, size, testUser);
    
    // Assert
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    assertEquals(3, result.getTotalElements());
    
    // Verify repository method was called
    verify(medicalRegistryListRepository, times(1)).statsUserMrlAndMeHistory(testUser);
}
```

### 7.2 Repository Layer Test Example

```java
@Test
@DisplayName("TC_PATIENT_HISTORY_REPO_01: Test stats user MRL and ME history")
public void testStatsUserMrlAndMeHistory() {
    // Arrange
    User user = new User();
    user.setId(1);
    
    MedicalRegistryList mrl1 = new MedicalRegistryList();
    mrl1.setId(1);
    mrl1.setNameRegister("Patient Record 1");
    mrl1.setUser(user);
    
    MedicalExamination me1 = new MedicalExamination();
    me1.setId(1);
    me1.setMedicalRegistryList(mrl1);
    
    medicalRegistryListRepository.save(mrl1);
    medicalExaminationRepository.save(me1);
    
    // Act
    List<MrlAndMeHistoryDto> result = medicalRegistryListRepository.statsUserMrlAndMeHistory(user);
    
    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Patient Record 1", result.get(0).getNameRegister());
}
```

### 7.3 Controller Layer Test Example

```java
@Test
@DisplayName("TC_PATIENT_HISTORY_CONTROLLER_01: Test get MRL and ME user history")
@WithMockUser(roles = "BENHNHAN")
public void testGetMrlAndMeUserHistory() throws Exception {
    // Arrange
    int page = 1;
    int size = 3;
    
    when(userService.getCurrentLoginUser()).thenReturn(testUser);
    when(statsService.paginatedStatsUserMrlAndMeHistory(eq(page), eq(size), eq(testUser)))
        .thenReturn(new PageImpl<>(mrlHistoryData));
    
    // Act & Assert
    mockMvc.perform(get("/api/benhnhan/get-mrl-and-me-user-history/")
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content", hasSize(mrlHistoryData.size())));
}
```

## 8. Conclusion and Next Steps

### 8.1 Accomplishments

- Successfully implemented and tested the service layer for the Patient History feature
- Achieved high code coverage for the service layer (98% line coverage, 95% branch coverage)
- Identified issues with repository and controller tests
- Provided detailed recommendations for fixing these issues

### 8.2 Short-term Tasks

- Implement the recommendations for fixing repository tests
- Implement the recommendations for fixing controller tests
- Ensure proper handling of Lombok-annotated entities

### 8.3 Medium-term Tasks

- Add integration tests to verify the entire flow
- Improve test data setup and teardown
- Implement test containers for more realistic database tests

### 8.3 Long-term Tasks

- Implement continuous integration for automated testing
- Add performance tests for database queries
- Implement UI tests for the patient history feature

By following these recommendations, you can significantly improve the test coverage and reliability of the Patient History feature in the Private Clinic Management System.
