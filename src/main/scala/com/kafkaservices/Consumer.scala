package com.kafkaservices

import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{ State, StateSpec }
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

class Consumer extends Thread {

  override def run() {

    //Creating the SparkStreaming Context with 10 Sec Consumption Delay
    val conf = new SparkConf().setMaster("local[*]").setAppName("Stream_Counter")
    val ssc = new StreamingContext(conf, Seconds(5))

    //set the Kafka Parameters
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use2",
      "auto.offset.reset" -> "earliest",
      "enable.auto.commit" -> (false: java.lang.Boolean))
    val topics = Array("kafka-employee") //topics list

    //Consuming Kafka Messages
    val kafkaStream = KafkaUtils.createDirectStream[String, String](ssc,PreferConsistent,Subscribe[String, String](topics, kafkaParams))
    val splits = kafkaStream.map(record => (record.key(), record.value.toString)).map(x => Context.count += 1)
    splits.print()			//print the consumed messages as sparkRDD
    ssc.start()
    ssc.awaitTermination()

  }

}
