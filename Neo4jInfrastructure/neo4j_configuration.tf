resource "null_resource" "neo4j-instance-1-configure" {

    # Trigger when Neo4j builds
    triggers {
        kafka_id = "${aws_instance.neo4j-instance-1.id}"
    }

    provisioner "file" {
        source = "./neo4j_configuration/neo4j.conf"
        destination = "~/neo4j.conf"
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.neo4j-instance-1.public_ip}"
        }
    }

    provisioner "remote-exec" {
        inline = [
            "sudo apt-get update -y",
            "sudo apt-get install default-jre -y",
            "wget --no-check-certificate -O - https://debian.neo4j.org/neotechnology.gpg.key | sudo apt-key add -",
            "echo 'deb http://debian.neo4j.org/repo stable/' | sudo tee /etc/apt/sources.list.d/neo4j.list",
            "sudo apt update -y",
            "sudo apt install neo4j -y",
            "sudo rm /etc/neo4j/neo4j.conf",
            "sudo mv ~/neo4j.conf /etc/neo4j/neo4j.conf",
            "sudo service neo4j start",
            "sleep 10s",
            "echo 'Complete Setup'"
        ]
        
        connection {
            type = "ssh"
            timeout = "5m"
            user = "ubuntu"
            private_key = "${file(var.ec2_secret_key_file_path)}"
            host = "${aws_instance.neo4j-instance-1.public_ip}"
        }
    }
}