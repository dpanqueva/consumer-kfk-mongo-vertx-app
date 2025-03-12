docker run --name mongodb-container \
-d \
-p 27017:27017 \
-v mongodata:/data/db \
-e MONGO_INITDB_ROOT_USERNAME=admin \
-e MONGO_INITDB_ROOT_PASSWORD=password \
mongo


mongosh "mongodb://admin:password@localhost:27017/"
