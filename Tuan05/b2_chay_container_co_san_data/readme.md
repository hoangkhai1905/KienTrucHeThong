docker-compose up -d

Xem data
docker exec -it my_postgres_init psql -U myuser -d baitap_db -c "SELECT * FROM students;"

Insert data
docker exec -it my_postgres_init psql -U myuser -d baitap_db -c "INSERT INTO students (name, email) VALUES ('John Doe', 'john.doe@example.com');"

Dump data
docker exec -t my_postgres_init pg_dump -U myuser -d baitap_db --clean --if-exists > init.sql