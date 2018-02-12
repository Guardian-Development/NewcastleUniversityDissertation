# Use AWS for our cloud provider
provider "aws" {
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
  region     = "${var.region}" 
}

# Security group to allow SSH onto a machine 
resource "aws_security_group" "kafka-ssh" {
    name = "Kafka SSH Group"

    # Allow port 22 (ssh) connections from any address
    ingress {
        from_port = 22 
        to_port = 22 
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags {
        Name = "Kafka-SSH-Group"
    }
}

# Security group to allow all outgoing network traffic
resource "aws_security_group" "kafka-outgoing-traffic" {
    name = "Kafka All Outgoing Group"

    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags {
        Name = "Kafka-All-Outgoing-Group"
    }
}

# Security group to allow all incoming network traffic
resource "aws_security_group" "kafka-incoming-traffic" {
    name = "Kafka All Incoming Group"

    ingress {
        from_port = 0 
        to_port = 0 
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags {
        Name = "Kafka-All-Incoming-Group"
    }
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

    # for this to work we need the IP of the box as the advertised.listeners=PLAINTEXT://IP:9092
    # find a way to interpolate and start with that IP
    provisioner "file" {
        source = "./kafka_configuration/kafka_server_properties.txt"
        destination = "~/kafka_server_properties.txt"

        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.kafka_secret_key_file_path)}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -q -y update",
            "sudo apt-get -q -y install default-jre",
            "wget http://mirrors.ukfast.co.uk/sites/ftp.apache.org/kafka/1.0.0/kafka_2.11-1.0.0.tgz",
            "tar -xzf kafka_2.11-1.0.0.tgz",
            "export KAFKA_HEAP_OPTS=\"-Xmx200M -Xmx200M\"",
            "echo 'Starting Zookeeper'",
            "nohup kafka_2.11-1.0.0/bin/zookeeper-server-start.sh kafka_2.11-1.0.0/config/zookeeper.properties > ~/zookeeper-logs &",
            "sleep 5s",
            "echo 'Starting Kafka'",
            "nohup kafka_2.11-1.0.0/bin/kafka-server-start.sh ~/kafka_server_properties.txt > ~/kafka-logs &",
            "sleep 5s",
            "echo 'Complete Setup'"
        ]

        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.kafka_secret_key_file_path)}"
        }
    }
}

# Output the public IP of the ec2 instance running Kafka
resource "aws_eip" "kafka-ip" {
  instance = "${aws_instance.kafka.id}"
}
output "ip" {
  value  = "${aws_eip.kafka-ip.public_ip}"
}

