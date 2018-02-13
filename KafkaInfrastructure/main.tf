# Use AWS for our cloud provider
provider "aws" {
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
  region     = "${var.region}" 
}

# Kafka machine
resource "aws_instance" "kafka" {
    # 16.04 LTS, amd64, hvm:ebs-ssd 
    ami = "ami-c1167eb8"
    instance_type = "t2.micro"
    vpc_security_group_ids = [
        "${aws_security_group.kafka-ssh.id}",
        "${aws_security_group.kafka-incoming-traffic.id}", 
        "${aws_security_group.kafka-outgoing-traffic.id}"]

    key_name = "${var.kafka_key_pair_name}"

    tags {
        Name = "Kafka-Instance"
    }
}

# Public IP of Kafka EC2 instance
resource "aws_eip" "kafka-public-ip" {
  instance = "${aws_instance.kafka.id}"
}

