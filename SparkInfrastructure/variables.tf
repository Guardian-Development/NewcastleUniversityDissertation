# Amazon credentials
variable "access_key" {}
variable "secret_key" {}
variable "region" {
  default = "eu-west-1"
}

# Key pair name for SSH connectivity to EC2
variable "ec2_key_pair_name" {}
variable "ec2_secret_key_file_path" {}

# Key pair that will be used for Spark instances to communicate
variable "spark_rsa_private_key_file_path" {}
variable "spark_rsa_public_key_file_path" {}