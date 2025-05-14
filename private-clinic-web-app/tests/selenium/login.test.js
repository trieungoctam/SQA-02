const { Builder, By, Key, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('assert');

describe('Login Tests', function () {
    let driver;

    before(async function () {
        // Khởi tạo WebDriver
        driver = await new Builder()
            .forBrowser('chrome')
            .setChromeOptions(new chrome.Options().headless())
            .build();
    });

    after(async function () {
        // Đóng WebDriver sau khi test xong
        await driver.quit();
    });

    it('should login successfully with valid credentials', async function () {
        try {
            // Navigate to login page
            await driver.get('http://localhost:3000/login');

            // Find and fill login form
            await driver.findElement(By.id('username')).sendKeys('admin');
            await driver.findElement(By.id('password')).sendKeys('admin123');
            await driver.findElement(By.id('login-button')).click();

            // Wait for successful login and verify redirect to dashboard
            await driver.wait(until.elementLocated(By.id('dashboard')), 5000);

            // Verify we're on the dashboard
            const currentUrl = await driver.getCurrentUrl();
            assert.ok(currentUrl.includes('/dashboard'), 'Should be redirected to dashboard');

            console.log('Login test passed!');
        } catch (error) {
            console.error('Login test failed:', error);
            throw error;
        }
    });

    it('should show error with invalid credentials', async function () {
        try {
            // Navigate to login page
            await driver.get('http://localhost:3000/login');

            // Find and fill login form with invalid credentials
            await driver.findElement(By.id('username')).sendKeys('invalid');
            await driver.findElement(By.id('password')).sendKeys('invalid123');
            await driver.findElement(By.id('login-button')).click();

            // Wait for error message
            await driver.wait(until.elementLocated(By.className('error-message')), 5000);

            // Verify error message is displayed
            const errorMessage = await driver.findElement(By.className('error-message')).getText();
            assert.ok(errorMessage.includes('Invalid credentials'), 'Should show error message');

            console.log('Invalid login test passed!');
        } catch (error) {
            console.error('Invalid login test failed:', error);
            throw error;
        }
    });
}); 