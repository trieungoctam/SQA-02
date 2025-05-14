const { Builder, By, Key, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('assert');

describe('Medical History Tests', function () {
    let driver;

    before(async function () {
        driver = await new Builder()
            .forBrowser('chrome')
            .setChromeOptions(new chrome.Options().headless())
            .build();

        // Login trước khi test
        await driver.get('http://localhost:3000/login');
        await driver.findElement(By.id('username')).sendKeys('patient');
        await driver.findElement(By.id('password')).sendKeys('patient123');
        await driver.findElement(By.id('login-button')).click();
        await driver.wait(until.elementLocated(By.id('dashboard')), 5000);
    });

    after(async function () {
        await driver.quit();
    });

    it('should view medical history', async function () {
        try {
            // Navigate to medical history page
            await driver.get('http://localhost:3000/medical-history');

            // Wait for history list to load
            await driver.wait(until.elementLocated(By.className('history-list')), 5000);

            // Verify history items are displayed
            const historyItems = await driver.findElements(By.className('history-item'));
            assert.ok(historyItems.length > 0, 'Should show medical history items');

            // Click on first history item
            await historyItems[0].click();

            // Wait for details to load
            await driver.wait(until.elementLocated(By.className('history-details')), 5000);

            // Verify details are displayed
            const diagnosis = await driver.findElement(By.className('diagnosis')).getText();
            const prescription = await driver.findElement(By.className('prescription')).getText();

            assert.ok(diagnosis.length > 0, 'Should show diagnosis');
            assert.ok(prescription.length > 0, 'Should show prescription');

            console.log('View medical history test passed!');
        } catch (error) {
            console.error('View medical history test failed:', error);
            throw error;
        }
    });

    it('should view and download invoice', async function () {
        try {
            // Navigate to invoices page
            await driver.get('http://localhost:3000/invoices');

            // Wait for invoice list to load
            await driver.wait(until.elementLocated(By.className('invoice-list')), 5000);

            // Verify invoices are displayed
            const invoiceItems = await driver.findElements(By.className('invoice-item'));
            assert.ok(invoiceItems.length > 0, 'Should show invoice items');

            // Click on first invoice
            await invoiceItems[0].click();

            // Wait for invoice details to load
            await driver.wait(until.elementLocated(By.className('invoice-details')), 5000);

            // Verify invoice details
            const totalAmount = await driver.findElement(By.className('total-amount')).getText();
            const services = await driver.findElement(By.className('services-list')).getText();

            assert.ok(totalAmount.includes('$'), 'Should show total amount');
            assert.ok(services.length > 0, 'Should show services list');

            // Click download button
            await driver.findElement(By.id('download-invoice')).click();

            // Wait for download to start
            await driver.wait(until.elementLocated(By.className('download-success')), 5000);

            console.log('View and download invoice test passed!');
        } catch (error) {
            console.error('View and download invoice test failed:', error);
            throw error;
        }
    });

    it('should filter medical history by date range', async function () {
        try {
            // Navigate to medical history page
            await driver.get('http://localhost:3000/medical-history');

            // Set date range
            await driver.findElement(By.id('start-date')).sendKeys('2024-01-01');
            await driver.findElement(By.id('end-date')).sendKeys('2024-12-31');
            await driver.findElement(By.id('apply-filter')).click();

            // Wait for filtered results
            await driver.wait(until.elementLocated(By.className('history-list')), 5000);

            // Verify filtered results
            const historyItems = await driver.findElements(By.className('history-item'));
            const firstItemDate = await historyItems[0].findElement(By.className('visit-date')).getText();

            assert.ok(new Date(firstItemDate) >= new Date('2024-01-01'), 'Should show items after start date');
            assert.ok(new Date(firstItemDate) <= new Date('2024-12-31'), 'Should show items before end date');

            console.log('Filter medical history test passed!');
        } catch (error) {
            console.error('Filter medical history test failed:', error);
            throw error;
        }
    });
}); 