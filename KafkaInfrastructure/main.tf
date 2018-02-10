# Use AWS for our cloud provider
provider "aws" {
  access_key = "${var.access_key}"
  secret_key = "${var.secret_key}"
  region     = "${var.region}" 
}

resource "aws_instance" "web" {
    # 16.04 LTS, amd64, hvm:ebs-ssd 
    ami = "ami-c1167eb8"
    instance_type = "t2.micro"

    tags {
        Name = "Kafka"
    }
}

