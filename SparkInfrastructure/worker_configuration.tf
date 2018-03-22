# Setup Spark Worker 1
resource "null_resource" "spark-worker-1-configure" {

    # Trigger when the Spark Master has been configured
    triggers {
        spark_id = "${null_resource.spark-master-1-configure.id}"
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -y update",
            "sudo apt-get -y install default-jre",
            "wget http://www.mirrorservice.org/sites/ftp.apache.org/spark/spark-2.3.0/spark-2.3.0-bin-hadoop2.7.tgz",
            "tar -xzf spark-2.3.0-bin-hadoop2.7.tgz",
            "nohup spark-2.3.0-bin-hadoop2.7/sbin/start-slave.sh ${aws_instance.spark-master-1.public_dns}:7077> ~/spark-logs &",
            "sleep 10s",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.spark-worker-1.public_ip}"
        }
    }
}

# Setup Spark Worker 1
resource "null_resource" "spark-worker-2-configure" {

    # Trigger when the Spark Master has been configured
    triggers {
        spark_id = "${null_resource.spark-master-1-configure.id}"
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get -y update",
            "sudo apt-get -y install default-jre",
            "wget http://www.mirrorservice.org/sites/ftp.apache.org/spark/spark-2.3.0/spark-2.3.0-bin-hadoop2.7.tgz",
            "tar -xzf spark-2.3.0-bin-hadoop2.7.tgz",
            "nohup spark-2.3.0-bin-hadoop2.7/sbin/start-slave.sh ${aws_instance.spark-master-1.private_dns}:7077> ~/spark-logs &",
            "sleep 10s",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.spark-worker-2.public_ip}"
        }
    }
}