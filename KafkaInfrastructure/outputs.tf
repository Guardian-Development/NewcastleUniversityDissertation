# Output the public IP of the EC2 instance running Kafka
output "kafka-public-ip" {
  value  = "${aws_eip.kafka-public-ip.public_ip}"
}