const amqp = require('amqplib');

async function pushToQueue(orderId, email) {
    try {
        // 1. Kết nối tới RabbitMQ
        const connection = await amqp.connect('amqp://admin:admin123@localhost');
        const channel = await connection.createChannel();
        const queue = 'email_queue';

        const msg = {
            orderId: orderId,
            to: email,
            subject: 'Xác nhận đơn hàng (Dùng MQ)',
            text: `Chào bạn, đơn hàng #${orderId} của bạn đã được tạo thành công!`
        };

        // 2. Khai báo hàng đợi
        await channel.assertQueue(queue, { durable: true });

        // 3. Gửi job vào queue: SendEmailJob(orderId, email)
        channel.sendToQueue(queue, Buffer.from(JSON.stringify(msg)), {
            persistent: true 
        });

        console.log(" [MQ] Đã đẩy job SendEmailJob vào queue cho đơn hàng: %s", orderId);

        // 4. Đóng kết nối
        setTimeout(() => {
            connection.close();
        }, 500);
    } catch (error) {
        console.error("Lỗi gửi job vào MQ:", error);
    }
}

// Giả lập Order Service
async function placeOrder() {
    console.log("--- Bắt đầu quy trình đặt hàng (CÓ DÙNG MQ) ---");
    console.time('TotalTime');

    const orderId = "ORD-" + Math.floor(Math.random() * 10000);
    const userEmail = 'khaihnguyen1905@gmail.com'; 

    // Bước 1: Ghi DB tạo đơn (Làm phần quan trọng và nhanh)
    console.log(" [1] Đang ghi DB tạo đơn hàng...");
    await new Promise(resolve => setTimeout(resolve, 500)); // Giả lập 0.5s
    console.log(" [1] Đã lưu đơn hàng %s vào DB thành công.", orderId);

    // Bước 2: Đẩy job vào queue: SendEmailJob(orderId, email)
    console.log(" [2] Đang đẩy job gửi email vào hàng đợi...");
    await pushToQueue(orderId, userEmail);

    // Kết thúc: API trả kết quả ngay cho User
    console.log(" [3] Trả kết quả thành công cho khách hàng.");
    console.timeEnd('TotalTime');
    console.log("----------------------------------------------");
}

placeOrder();
