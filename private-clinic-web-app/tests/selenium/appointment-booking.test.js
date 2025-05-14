const { Builder, By, Key, until } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');
const assert = require('assert');
const path = require('path');

describe('Đăng ký lịch khám bệnh', function () {
    let driver;
    // Thời gian timeout cho toàn bộ test suite
    this.timeout(30000);

    before(async function () {
        // Tạo ChromeDriver mới, có thể xem được UI
        driver = await new Builder()
            .forBrowser('chrome')
            .setChromeOptions(new chrome.Options().addArguments('--start-maximized'))
            .build();

        // Đăng nhập trước khi kiểm thử
        await driver.get('http://localhost:3000/login');
        await driver.findElement(By.id('email')).sendKeys('patient1@example.com');
        await driver.findElement(By.id('password')).sendKeys('123456');
        await driver.findElement(By.css('button[type="submit"]')).click();

        // Đợi đến khi đăng nhập thành công và chuyển đến trang chủ
        await driver.wait(until.elementLocated(By.css('.home-container')), 5000);
    });

    after(async function () {
        // Đóng trình duyệt sau khi hoàn thành kiểm thử
        if (driver) {
            await driver.quit();
        }
    });

    it('Đăng ký lịch khám mới thành công', async function () {
        try {
            // Điều hướng đến trang đặt lịch khám
            await driver.get('http://localhost:3000/appointment-form');

            // Điền tên người khám
            await driver.findElement(By.id('name')).sendKeys('Nguyễn Thị Test');

            // Chọn ngày khám (ngày trong tương lai)
            // Tính ngày mai
            const tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            const formattedDate = tomorrow.toISOString().split('T')[0]; // Format YYYY-MM-DD

            await driver.findElement(By.id('date')).sendKeys(formattedDate);

            // Nhập triệu chứng
            await driver.findElement(By.id('favor')).sendKeys('Đau đầu, sốt nhẹ, mệt mỏi');

            // Nhấn nút đặt hẹn
            await driver.findElement(By.css('button[type="submit"]')).click();

            // Đợi thông báo thành công
            await driver.wait(until.elementLocated(By.css('.MuiAlert-message')), 5000);

            // Xác minh thông báo thành công
            const successMessage = await driver.findElement(By.css('.MuiAlert-message')).getText();
            assert.ok(successMessage.includes('Đặt lịch thành công'), 'Phải hiển thị thông báo đặt lịch thành công');

            console.log('Kiểm thử đặt lịch khám thành công!');
        } catch (error) {
            console.error('Kiểm thử đặt lịch khám thất bại:', error);
            // Chụp ảnh màn hình khi thất bại
            const screenshot = await driver.takeScreenshot();
            require('fs').writeFileSync('appointment-booking-error.png', screenshot, 'base64');
            throw error;
        }
    });

    it('Không cho phép đặt lịch với ngày trong quá khứ', async function () {
        try {
            // Điều hướng đến trang đặt lịch khám
            await driver.get('http://localhost:3000/appointment-form');

            // Điền tên người khám
            await driver.findElement(By.id('name')).sendKeys('Nguyễn Thị Test');

            // Chọn ngày trong quá khứ
            const pastDate = new Date();
            pastDate.setDate(pastDate.getDate() - 5);
            const formattedPastDate = pastDate.toISOString().split('T')[0]; // Format YYYY-MM-DD

            await driver.findElement(By.id('date')).sendKeys(formattedPastDate);

            // Kiểm tra xem có hiển thị thông báo cảnh báo không
            await driver.wait(async function () {
                // Alert của window sẽ xuất hiện
                try {
                    await driver.switchTo().alert().getText();
                    return true;
                } catch (e) {
                    return false;
                }
            }, 5000, 'Alert không xuất hiện khi chọn ngày trong quá khứ');

            // Xác nhận alert
            const alert = await driver.switchTo().alert();
            const alertText = await alert.getText();
            assert.ok(alertText.includes('Đặt lịch khám khám phải nằm trong khoảng từ ngày mai'), 'Phải hiển thị thông báo cảnh báo về ngày không hợp lệ');
            await alert.accept();

            console.log('Kiểm thử ngăn chặn đặt lịch quá khứ thành công!');
        } catch (error) {
            console.error('Kiểm thử ngăn chặn đặt lịch quá khứ thất bại:', error);
            // Chụp ảnh màn hình khi thất bại
            const screenshot = await driver.takeScreenshot();
            require('fs').writeFileSync('past-date-error.png', screenshot, 'base64');
            throw error;
        }
    });

    it('Xem danh sách lịch khám đã đặt', async function () {
        try {
            // Điều hướng đến trang danh sách đăng ký lịch khám
            await driver.get('http://localhost:3000/user-register-schedule-list');

            // Đợi cho bảng danh sách hiển thị
            await driver.wait(until.elementLocated(By.css('.responsive-table')), 5000);

            // Lấy danh sách các dòng trong bảng
            const tableRows = await driver.findElements(By.css('.table-row'));

            // Kiểm tra có ít nhất một lịch khám đã đăng ký
            assert.ok(tableRows.length > 0, 'Phải có ít nhất một lịch khám đã đăng ký');

            console.log('Kiểm thử xem danh sách lịch khám thành công!');
        } catch (error) {
            console.error('Kiểm thử xem danh sách lịch khám thất bại:', error);
            // Chụp ảnh màn hình khi thất bại
            const screenshot = await driver.takeScreenshot();
            require('fs').writeFileSync('schedule-list-error.png', screenshot, 'base64');
            throw error;
        }
    });
}); 