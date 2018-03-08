# Output the public IP of the EC2 instances running Kafka
output "kafka-broker-1-public-ip" {
  value  = "${aws_instance.kafka-broker-1.public_ip}"
}
output "kafka-broker-2-public-ip" {
  value  = "${aws_instance.kafka-broker-2.public_ip}"
}

# Output the public IP of the EC2 instance running Zookeeper
output "zookeeper-public-ip" {
  value = "${aws_instance.zookeeper.public_ip}"
}