package com.dieva;

import com.dieva.consumer.KafkaMongoConsumer;
import io.vertx.core.Vertx;

/**
 * Hello world!
 */
public class MainVerticleConsumer {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new KafkaMongoConsumer());

    }
}
