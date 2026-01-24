const amqp = require('amqplib');

async function sendMessage() {
    try {
        // 1. Kết nối tới RabbitMQ (Docker)
        const connection = await amqp.connect('amqp://admin:admin123@localhost');
        const channel = await connection.createChannel();

        const queue = 'task_queue';
        const msg = { id: 1, text: 'Hello RabbitMQ from Node.js!' };

        // 2. Khai báo hàng đợi (đảm bảo nó tồn tại)
        await channel.assertQueue(queue, { durable: true });

        // 3. Gửi tin nhắn (dưới dạng Buffer)
        channel.sendToQueue(queue, Buffer.from(JSON.stringify(msg)), {
            persistent: true // Tin nhắn sẽ không mất khi RabbitMQ restart
        });

        console.log(" [x] Đã gửi: %s", msg.text);

        // 4. Đóng kết nối
        setTimeout(() => {
            connection.close();
            process.exit(0);
        }, 500);
    } catch (error) {
        console.error("Lỗi gửi tin nhắn:", error);
    }
}

sendMessage();