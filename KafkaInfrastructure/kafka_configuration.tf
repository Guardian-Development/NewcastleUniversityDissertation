# Setup Kafka Broker 2
data "template_file" "kafka-broker-1-config" {
    template = "${file("./kafka_configuration/kafka_server_properties.tpl")}"
    vars {
        broker_id = "0"
        kafka_public_ip = "${aws_instance.kafka-broker-1.public_ip}"
        kafka_port = "${var.kafka_port}"
        zookeeper_connection_url = "${aws_instance.zookeeper.public_ip}:${var.zookeeper-port}"
    }
}

# Setup Kafka Broker 1 machine
resource "null_resource" "kafka-broker-1-configure" {

    # Trigger when Kafka instance changes
    triggers {
        kafka_id = "${null_resource.zookeeper-configure.id}"
    }

    provisioner "file" {
        content = "${data.template_file.kafka-broker-1-config.rendered}"
        destination = "~/kafka_server_properties.txt"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.kafka-broker-1.public_ip}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -q -y update",
            "sudo apt-get -q -y install default-jre",
            "wget http://mirrors.ukfast.co.uk/sites/ftp.apache.org/kafka/1.0.0/kafka_2.11-1.0.0.tgz",
            "tar -xzf kafka_2.11-1.0.0.tgz",
            "export KAFKA_HEAP_OPTS=\"-Xmx500M -Xmx500M\"",
            "echo 'Starting Kafka'",
            "nohup kafka_2.11-1.0.0/bin/kafka-server-start.sh ~/kafka_server_properties.txt > ~/kafka-logs &",
            "sleep 10s",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.kafka-broker-1.public_ip}"
        }
    }
}

# Setup Kafka Broker 2
data "template_file" "kafka-broker-2-config" {
    template = "${file("./kafka_configuration/kafka_server_properties.tpl")}"
    vars {
        broker_id = "1"
        kafka_public_ip = "${aws_instance.kafka-broker-2.public_ip}"
        kafka_port = "${var.kafka_port}"
        zookeeper_connection_url = "${aws_instance.zookeeper.public_ip}:2181"
    }
}

# Setup Kafka Broker 2 machine
resource "null_resource" "kafka-broker-2-configure" {

    # Trigger when Kafka instance changes
    triggers {
        kafka_id = "${null_resource.zookeeper-configure.id}"
    }

    provisioner "file" {
        content = "${data.template_file.kafka-broker-2-config.rendered}"
        destination = "~/kafka_server_properties.txt"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.kafka-broker-2.public_ip}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -q -y update",
            "sudo apt-get -q -y install default-jre",
            "wget http://mirrors.ukfast.co.uk/sites/ftp.apache.org/kafka/1.0.0/kafka_2.11-1.0.0.tgz",
            "tar -xzf kafka_2.11-1.0.0.tgz",
            "export KAFKA_HEAP_OPTS=\"-Xmx500M -Xmx500M\"",
            "echo 'Starting Kafka'",
            "nohup kafka_2.11-1.0.0/bin/kafka-server-start.sh ~/kafka_server_properties.txt > ~/kafka-logs &",
            "sleep 10s",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.kafka-broker-2.public_ip}"
        }
    }
}