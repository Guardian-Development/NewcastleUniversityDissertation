# Kafka configuration file 
data "template_file" "kafka-config" {
    template = "${file("./kafka_configuration/kafka_server_properties.tpl")}"
    vars {
        kafka_public_ip = "${aws_eip.kafka-public-ip.public_ip}"
    }
}

# Setup Kafka with correct configuration
resource "null_resource" "kafka-configure" {

    # Trigger when Kafka instance changes
    triggers {
        kafka_id = "${aws_instance.kafka.id}"
    }

    provisioner "file" {
        content = "${data.template_file.kafka-config.rendered}"
        destination = "~/kafka_server_properties.txt"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.kafka_secret_key_file_path)}"
            host = "${aws_eip.kafka-public-ip.public_ip}"
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
            host = "${aws_eip.kafka-public-ip.public_ip}"
        }
    }
}