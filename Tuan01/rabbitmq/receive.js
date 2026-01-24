const amqp = require('amqplib');

async function receiveMessage() {
    try {
        const connection = await amqp.connect('amqp://admin:admin123@localhost');
        const channel = await connection.createChannel();

        const queue = 'task_queue';

        await channel.assertQueue(queue, { durable: true });

        // Mỗi lần chỉ nhận 1 tin nhắn để xử lý (tránh quá tải)
        channel.prefetch(1);

        console.log(" [*] Đang chờ tin nhắn trong %s. Nhấn CTRL+C để thoát", queue);

        // Nhận tin nhắn
        channel.consume(queue, (msg) => {
            const content = JSON.parse(msg.content.toString());
            console.log(" [x] Đã nhận: %s", content.text);

            // Xác nhận đã xử lý xong (Acknowledge)
            channel.ack(msg);
        });
    } catch (error) {
        console.error("Lỗi nhận tin nhắn:", error);
    }
}

receiveMessage();