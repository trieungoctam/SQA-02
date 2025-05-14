const { Builder, By, Key, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('assert');

describe('Appointment Tests', function () {
    let driver;

    before(async function () {
        // Khởi tạo WebDriver
        driver = await new Builder()
            .forBrowser('chrome')
            .setChromeOptions(new chrome.Options().headless())
            .build();

        // Login trước khi test
        await driver.get('http://localhost:3000/login');
        await driver.findElement(By.id('username')).sendKeys('admin');
        await driver.findElement(By.id('password')).sendKeys('admin123');
        await driver.findElement(By.id('login-button')).click();
        await driver.wait(until.elementLocated(By.id('dashboard')), 5000);
    });

    after(async function () {
        await driver.quit();
    });

    it('should create new appointment successfully', async function () {
        try {
            // Navigate to appointments page
            await driver.get('http://localhost:3000/appointments');

            // Click create new appointment button
            await driver.findElement(By.id('create-appointment-btn')).click();

            // Select patient
            await driver.findElement(By.id('patient-select')).click();
            await driver.findElement(By.xpath("//option[contains(text(), 'John Doe')]")).click();

            // Select date
            await driver.findElement(By.id('appointment-date')).sendKeys('2024-03-20');

            // Select time
            await driver.findElement(By.id('appointment-time')).sendKeys('10:00');

            // Select doctor
            await driver.findElement(By.id('doctor-select')).click();
            await driver.findElement(By.xpath("//option[contains(text(), 'Dr. Smith')]")).click();

            // Add notes
            await driver.findElement(By.id('appointment-notes')).sendKeys('Regular checkup');

            // Submit appointment
            await driver.findElement(By.id('submit-appointment')).click();

            // Wait for success message
            await driver.wait(until.elementLocated(By.className('success-message')), 5000);

            // Verify success message
            const successMessage = await driver.findElement(By.className('success-message')).getText();
            assert.ok(successMessage.includes('Appointment created successfully'), 'Should show success message');

            console.log('Create appointment test passed!');
        } catch (error) {
            console.error('Create appointment test failed:', error);
            throw error;
        }
    });

    it('should view appointment details', async function () {
        try {
            // Navigate to appointments page
            await driver.get('http://localhost:3000/appointments');

            // Search for appointment
            await driver.findElement(By.id('search-appointment')).sendKeys('John Doe');
            await driver.findElement(By.id('search-btn')).click();

            // Click on appointment
            await driver.findElement(By.className('appointment-item')).click();

            // Wait for details to load
            await driver.wait(until.elementLocated(By.className('appointment-details')), 5000);

            // Verify appointment details
            const patientName = await driver.findElement(By.className('patient-name')).getText();
            const appointmentDate = await driver.findElement(By.className('appointment-date')).getText();

            assert.ok(patientName.includes('John Doe'), 'Should show correct patient name');
            assert.ok(appointmentDate.includes('2024-03-20'), 'Should show correct appointment date');

            console.log('View appointment details test passed!');
        } catch (error) {
            console.error('View appointment details test failed:', error);
            throw error;
        }
    });

    it('should cancel appointment', async function () {
        try {
            // Navigate to appointments page
            await driver.get('http://localhost:3000/appointments');

            // Search for appointment
            await driver.findElement(By.id('search-appointment')).sendKeys('John Doe');
            await driver.findElement(By.id('search-btn')).click();

            // Click cancel button
            await driver.findElement(By.className('cancel-appointment-btn')).click();

            // Confirm cancellation
            await driver.findElement(By.id('confirm-cancel')).click();

            // Wait for success message
            await driver.wait(until.elementLocated(By.className('success-message')), 5000);

            // Verify success message
            const successMessage = await driver.findElement(By.className('success-message')).getText();
            assert.ok(successMessage.includes('Appointment cancelled successfully'), 'Should show success message');

            console.log('Cancel appointment test passed!');
        } catch (error) {
            console.error('Cancel appointment test failed:', error);
            throw error;
        }
    });
}); 