# Test Documentation for Private Clinic Management System

This document provides a comprehensive overview of all test cases implemented in the Private Clinic Management System, organized by functionality.

## Table of Contents
1. [Payment Functionality](#1-payment-functionality)
2. [Medical Registry Management](#2-medical-registry-management)
3. [User Approval System](#3-user-approval-system)
4. [System Data Management](#4-system-data-management)

## 1. Payment Functionality

### 1.1 Payment Controllers

| Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| api/ApiMOMOPaymentControllerTest | payment | Test successful MOMO payment initialization | Valid PaymentInitDto with existing user and medical registry | Success response with payment URL | PASS | Verifies the controller correctly processes payment requests and returns MOMO payment URL |
| api/ApiMOMOPaymentControllerTest | payment | Test payment with non-existent user | PaymentInitDto with valid data but user not logged in | HTTP 404 Not Found response | PASS | Verifies proper error handling when user is not found |
| api/ApiMOMOPaymentControllerTest | payment | Test payment with non-existent medical registry | PaymentInitDto with valid user but non-existent medical registry ID | HTTP 404 Not Found response | PASS | Verifies proper error handling when medical registry is not found |
| api/ApiMOMOPaymentControllerTest | payment_return | Test successful payment return processing | Valid MOMO response parameters | Success response with redirect URL | PASS | Verifies the controller correctly processes payment return callbacks |
| api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Test successful VNPAY payment initialization | Valid PaymentInitDto with existing user and medical registry | Success response with payment URL | PASS | Verifies the controller correctly processes payment requests and returns VNPAY payment URL |
| api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Test payment with non-existent user | PaymentInitDto with valid data but user not logged in | HTTP 404 Not Found response | PASS | Verifies proper error handling when user is not found |
| api/ApiVNPAYPaymentControllerTest | paymentPhase1 | Test payment with non-existent medical registry | PaymentInitDto with valid user but non-existent medical registry ID | HTTP 404 Not Found response | PASS | Verifies proper error handling when medical registry is not found |
| api/ApiVNPAYPaymentControllerTest | payment_return | Test successful payment return processing | Valid VNPAY response parameters | Success response with redirect URL | PASS | Verifies the controller correctly processes payment return callbacks |
| api/ApiYtaRestControllerCashPaymentTest | cashPaymentMrl | Test successful cash payment for registration | Valid CashPaymentDto with existing user and medical registry | HTTP 200 OK with success message | PASS | Verifies the controller correctly processes cash payments for registration |
| api/ApiYtaRestControllerCashPaymentTest | cashPaymentMrl | Test cash payment with non-existent user | CashPaymentDto with valid data but user not logged in | HTTP 404 Not Found response | PASS | Verifies proper error handling when user is not found |
| api/ApiYtaRestControllerCashPaymentTest | cashPaymentMrl | Test cash payment with non-existent medical registry | CashPaymentDto with valid user but non-existent medical registry ID | HTTP 404 Not Found response | PASS | Verifies proper error handling when medical registry is not found |
| api/ApiYtaRestControllerCashPaymentTest | cashPaymentMrl | Test cash payment with invalid medical registry status | CashPaymentDto with valid data but invalid medical registry status | HTTP 401 Unauthorized response | PASS | Verifies proper error handling when medical registry status is invalid |
| api/ApiYtaRestControllerCashPaymentTest | cashPaymentMe | Test successful cash payment for medical examination | Valid CashPaymentDto with existing user and medical examination | HTTP 200 OK with success message | PASS | Verifies the controller correctly processes cash payments for medical examination |

### 1.2 Payment Services

| Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| service/PaymentMOMODetailServiceImplTest | generateMOMOUrlPayment | Test generating MOMO payment URL with valid inputs | Valid amount, MedicalRegistryList, and Voucher | Map containing resultCode "0" and valid payUrl | PASS | Verifies the service correctly generates MOMO payment URLs |
| service/PaymentMOMODetailServiceImplTest | generateMOMOUrlPayment | Test generating MOMO payment URL without voucher | Valid amount, MedicalRegistryList, null Voucher | Map containing resultCode "0" and valid payUrl | PASS | Verifies the service handles payments without vouchers |
| service/PaymentMOMODetailServiceImplTest | generateMOMOUrlPayment | Test generating MOMO payment URL for medical examination | Valid amount, null MedicalRegistryList, MedicalExamination | Map containing resultCode "0" and valid payUrl | PASS | Verifies the service handles payments for medical examinations |
| service/PaymentMOMODetailServiceImplTest | generateMOMOUrlPayment | Test handling error response from MOMO | Valid inputs but MOMO returns error | Map containing error resultCode | PASS | Verifies the service properly handles error responses from MOMO |
| service/PaymentVNPAYDetailServiceImplTest | generateUrlPayment | Test generating VNPAY payment URL with valid inputs | Valid amount, MedicalRegistryList, and Voucher | Valid VNPAY payment URL | PASS | Verifies the service correctly generates VNPAY payment URLs |
| service/PaymentVNPAYDetailServiceImplTest | generateUrlPayment | Test generating VNPAY payment URL without voucher | Valid amount, MedicalRegistryList, null Voucher | Valid VNPAY payment URL | PASS | Verifies the service handles payments without vouchers |
| service/PaymentVNPAYDetailServiceImplTest | generateUrlPayment | Test generating VNPAY payment URL for medical examination | Valid amount, null MedicalRegistryList, MedicalExamination | Valid VNPAY payment URL | PASS | Verifies the service handles payments for medical examinations |
| service/PaymentVNPAYDetailServiceImplTest | generateUrlPayment | Test handling invalid inputs | Invalid inputs (null amount) | Exception thrown | PASS | Verifies the service properly validates inputs |
| service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Test saving valid PaymentDetailPhase1 | Valid PaymentDetailPhase1 object | Repository save method called with correct object | PASS | Verifies the service correctly saves payment details |
| service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Test saving null PaymentDetailPhase1 | Null PaymentDetailPhase1 object | NullPointerException thrown | PASS | Verifies the service handles null inputs |
| service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Test saving PaymentDetailPhase1 with null orderId | PaymentDetailPhase1 with null orderId | Repository save method called | PASS | Verifies service behavior with null orderId |
| service/PaymentDetailPhase1ServiceImplTest | savePdp1 | Test saving PaymentDetailPhase1 with null amount | PaymentDetailPhase1 with null amount | Repository save method called | PASS | Verifies service behavior with null amount |
| service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Test saving valid PaymentDetailPhase2 | Valid PaymentDetailPhase2 object | Repository save method called with correct object | PASS | Verifies the service correctly saves payment details |
| service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Test saving null PaymentDetailPhase2 | Null PaymentDetailPhase2 object | NullPointerException thrown | PASS | Verifies the service handles null inputs |
| service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Test saving PaymentDetailPhase2 with null orderId | PaymentDetailPhase2 with null orderId | Repository save method called | PASS | Verifies service behavior with null orderId |
| service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Test saving PaymentDetailPhase2 with null amount | PaymentDetailPhase2 with null amount | Repository save method called | PASS | Verifies service behavior with null amount |
| service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Test saving PaymentDetailPhase2 with zero amount | PaymentDetailPhase2 with zero amount | Repository save method called | PASS | Verifies service behavior with zero amount |
| service/PaymentDetailPhase2ServiceImplTest | savePdp2 | Test saving PaymentDetailPhase2 with negative amount | PaymentDetailPhase2 with negative amount | Repository save method called | PASS | Verifies service behavior with negative amount |

### 1.3 Payment Repositories

| Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| repository/PaymentDetailRepositoryTest | save/findById | Test saving and retrieving payment details | Valid PaymentDetailPhase1 object | Saved payment can be retrieved with correct data | PASS | Verifies repository correctly saves and retrieves data with rollback |
| repository/PaymentDetailRepositoryTest | statsByRevenue | Test revenue statistics by year | Current year with payments in different months | Accurate revenue statistics by month | PASS | Verifies revenue statistics with rollback and database check |
| repository/PaymentDetailRepositoryTest | statsByRevenue | Test revenue statistics with year having no payments | Year with no payments | Empty statistics list | PASS | Verifies handling of years with no data with rollback |
| repository/PaymentDetailRepositoryTest | findByOrderId | Test finding payment by orderId | Valid orderId | Correct payment detail | PASS | Verifies finding payment by orderId with rollback |

## 2. Medical Registry Management

| Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| service/MedicalRegistryListServiceUnitTest | saveMedicalRegistryList | Test saving valid medical registry | Valid MedicalRegistryList object | Repository save method called once with correct object | PASS | Uses Mockito to verify repository interaction |
| service/MedicalRegistryListServiceUnitTest | findById | Test finding medical registry by ID when exists | Valid ID | Returns correct MedicalRegistryList object | PASS | Verifies service returns correct object |
| service/MedicalRegistryListServiceUnitTest | findById | Test finding medical registry by ID when not exists | Non-existent ID | Returns null | PASS | Verifies service handles non-existent IDs |
| service/MedicalRegistryListServiceUnitTest | findAllMrl | Test finding all medical registries | N/A | Returns list of all medical registries | PASS | Verifies service returns all registries |
| service/MedicalRegistryListServiceUnitTest | findByScheduleAndStatusIsApproved2 | Test finding registries by schedule and status | Valid Schedule and StatusIsApproved objects | Returns matching registries | PASS | Verifies filtering by schedule and status |
| service/MedicalRegistryListServiceUnitTest | countMRLByScheduleAndStatuses | Test counting registries by schedule and statuses | Valid Schedule and list of StatusIsApproved objects | Returns correct count | PASS | Verifies counting functionality |
| service/MedicalRegistryListServiceUnitTest | findAllMrlByUserAndName | Test finding registries by user and name | Valid User and name string | Returns matching registries | PASS | Verifies filtering by user and name |
| service/MedicalRegistryListServiceUnitTest | findAllMrlByUserAndName | Test finding registries by user only | Valid User, null name | Returns all user's registries | PASS | Verifies filtering by user only |
| repository/MedicalRegistryListRepositoryUnitTest | findMRLByUserAndSchedule | Test finding registry by user and schedule | Valid User and Schedule | Returns matching registry | PASS | Verifies repository query |
| repository/MedicalRegistryListRepositoryUnitTest | findByUser | Test finding registries by user | Valid User | Returns all user's registries | PASS | Verifies repository query |
| repository/MedicalRegistryListRepositoryUnitTest | findByScheduleAndStatusIsApproved | Test finding registries by schedule and status | Valid Schedule and StatusIsApproved | Returns matching registries | PASS | Verifies repository query |
| repository/MedicalRegistryListRepositoryUnitTest | findByAnyKey | Test finding registries by keyword | Valid search keyword | Returns registries matching keyword | PASS | Verifies search functionality |
| repository/MedicalRegistryListRepositoryUnitTest | countRegistrationsBetweenDates | Test counting registrations between dates | Valid start and end dates | Returns correct count | PASS | Verifies date range query |
| api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Test successful schedule registration | Valid RegisterScheduleDto | HTTP 200 OK with success message | PASS | Verifies successful registration flow |
| api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Test registration when daily limit reached | Valid RegisterScheduleDto, daily limit reached | HTTP 401 Unauthorized | PASS | Verifies daily limit enforcement |
| api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Test registration with non-existent user | RegisterScheduleDto, user not logged in | HTTP 404 Not Found | PASS | Verifies user validation |
| api/ApiBenhNhanRestControllerUnitTest | registerSchedule | Test registration with non-existent schedule | RegisterScheduleDto with invalid schedule ID | HTTP 404 Not Found | PASS | Verifies schedule validation |
| api/ApiBenhNhanRestControllerUnitTest | getCurrentUserRegisterScheduleList | Test getting user's registrations when exists | Logged in user with registrations | HTTP 200 OK with list of registrations | PASS | Verifies retrieval of user's registrations |
| api/ApiBenhNhanRestControllerUnitTest | getCurrentUserRegisterScheduleList | Test getting registrations for non-existent user | User not logged in | HTTP 404 Not Found | PASS | Verifies user validation |

## 3. User Approval System

| Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| api/ApiYtaRestControllerTest | autoConfirmRegisters | Test successful approval of registration forms | Valid ConfirmRegisterDto with PAYMENTPHASE1 status | HTTP 200 OK with success message | PASS | Verifies successful approval flow |
| api/ApiYtaRestControllerTest | autoConfirmRegisters | Test approval with non-existent user | ConfirmRegisterDto with null current user | HTTP 404 Not Found with error message | PASS | Verifies handling when user doesn't exist |
| api/ApiYtaRestControllerTest | autoConfirmRegisters | Test approval with invalid status | ConfirmRegisterDto with non-existent status | HTTP 404 Not Found with error message | PASS | Verifies status validation |
| api/ApiYtaRestControllerTest | autoConfirmRegisters | Test approval with no forms to approve | Valid inputs but no forms to approve | HTTP 200 OK with appropriate message | PASS | Verifies handling when no forms need approval |
| api/ApiYtaRestControllerTest | autoConfirmRegisters | Test approval with specific emails | ConfirmRegisterDto with specific email list | HTTP 200 OK, only specified emails processed | PASS | Verifies filtering by email |
| service/MedicalRegistryListServiceTest | findById | Test finding medical registry by ID | Valid ID | Correct MedicalRegistryList object | PASS | Verifies service returns correct object |
| service/MedicalRegistryListServiceTest | saveMedicalRegistryList | Test saving medical registry | Valid MedicalRegistryList object | Repository save method called | PASS | Verifies save functionality |
| service/MedicalRegistryListServiceTest | findByScheduleAndStatusIsApproved2 | Test finding registries by schedule and status | Valid Schedule and StatusIsApproved | List of matching registries | PASS | Verifies filtering functionality |
| service/MedicalRegistryListServiceTest | countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved | Test counting user registrations with specific criteria | Valid User, Schedule, cancellation flag, and status | Correct count | PASS | Verifies complex counting query |
| service/MedicalRegistryListServiceTest | countMRLByScheduleAndStatuses | Test counting registries by schedule and multiple statuses | Valid Schedule and list of statuses | Correct count | PASS | Verifies counting with multiple statuses |
| service/StatusIsApprovedServiceTest | findByStatus | Test finding status by name - valid case | Valid status name | Correct StatusIsApproved object | PASS | Verifies status lookup |
| service/StatusIsApprovedServiceTest | findByStatus | Test finding status by name - invalid case | Non-existent status name | Null | PASS | Verifies handling of invalid status names |
| service/StatusIsApprovedServiceTest | findByStatus | Test availability of different status types | Various status names | Corresponding StatusIsApproved objects | PASS | Verifies all required statuses exist |

## 4. System Data Management

| Folder/File | Function Name | Test Case Objective | Input | Expected Output | Test Result | Notes |
|-------------|---------------|---------------------|-------|-----------------|-------------|-------|
| service/StatsServiceTest | getRevenueStatsByYear | Test getting revenue statistics by year | Valid year | List of monthly revenue statistics | PASS | Verifies revenue reporting functionality |
| service/StatsServiceTest | getRevenueStatsByYear | Test getting revenue statistics for year with no data | Year with no data | Empty list | PASS | Verifies handling of years with no data |
| service/StatsServiceTest | getRegistrationStatsByYear | Test getting registration statistics by year | Valid year | List of monthly registration counts | PASS | Verifies registration reporting functionality |
| service/StatsServiceTest | getRegistrationStatsByYear | Test getting registration statistics for year with no data | Year with no data | Empty list | PASS | Verifies handling of years with no data |
| service/StatsServiceTest | getMedicineUsageStats | Test getting medicine usage statistics | N/A | List of medicine usage statistics | PASS | Verifies medicine usage reporting |
| service/StatsServiceTest | getMedicineUsageStats | Test getting medicine usage statistics with no data | Empty repository | Empty list | PASS | Verifies handling of empty data |
| repository/MedicalRegistryListRepositoryTest | countRegistrationsByMonth | Test counting registrations by month and year | Valid month and year | Correct registration count | PASS | Verifies monthly registration counting |
| repository/MedicalRegistryListRepositoryTest | countRegistrationsByMonth | Test counting registrations for month with no data | Month and year with no registrations | Zero count | PASS | Verifies handling of months with no data |
