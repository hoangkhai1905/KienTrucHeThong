const nodemailer = require('nodemailer');

// Giả lập hàm gửi email (mất khoảng 2-3 giây)
async function sendEmailSync(email, orderId) {
    console.log(` [Email] Đang kết nối tới SMTP server để gửi mail cho đơn hàng ${orderId}...`);
    
    const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: 'khaihnguyen1905@gmail.com', 
            pass: 'yton kmwb hpbt egst'     
        }
    });

    try {
        await transporter.sendMail({
            from: '"Hệ Thống" <khaihnguyen1905@gmail.com>',
            to: email,
            subject: 'Xác nhận đơn hàng (KHÔNG dùng MQ)',
            text: `Chào bạn, đơn hàng #${orderId} của bạn đã được tạo thành công! (Xử lý đồng bộ)`
        });
        console.log(` [Email] Đã gửi email xong cho ${email}`);
    } catch (error) {
        console.error(` [Email] Lỗi gửi email: ${error.message}`);
    }
}

// Giả lập API đặt hàng KHÔNG dùng MQ
async function placeOrderNoMQ() {
    console.log("--- Bắt đầu quy trình đặt hàng (KHÔNG DÙNG MQ) ---");
    console.time('TotalTime_NoMQ');

    const orderId = "ORD-" + Math.floor(Math.random() * 10000);
    const userEmail = 'khaihnguyen1905@gmail.com'; 

    // Bước 1: Ghi DB tạo đơn
    console.log(" [1] Đang ghi DB tạo đơn hàng...");
    await new Promise(resolve => setTimeout(resolve, 500)); 
    console.log(" [1] Đã lưu đơn hàng %s vào DB thành công.", orderId);

    // Bước 2: Gửi email trực tiếp (Phải đợi!)
    console.log(" [2] Đang gửi email xác nhận trực tiếp (API phải đợi bước này)...");
    await sendEmailSync(userEmail, orderId);

    // Kết thúc: API trả kết quả muộn vì phải đợi gửi email
    console.log(" [3] Trả kết quả thành công cho khách hàng.");
    console.timeEnd('TotalTime_NoMQ');
    console.log("--------------------------------------------------");
}

placeOrderNoMQ();
