const { Builder, By, Key, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('assert');

describe('Patient Management Tests', function () {
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

    it('should add new patient successfully', async function () {
        try {
            // Navigate to patient list
            await driver.get('http://localhost:3000/patients');

            // Click add new patient button
            await driver.findElement(By.id('add-patient-btn')).click();

            // Fill patient information
            await driver.findElement(By.id('patient-name')).sendKeys('John Doe');
            await driver.findElement(By.id('patient-phone')).sendKeys('0123456789');
            await driver.findElement(By.id('patient-email')).sendKeys('john@example.com');
            await driver.findElement(By.id('patient-address')).sendKeys('123 Test Street');

            // Submit form
            await driver.findElement(By.id('submit-patient')).click();

            // Wait for success message
            await driver.wait(until.elementLocated(By.className('success-message')), 5000);

            // Verify success message
            const successMessage = await driver.findElement(By.className('success-message')).getText();
            assert.ok(successMessage.includes('Patient added successfully'), 'Should show success message');

            console.log('Add patient test passed!');
        } catch (error) {
            console.error('Add patient test failed:', error);
            throw error;
        }
    });

    it('should search patient by name', async function () {
        try {
            // Navigate to patient list
            await driver.get('http://localhost:3000/patients');

            // Search for patient
            await driver.findElement(By.id('search-patient')).sendKeys('John Doe');
            await driver.findElement(By.id('search-btn')).click();

            // Wait for search results
            await driver.wait(until.elementLocated(By.className('patient-list')), 5000);

            // Verify search results
            const patientName = await driver.findElement(By.className('patient-name')).getText();
            assert.ok(patientName.includes('John Doe'), 'Should find the patient');

            console.log('Search patient test passed!');
        } catch (error) {
            console.error('Search patient test failed:', error);
            throw error;
        }
    });

    it('should edit patient information', async function () {
        try {
            // Navigate to patient list
            await driver.get('http://localhost:3000/patients');

            // Search for patient
            await driver.findElement(By.id('search-patient')).sendKeys('John Doe');
            await driver.findElement(By.id('search-btn')).click();

            // Click edit button
            await driver.findElement(By.className('edit-patient-btn')).click();

            // Update patient information
            const phoneInput = await driver.findElement(By.id('patient-phone'));
            await phoneInput.clear();
            await phoneInput.sendKeys('0987654321');

            // Save changes
            await driver.findElement(By.id('save-patient')).click();

            // Wait for success message
            await driver.wait(until.elementLocated(By.className('success-message')), 5000);

            // Verify success message
            const successMessage = await driver.findElement(By.className('success-message')).getText();
            assert.ok(successMessage.includes('Patient updated successfully'), 'Should show success message');

            console.log('Edit patient test passed!');
        } catch (error) {
            console.error('Edit patient test failed:', error);
            throw error;
        }
    });
}); 