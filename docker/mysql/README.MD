<!-- Stop and remove existing container -->
docker compose down

<!-- List existing volume -->
docker volume ls

<!-- Remove mounted volum if needed -->
docker volume rm mysql_mysql84-data

<!-- Start the mysql:8.4 -->
docker compose up -d

<!-- Check is Table case incensitive, lower_case_table_names=1 -->
docker exec -it mysql84 mysql -uroot -p -e "SHOW VARIABLES LIKE 'lower_case_table_names';"