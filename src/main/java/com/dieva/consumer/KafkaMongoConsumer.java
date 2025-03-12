package com.dieva.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.Map;

public class KafkaMongoConsumer extends AbstractVerticle {

    @Override
    public void start() {
        Map<String, String> kafkaConfig = new HashMap<>();
        kafkaConfig.put("bootstrap.servers", "localhost:9092");
        kafkaConfig.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConfig.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConfig.put("group.id", "profession-group");
        kafkaConfig.put("auto.offset.reset", "earliest");
        kafkaConfig.put("enable.auto.commit", "true");

        KafkaConsumer<String, Object> consumer = KafkaConsumer.create(vertx, kafkaConfig);

        JsonObject mongoConfig = new JsonObject();
        mongoConfig.put("connection_string", "mongodb://profession:profession@localhost:27017");
        mongoConfig.put("db_name", "profession");

        MongoClient mongoClient = MongoClient.createShared(vertx, mongoConfig);

        /**
         * Subscribe to the topic profession
         * */
        consumer.subscribe("profession", ar -> {
            if (ar.succeeded()) {
                System.out.println("Consumer subscribed");
            } else {
                String error = ar.cause().getMessage();
                System.out.println("Error while subscribing consumer".concat(" ").concat(error));
            }
        });

        /**
         * Consume the messages from the topic profession
         * */
        consumer.handler(record -> {
            System.out.println("Consumed message: " + record.value());
            JsonObject professionDocument = new JsonObject()
                    .put("profession", record.value());

            /**
             * Save to MongoDB
             * */
            mongoClient.save("profession", professionDocument, res -> {
                if (res.succeeded()) {
                    System.out.println("Saved to MongoDB");
                } else {
                    String error = res.cause().getMessage();
                    System.out.println("Error while saving to MongoDB".concat(" ").concat(error));
                }

            });
        });
    }
}
