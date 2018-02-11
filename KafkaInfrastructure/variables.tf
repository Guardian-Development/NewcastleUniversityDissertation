# Amazon credentials
variable "access_key" {}
variable "secret_key" {}
variable "region" {
  default = "eu-west-1"
}

# Key pair name for SSH connectivity to Kafka
variable "kafka_key_pair_name" {}
variable "kafka_secret_key_file_path" {}