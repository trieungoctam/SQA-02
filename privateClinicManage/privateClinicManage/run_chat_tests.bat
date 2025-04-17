@echo off
echo Running Chat Feature Tests and Generating Coverage Report...

rem Clean and compile the project
call mvnw clean compile

rem Run tests with JaCoCo coverage
call mvnw test -Dtest=com.spring.privateClinicManage.controller.ChatControllerTest,com.spring.privateClinicManage.api.ApiAnyRoleRestControllerChatTest,com.spring.privateClinicManage.utilities.OnlinerUsersTest

rem Generate JaCoCo report
call mvnw jacoco:report

echo.
echo Tests completed. Coverage report generated at:
echo target/site/jacoco/index.html
echo.
echo Please take screenshots of the test results and coverage reports for your documentation.
echo.
pause
