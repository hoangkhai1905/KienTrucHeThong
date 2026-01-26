const amqp = require('amqplib');
const nodemailer = require('nodemailer');

async function receiveAndSendEmail() {
    try {
        // Cấu hình transporter cho nodemailer (Sử dụng Gmail hoặc dịch vụ khác)
        const transporter = nodemailer.createTransport({
            service: 'gmail',
            auth: {
                user: 'khaihnguyen1905@gmail.com', 
                pass: 'yton kmwb hpbt egst'     
            }
        });

        const connection = await amqp.connect('amqp://admin:admin123@localhost');
        const channel = await connection.createChannel();

        const queue = 'email_queue';

        await channel.assertQueue(queue, { durable: true });

        // Mỗi lần chỉ nhận 1 tin nhắn để xử lý
        channel.prefetch(1);

        console.log(" [*] Email Worker đang chờ job trong %s. Nhấn CTRL+C để thoát", queue);

        // Nhận tin nhắn từ RabbitMQ
        channel.consume(queue, async (msg) => {
            if (msg !== null) {
                const emailData = JSON.parse(msg.content.toString());
                console.log(" [Worker] Nhận được job: Gửi email cho đơn hàng %s tới %s", emailData.orderId || 'N/A', emailData.to);

                try {
                    // Thực hiện gửi email thật qua nodemailer
                    await transporter.sendMail({
                        from: '"Email Worker" <khaihnguyen1905@gmail.com>',
                        to: emailData.to,
                        subject: emailData.subject,
                        text: emailData.text
                    });

                    console.log(" [Worker] [v] Đã gửi email xong cho khách hàng.");

                    // Xác nhận đã xử lý xong (Acknowledge)
                    channel.ack(msg);
                } catch (emailError) {
                    console.error(" [Worker] [x] Lỗi khi gửi email:", emailError.message);
                    // Nack để tin nhắn quay lại hàng đợi
                    channel.nack(msg);
                }
            }
        });
    } catch (error) {
        console.error("Lỗi kết nối RabbitMQ:", error);
    }
}

receiveAndSendEmail();
