# Zookeeper configuration file 
data "template_file" "zookeeper-config" {
    template = "${file("./zookeeper_configuration/zookeeper_server_properties.tpl")}"
    vars {
        client_port = "${var.zookeeper-port}"
    }
}

# Setup Zookeeper
resource "null_resource" "zookeeper-configure" {

    # Trigger when Zookeeper instance changes
    triggers {
        kafka_id = "${aws_instance.zookeeper.id}"
    }

    provisioner "file" {
        content = "${data.template_file.zookeeper-config.rendered}"
        destination = "~/zookeeper_server_properties.txt"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.zookeeper-public-ip.public_ip}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -q -y update",
            "sudo apt-get -q -y install default-jre",
            "wget http://mirrors.ukfast.co.uk/sites/ftp.apache.org/kafka/1.0.0/kafka_2.11-1.0.0.tgz",
            "tar -xzf kafka_2.11-1.0.0.tgz",
            "echo 'Starting Zookeeper'",
            "nohup kafka_2.11-1.0.0/bin/zookeeper-server-start.sh ~/zookeeper_server_properties.txt > ~/zookeeper-logs &",
            "sleep 5s",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.zookeeper-public-ip.public_ip}"
        }
    }
}

