# Buổi 4 — Architecture Styles (Microservices, SOA)

## Nguyễn Hoàng Khải - 22714331

## Chủ đề: Distributed architecture style.

### Với hệ thống “Online Food Delivery”:

#### 1. Thiết kế bản monolith → microservices migration.

#### 1.1. Kiến trúc monolith ban đầu
- **Đặc điểm:** Toàn bộ hệ thống Online Food Delivery (từ quản lý User, Restaurant, Menu, Order, Payment, đến Delivery) được đóng gói chung thành một codebase duy nhất và deploy trên một server/cluster, sử dụng chung một cơ sở dữ liệu (Single Data Store).
- **Vấn đề gặp phải:**
    - **Spaghetti code:** Khi logic nghiệp vụ phức tạp lên, việc bảo trì và thêm mới tính năng rất khó khăn.
    - **Single Point of Failure:** Lỗi ở module thanh toán hoặc module tracking tài xế (ăn nhiều bộ nhớ) có thể làm sập toàn bộ hệ thống.
    - **Không thể scale linh hoạt:** Vào giờ cao điểm, tính năng Order/Delivery chịu tải lớn nhưng hệ thống bắt buộc phải scale thêm tài nguyên cho cả những tính năng không cần thiết.

#### 1.2. Mục tiêu khi migrate sang microservices
- **Khả năng mở rộng độc lập (Independent Scalability):** Hệ thống có thể scale riêng lẻ các service nhận nhiều lượng truy cập (ví dụ: hệ thống tracking tài xế hay tạo đơn hàng) mà không ảnh hưởng module khác.
- **Tính khả dụng và chịu lỗi cao (Fault Tolerance):** Một service chết (ví dụ: Notification) sẽ không làm gián đoạn luồng đặt đồ ăn.
- **Phát triển và triển khai nhanh chóng (Rapid Deployment):** Các team (squad) khác nhau có thể code, test và deploy từng service một cách độc lập không chờ đợi nhau.
- **Tối ưu hóa công nghệ (Polyglot Persistence):** Tùy chọn DB phù hợp với từng nghiệp vụ (VD: MongoDB cho Menu linh hoạt, PostgreSQL/MySQL cho Payment cần tính ACID).

#### 1.3. Nguyên tắc migrate đúng
- **Strangler Fig Pattern:** Không đập đi xây lại hệ thống (Big Bang). Thay vào đó, trích xuất từng module từ Monolith ra thành Service riêng, đồng thời điều hướng dần traffic qua Gateway.
- **Phân tách theo Bounded Context (Domain-Driven Design):** Chia module dựa trên giới hạn nghiệp vụ, đảm bảo mỗi service làm tốt một việc duy nhất.
- **Database per Service:** Mỗi microservice phải sở hữu một Database độc lập, tuyệt đối không query chung database để tránh hidden coupling.
- **Giao tiếp bất đồng bộ (Async/Event-driven):** Hạn chế tối đa gọi API trực tiếp giữa các service ở các flow chính, ưu tiên dùng Message Broker (Kafka/RabbitMQ) để giảm độ trễ và tránh sập dây chuyền.

#### 1.4. Lộ trình migrate đề xuất
Lộ trình được chia làm 4 giai đoạn chính để đảm bảo hệ thống cũ vẫn hoạt động trơn tru:
- **Giai đoạn 1: Chuẩn bị hạ tầng & Tách các Edge Services (Dịch vụ râu ria)**
    - Xây dựng nền tảng: Cài đặt API Gateway, Service Registry, và hệ thống CI/CD pipeline.
    - Tách **Notification Service** (gửi SMS, Email, Push App): Module này ít tạo rủi ro cho Core Business, được tách ra trước để đội ngũ làm quen dần với môi trường triển khai phân tán.
- **Giai đoạn 2: Tách các dịch vụ Read-Heavy (Nặng về Đọc)**
    - Tách **User Service** (Tài khoản, Auth) và **Restaurant & Menu Service** (Quản lý nhà hàng, thực đơn món ăn).
    - Các dịch vụ này chủ yếu phục vụ tra cứu thông tin. API Gateway sẽ định tuyến truy vấn danh sách món ăn vào Microservice mới, trong khi phần thanh toán/đặt hàng vẫn chuyển vào Monolith.
- **Giai đoạn 3: Tách Core Transactional Services (Giao dịch cốt lõi)**
    - Tách **Order Service** và **Payment Service**.
    - Đây là bước khó nhất vì liên quan đến toàn vẹn dữ liệu. Áp dụng Saga Pattern / Outbox Pattern và Event-Driven (Kafka) để đảm bảo tính nhất quán (VD: Đơn hàng tạo -> Gửi Event -> Thanh toán -> Cập nhật trạng thái đơn).
- **Giai đoạn 4: Tách Real-time Services (Dịch vụ thời gian thực)**
    - Tách **Delivery Service** chuyên lo phần tracking vị trí tài xế, điều phối tìm kiếm tài xế. Dịch vụ này đòi hỏi tần suất read/write tọa độ cực cao (có thể sử dụng caching Redis, NoSQL).
    - Tới đây, Monolith đã bị "bóp nghẹt" (strangled) hoàn toàn, tất cả traffic đã ở Microservices, ta có thể retire (xóa bỏ) app Monolith đi.

#### 1.5. Danh sách microservices đề xuất
1. **User Service:** Quản trị mọi loại tài khoản (Customer, Driver, Restaurant Owner, Admin), xử lý Login/Register, Token JWT.
2. **Restaurant & Menu Service:** Quản lý thông tin profile nhà hàng, thông tin món ăn, Category, giá bán, khuyến mãi.
3. **Order Service:** Xử lý việc tạo mới đơn hàng (Checkout cart), quản lý toàn bộ State (vòng đời) của đơn (Created -> Confirmed -> Preparing -> Delivering -> Completed).
4. **Payment Service:** Chuyên giao tiếp với 3rd-party Payment Gateways (Stripe, VNPay, MOMO...), xử lý ví nội bộ (nếu có), Refund.
5. **Delivery Service:** Hệ thống Matching (Tìm tài xế gần nhất với nhà hàng), tích hợp bản đồ, cập nhật định vị GPS thời gian thực.
6. **Notification Service:** Unified hub dành riêng cho việc bắn thông báo đa kênh.

#### 1.6. Vì sao cách tách này hợp lý?
- Ranh giới nghiệp vụ (Boundaries) cực kì rõ ràng đúng chuẩn Domain-Driven Design; một team quản lý User sẽ không bị dẫm chân lên team Order/Delivery.
- Giải quyết đúng "Pain-points" của mô hình cũ: Giai đoạn 4 tách ra một Service Tracking độc lập vì tính năng GPS sinh ra lượng tải IO cực lớn, việc tách riêng tránh tải xử lý GPS làm chậm quá trình tra cứu thực đơn (Menu) hay thanh toán (Payment).
- Sử dụng triệt để lợi thế "Polyglot": Dịch vụ Restaurant & Menu sử dụng NoSQL (VD MongoDB) lưu data không có structure cố định dễ dàng mở rộng, query nhanh. Order / Payment lại cần cấu trúc Relational (PostgreSQL) để giữ tính chặt chẽ ACID.
- Đảm bảo High-availability: Khi Payment Gateway đối tác có sự cố, Order Service vẫn tiếp nhận đơn, chuyển trạng thái đơn hàng thành "Awaiting Payment" và thử retry (hoặc hold) thông qua Kafka Event-Driven thay vì chặn đứng luồn thao tác của User.

#### 2. Xác định bounded context.

Theo tư tưởng Domain-Driven Design (DDD), việc phân định Bounded Context giúp chúng ta vạch ra ranh giới rõ ràng về mặt logic nghiệp vụ và dữ liệu, tránh tình trạng giẫm chân lên nhau. Dưới đây là các Bounded Context chính cho hệ thống Food Delivery:

| STT | Bounded Context | Trách nhiệm chính (Responsibilities) | Core Entities |
| :--- | :--- | :--- | :--- |
| **1** | **Identity & Access Context** | Quản lý vòng đời tài khoản, xác thực (Authentication), phân quyền (Authorization) và hồ sơ cá nhân của Khách hàng, Tài xế, Chủ nhà hàng, Admin. | `User`, `Role`, `Credential`, `Profile`, `Address` |
| **2** | **Catalog & Restaurant Context** | Quản lý thông tin profile nhà hàng, giờ hoạt động; quản lý thực đơn, danh mục món ăn. Phục vụ việc tra cứu, tìm kiếm món ăn của khách hàng. | `Restaurant`, `Menu`, `MenuItem`, `Category`, `Review` |
| **3** | **Ordering Context** | Quản lý toàn bộ vòng đời của Đơn hàng (từ lúc tạo giỏ hàng, đặt hàng, xử lý các thay đổi trạng thái đơn). Giao tiếp với các context khác để hoàn tất quá trình "Mua - Bán". | `Order`, `OrderItem`, `Cart`, `OrderStatus` |
| **4** | **Billing & Payment Context** | Xử lý các giao dịch tiền bạc, tương tác với cổng thanh toán bên thứ 3 (Stripe, VNPay...), quản lý ví nội bộ, xuất biên lai và hoàn tiền (Refund). | `PaymentTransaction`, `Invoice`, `Wallet`, `PaymentMethod` |
| **5** | **Fulfillment & Logistics Context** | Phụ trách mảng giao nhận: thu thập tọa độ GPS thời gian thực, thuật toán matching tài xế gần nhất, tính khoảng cách và thời gian dự kiến (ETA). | `Delivery`, `DriverLocation`, `Route`, `DeliveryStatus` |
| **6** | **Communication Context** | Đóng vai trò làm trung tâm phát đa thông báo. Lắng nghe sự kiện từ các Context khác để gửi Email, SMS, Push Notification tới thiết bị người dùng. | `NotificationTemplate`, `Message`, `DeviceToken` |

#### 3. Mô tả giao tiếp Sync vs Async giữa services.

Trong kiến trúc Microservices của hệ thống Online Food Delivery, các luồng nghiệp vụ không bao giờ đứng độc lập mà là sự phối hợp chặt chẽ. Việc lựa chọn hình thức giao tiếp đóng vai trò cốt lõi đến hiệu năng.

##### 3.1. Giao tiếp đồng bộ (Synchronous / Request-Response)
- **Cơ chế:** Service gọi (Client) gửi một Request tới Service đích (Server) và **bắt buộc chờ (Block)** cho đến khi nhận được Response hoặc bị Timeout trễ hạn rồi mới tiếp tục chạy code của mình.
- **Công nghệ phổ biến:** RESTful API (HTTP/HTTPS), gRPC, GraphQL.
- **Áp dụng trong hệ thống "Online Food Delivery":**
    - **Kiểm tra giỏ hàng / Check giá món:** Khi người dùng mở ứng dụng xem giỏ đồ, `Ordering Service` sẽ gọi đồng bộ API (như REST hoặc gRPC) sang `Catalog & Restaurant Service` để **lấy cập nhật bảng giá chính xác tính đến hiện tại và tình trạng món (In-stock)** trước khi checkout. Giao dịch này cần dữ liệu tức thời và chính xác để hiển thị thông báo "Hết hàng".
    - **Xác thực User:** Tại tầng API Gateway, mỗi khi người dùng thao tác đặt đồ, Gateway sẽ call nội bộ sang `Identity & Access Service` để xác thực Access Token. Bắt buộc phải được kết quả là Token hợp lệ thì Gateway mới route Request đi tiếp.

##### 3.2. Giao tiếp bất đồng bộ (Asynchronous / Event-Driven)
- **Cơ chế:** Service gọi sẽ gửi một Message hoặc sinh ra Event chuyển vào một hệ thống Middleware trung gian (như Message Broker) và lập tức phản hồi về kết quả thành công cho người dùng ("Fire and Forget") **mà không cần chờ** Service đích hoàn tất tác vụ xử lý.
- **Công nghệ phổ biến:** Message Brokers (Apache Kafka, RabbitMQ, Amazon SQS, Pub/Sub).
- **Áp dụng trong hệ thống "Online Food Delivery":**
    - **Chu trình Đặt món cốt lõi (Core Order Flow):** Khi người dùng "Chốt đơn", `Ordering Service` ghi đơn vào Database với thái "Chờ thanh toán", đồng thời ném Event `OrderCreatedEvent` vào Kafka, rồi trả thẳng về cho App báo "Đặt đơn thành công". Ở phía sau, `Billing & Payment Service` bắt lấy sự kiện để thu tiền qua thẻ Stripe của khách. Nếu trừ thẻ xong, nó lại phát `PaymentSucceededEvent`. Ngay lúc này, `Delivery` lẫn `Notification Service` cùng bắt Event để phân công tài xế và gửi SMS cho khách. Rõ ràng, không ai phải đợi ai.
    - **Hệ thống Gửi thông báo:** Mỗi khi chuẩn bị đổi trạng thái đơn (Món đã nấu xong), chỉ cần thiết lập event `FoodReadyEvent`. `Communication Service` tự khắc bắt event và đi gửi Push Notification. Có hay không báo thì luồng mua bán vẫn chạy trơn tru, chẳng lo third-party SMS die làm treo giỏ đồ ăn.

##### 3.3. Bảng tóm tắt so sánh Sync và Async

| Tiêu chí | Giao tiếp Đồng bộ (Synchronous) | Giao tiếp Bất đồng bộ (Asynchronous) |
| :--- | :--- | :--- |
| **Bản chất mô hình** | Request - Response (Hỏi và đợi Trả Lời) | Event-Driven / Message-Based (Lửa rồi quên - Fire and Forget) |
| **Sự chặt chẽ kết nối (Coupling)** | **Cao (Tightly Coupled).** Service gọi phải biết rõ về địa chỉ IP/Endpoint của Service đích. | **Thấp (Loosely Coupled).** Publisher và Subscriber tách biệt, không hề biết gì về sự tồn tại của nhau (nhờ Broker đứng giữa). |
| **Tính khả dụng và chịu lỗi (Fault Tolerance)** | **Kém.** Dễ xảy ra hiệu ứng sập dây chuyền (Cascading Failure). Thanh toán die, gọi mãi không được thì Giỏ hàng cũng treo theo. | **Tốt.** Giao tiếp lỏng lẻo. Nếu gửi Notification (SMS) sập mạng, thì Message vẫn nằm xếp hàng gọn ở Kafka. Khi kết nối bình thường, Service lôi Messages đó ra xử lí tiếp mà không mất xíu Data nào. |
| **Độ trễ cho Người dùng UI (Latency)** | Nếu có Request chuỗi sâu (A gọi B, B gọi C, sau đó trả C về B về A), user sẽ mỏi cổ chờ load (Thời gian load tính bằng tổng tg xử lý từng bước). | Rất mượt về mặt UI. Các xử lý "Tác vụ tốn não" sẽ được đẩy xuống chờ xử lý ngầm trong Background Queue. |
| **Tính phức tạp về hệ thống** | Tương đối dễ tiếp cận, luồng Code chạy từ trên xuống dưới dễ Debug giống làm lập trình bình thường. | Phức tạp cao. Vất vả để xử lý tính toàn vẹn (Eventual Consistency), áp dụng phức tạp như Saga Pattern. Yêu cầu công cụ xịn để trace log Distributed / Retry message lỗi. |
| **Khi nào ưu tiên áp dụng?** | Các nhu cầu Lấy thông tin (READ/GET), query dữ liệu hay buộc kiểm tra tính hợp lệ tức thì trước vòng xoay tiếp theo. | Mọi thao tác Ghi/Cập nhật (WRITE/UPDATE process), hoặc chu trình quy trình rẽ ngánh, kéo dài mà không đòi hòi đáp ứng hiện ra hình liền cho User ngay lập tức. |

#### 4. Tạo event messaging design (Kafka/RabbitMQ, chỉ cần mô hình).

Dưới đây là các mô hình kiến trúc trực quan (vẽ bằng Draw.io) được đính kèm vào hệ thống:

##### 4.1. Service Map
Biểu diễn toàn cảnh kiến trúc Microservices gắn kết với các API Gateway, vùng Database tương ứng và luồng nối vào Message Broker.
![Service Map](./service%20map.drawio.png)

##### 4.2. Context Map (DDD)
Thể hiện các Bounded Context bên trong hệ thống và phân loại mối quan hệ giao tiếp giữa chúng.
![Context Map](./Context%20Map.drawio.png)

##### 4.3. Communication Diagram
Sơ đồ mô tả thực tế luồng mua hàng giao tiếp xuyên suốt qua các service (Kết hợp giữa Sync & Async).
![Communication Diagram](./Communication%20Diagram.drawio.png)
