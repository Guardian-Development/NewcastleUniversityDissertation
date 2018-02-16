# Kafka Broker 1
resource "aws_instance" "kafka-broker-1" {
    # 16.04 LTS, amd64, hvm:ebs-ssd 
    ami = "ami-c1167eb8"
    instance_type = "t2.micro"
    vpc_security_group_ids = [
        "${aws_security_group.ec2-ssh.id}",
        "${aws_security_group.ec2-incoming-traffic.id}", 
        "${aws_security_group.ec2-outgoing-traffic.id}"]

    key_name = "${var.ec2_key_pair_name}"

    tags {
        Name = "Kafka-Broker-1"
    }
}

# Kafka Broker 2
resource "aws_instance" "kafka-broker-2" {
    # 16.04 LTS, amd64, hvm:ebs-ssd 
    ami = "ami-c1167eb8"
    instance_type = "t2.micro"
    vpc_security_group_ids = [
        "${aws_security_group.ec2-ssh.id}",
        "${aws_security_group.ec2-incoming-traffic.id}", 
        "${aws_security_group.ec2-outgoing-traffic.id}"]

    key_name = "${var.ec2_key_pair_name}"

    tags {
        Name = "Kafka-Broker-2"
    }
}

# Public IP of Kafka Broker 1 EC2 instance
resource "aws_eip" "kafka-broker-1-public-ip" {
  instance = "${aws_instance.kafka-broker-1.id}"
}

# Public IP of Kafka Broker 2 EC2 instance
resource "aws_eip" "kafka-broker-2-public-ip" {
    instance = "${aws_instance.kafka-broker-2.id}"
}