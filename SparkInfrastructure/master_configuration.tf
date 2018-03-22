# Setup Spark Master 1
resource "null_resource" "spark-master-1-configure" {

    # Trigger when the Spark Master has been built
    triggers {
        spark_id = "${aws_instance.spark-master-1.id}"
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get update -y",
            "sudo apt-get install default-jre -y",
            "sleep 5s",
            "wget http://www.mirrorservice.org/sites/ftp.apache.org/spark/spark-2.3.0/spark-2.3.0-bin-hadoop2.7.tgz",
            "tar -xzf spark-2.3.0-bin-hadoop2.7.tgz",
            "export SPARK_MASTER_HOST=\"${aws_instance.spark-master-1.public_dns}\"",
            "sleep 5s",
            "nohup spark-2.3.0-bin-hadoop2.7/sbin/start-master.sh > ~/spark-logs &",
            "sleep 10s",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.spark-master-1.public_ip}"
        }
    }
}