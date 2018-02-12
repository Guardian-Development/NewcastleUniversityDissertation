# Use AWS for our cloud provider
provider "aws" {
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
  region     = "${var.region}" 
}

# Security group to allow SSH onto a machine 
resource "aws_security_group" "kafka-security-group" {
    name = "Kafka Security Group"

    # Allow port 22 (ssh) connections from any address
    ingress {
        from_port = 22 
        to_port = 22 
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    # Allow outgoing network traffic
    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags {
        Name = "Kafka-Security-Group"
    }
}

resource "aws_instance" "kafka" {
    # 16.04 LTS, amd64, hvm:ebs-ssd 
    ami = "ami-c1167eb8"
    instance_type = "t2.micro"
    vpc_security_group_ids = ["${aws_security_group.kafka-security-group.id}"]
    key_name = "${var.kafka_key_pair_name}"

    tags {
        Name = "Kafka-Instance"
    }

    provisioner "remote-exec"{
        inline = [
            "sudo apt-get -q -y update",
            "sudo apt-get -q -y install default-jre",
            "wget http://mirrors.ukfast.co.uk/sites/ftp.apache.org/kafka/1.0.0/kafka_2.11-1.0.0.tgz",
            "tar -xzf kafka_2.11-1.0.0.tgz",
            "cd kafka_2.11-1.0.0/",
            # "export KAFKA_HEAP_OPTS=\"-Xmx500M -Xmx500M\"",
            # "source ~\\.bashrc",
            "bin/zookeeper-server-start.sh config/zookeeper.properties &",
            "bin/kafka-server-start.sh config/server.properties &"
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

