# Security group to allow SSH onto a machine 
resource "aws_security_group" "ec2-ssh" {
    name = "Spark SSH Group"

    # Allow port 22 (ssh) connections from any address
    ingress {
        from_port = 22 
        to_port = 22 
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags {
        Name = "Spark-SSH-Group"
    }
}

# Security group to allow all outgoing network traffic
resource "aws_security_group" "ec2-outgoing-traffic" {
    name = "Spark All Outgoing Group"

    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags {
        Name = "Spark-All-Outgoing-Group"
    }
}

# Security group to allow all incoming network traffic
resource "aws_security_group" "ec2-incoming-traffic" {
    name = "Spark All Incoming Group"

    ingress {
        from_port = 0 
        to_port = 0 
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags {
        Name = "Spark-All-Incoming-Group"
    }
}