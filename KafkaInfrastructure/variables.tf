# Amazon credentials
variable "access_key" {}
variable "secret_key" {}
variable "region" {
  default = "eu-west-1"
}

# Key pair name for SSH connectivity to EC2
variable "ec2_key_pair_name" {}
variable "ec2_secret_key_file_path" {}

# Kafka variables
variable "zookeeper-port" {
  default = "2181"
}
variable "kafka_port" {
  default = "9092"
}