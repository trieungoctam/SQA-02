const {Builder, By, Key, until} = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('assert');

describe('Consultation Tests', function() {
    let driver;

    before(async function() {
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

    after(async function() {
        await driver.quit();
    });

    it('should post new consultation question', async function() {
        try {
            // Navigate to consultation page
            await driver.get('http://localhost:3000/consultation');
            
            // Click new question button
            await driver.findElement(By.id('new-question-btn')).click();
            
            // Fill question form
            await driver.findElement(By.id('question-title')).sendKeys('Fever and sore throat');
            await driver.findElement(By.id('question-content')).sendKeys('I have been having fever and sore throat for 2 days. What should I do?');
            await driver.findElement(By.id('question-category')).click();
            await driver.findElement(By.xpath("//option[contains(text(), 'General Health')]")).click();
            
            // Submit question
            await driver.findElement(By.id('submit-question')).click();
            
            // Wait for success message
            await driver.wait(until.elementLocated(By.className('success-message')), 5000);
            
            // Verify success message
            const successMessage = await driver.findElement(By.className('success-message')).getText();
            assert.ok(successMessage.includes('Question posted successfully'), 'Should show success message');
            
            console.log('Post question test passed!');
        } catch (error) {
            console.error('Post question test failed:', error);
            throw error;
        }
    });

    it('should like a question', async function() {
        try {
            // Navigate to consultation page
            await driver.get('http://localhost:3000/consultation');
            
            // Find and click like button on first question
            const likeButton = await driver.findElement(By.className('like-button'));
            const initialLikes = await likeButton.getText();
            await likeButton.click();
            
            // Wait for like count to update
            await driver.wait(async () => {
                const newLikes = await likeButton.getText();
                return newLikes !== initialLikes;
            }, 5000);
            
            // Verify like count increased
            const newLikes = await likeButton.getText();
            assert.ok(parseInt(newLikes) > parseInt(initialLikes), 'Like count should increase');
            
            console.log('Like question test passed!');
        } catch (error) {
            console.error('Like question test failed:', error);
            throw error;
        }
    });

    it('should view and reply to questions', async function() {
        try {
            // Navigate to consultation page
            await driver.get('http://localhost:3000/consultation');
            
            // Click on first question
            await driver.findElement(By.className('question-item')).click();
            
            // Wait for question details to load
            await driver.wait(until.elementLocated(By.className('question-details')), 5000);
            
            // Add reply
            await driver.findElement(By.id('reply-content')).sendKeys('This is a test reply');
            await driver.findElement(By.id('submit-reply')).click();
            
            // Wait for reply to appear
            await driver.wait(until.elementLocated(By.className('reply-item')), 5000);
            
            // Verify reply is displayed
            const replyText = await driver.findElement(By.className('reply-content')).getText();
            assert.ok(replyText.includes('This is a test reply'), 'Should show the reply');
            
            console.log('View and reply test passed!');
        } catch (error) {
            console.error('View and reply test failed:', error);
            throw error;
        }
    });
}); 