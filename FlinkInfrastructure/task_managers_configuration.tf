# Setup Flink Task Manager 1
resource "null_resource" "flink-task-manager-1-configure" {

    # Trigger when Task Manager instance changes
    triggers {
        kafka_id = "${aws_instance.flink-task-manager-1.id}"
    }

    provisioner "file" {
        source = "${var.flink_rsa_public_key_file_path}"
        destination = "~/.ssh/id_rsa.pub"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-1-public-ip.public_ip}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -q -y update",
            "sudo apt-get -q -y install default-jre",
            "sudo apt-get install openssh-server openssh-client",
            "wget http://apache.mirror.anlx.net/flink/flink-1.4.1/flink-1.4.1-bin-scala_2.11.tgz",
            "tar -xzf flink-1.4.1-bin-scala_2.11.tgz",
            "rm flink-1.4.1/conf/flink-conf.yaml",
            "rm flink-1.4.1/conf/slaves",
            "chmod 700 ~/.ssh/",
            "chmod 644 ~/.ssh/authorized_keys",
            "cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-1-public-ip.public_ip}"
        }
    }

    provisioner "file" {
        content = "${data.template_file.flink-config.rendered}"
        destination = "~/flink-1.4.1/conf/flink-conf.yaml"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-1-public-ip.public_ip}"
        }
    }

     provisioner "file" {
        content = "${data.template_file.flink-slaves-config.rendered}"
        destination = "~/flink-1.4.1/conf/slaves"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-1-public-ip.public_ip}"
        }
    }
}

# Setup Flink Task Manager 2
resource "null_resource" "flink-task-manager-2-configure" {

    # Trigger when Task Manager instance changes
    triggers {
        kafka_id = "${aws_instance.flink-task-manager-2.id}"
    }

    provisioner "file" {
        source = "${var.flink_rsa_public_key_file_path}"
        destination = "~/.ssh/id_rsa.pub"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-2-public-ip.public_ip}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -q -y update",
            "sudo apt-get -q -y install default-jre",
            "sudo apt-get install openssh-server openssh-client",
            "wget http://apache.mirror.anlx.net/flink/flink-1.4.1/flink-1.4.1-bin-scala_2.11.tgz",
            "tar -xzf flink-1.4.1-bin-scala_2.11.tgz",
            "rm flink-1.4.1/conf/flink-conf.yaml",
            "rm flink-1.4.1/conf/slaves",
            "chmod 700 ~/.ssh/",
            "chmod 644 ~/.ssh/authorized_keys",
            "cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-2-public-ip.public_ip}"
        }
    }

    provisioner "file" {
        content = "${data.template_file.flink-config.rendered}"
        destination = "~/flink-1.4.1/conf/flink-conf.yaml"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-2-public-ip.public_ip}"
        }
    }

     provisioner "file" {
        content = "${data.template_file.flink-slaves-config.rendered}"
        destination = "~/flink-1.4.1/conf/slaves"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_eip.flink-task-manager-2-public-ip.public_ip}"
        }
    }
}