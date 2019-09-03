package com.kafkaservices

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.kafkaservices.EmployeeController.Employee
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}

import scala.concurrent.Future

class Producer {

  case class KafkaProducerConfigs(brokerList: String = "127.0.0.1:9092") {
    val properties = new Properties()
    properties.put("bootstrap.servers", brokerList)
    properties.put("key.serializer", classOf[StringSerializer])
    properties.put("value.serializer", classOf[StringSerializer])
    //    properties.put("serializer.class", classOf[StringDeserializer])
    //    properties.put("batch.size", 16384)
    //    properties.put("linger.ms", 1)
    //    properties.put("buffer.memory", 33554432)
  }

  val producer = new KafkaProducer[String, String](KafkaProducerConfigs().properties)

  import scala.concurrent.ExecutionContext.Implicits._

  /**
    * Publish the Message to Topic
    * @param message
    */
  def produce(message: Employee ): Future[Int] =  Future {

    val topic = "kafka-employee"
    producer.send(new ProducerRecord[String, String](topic, message.toString))

    producer.close(100L, TimeUnit.MILLISECONDS)
    Context.count
  }

}
object Context  {
  var count = 0
}
