# Setup Flink Job Manager 1
resource "null_resource" "flink-job-manager-1-configure" {

    # Trigger when the first FLink Task Manager has completed setup
    triggers {
        kafka_id = "${null_resource.flink-task-manager-1-configure.id}"
    }

    # Load both parts of SSH key to Job Manager, with SSH configuration
    provisioner "file" {
        source = "${var.flink_rsa_public_key_file_path}"
        destination = "~/.ssh/id_rsa.pub"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.flink-job-manager-1.public_ip}"
        }
    }
    provisioner "file" {
        source = "${var.flink_rsa_private_key_file_path}"
        destination = "~/.ssh/id_rsa"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.flink-job-manager-1.public_ip}"
        }
    }
    provisioner "file" {
        content = "${data.template_file.flink-ssh-configuration.rendered}"
        destination = "~/.ssh/config"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.flink-job-manager-1.public_ip}"
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
            "chmod 700 ~/.ssh/id_rsa",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.flink-job-manager-1.public_ip}"
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
            host = "${aws_instance.flink-job-manager-1.public_ip}"
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
            host = "${aws_instance.flink-job-manager-1.public_ip}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sleep 10s",
            "nohup flink-1.4.1/bin/start-cluster.sh > ~/flink-logs &",
            "sleep 10s",
            "echo 'Running Apache Flink'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.flink-job-manager-1.public_ip}"
        }
    }
}