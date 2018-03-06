# Flink JobManager 1
resource "aws_instance" "flink-job-manager-1" {
    # 16.04 LTS, amd64, hvm:ebs-ssd 
    ami = "ami-c1167eb8"
    instance_type = "t2.micro"
    vpc_security_group_ids = [
        "${aws_security_group.ec2-ssh.id}",
        "${aws_security_group.ec2-incoming-traffic.id}", 
        "${aws_security_group.ec2-outgoing-traffic.id}"]

    key_name = "${var.ec2_key_pair_name}"

    tags {
        Name = "Flink-Job-Manager-1"
    }
}

# Flink TaskManager 1
resource "aws_instance" "flink-task-manager-1" {
    # 16.04 LTS, amd64, hvm:ebs-ssd 
    ami = "ami-c1167eb8"
    instance_type = "t2.micro"
    vpc_security_group_ids = [
        "${aws_security_group.ec2-ssh.id}",
        "${aws_security_group.ec2-incoming-traffic.id}", 
        "${aws_security_group.ec2-outgoing-traffic.id}"]

    key_name = "${var.ec2_key_pair_name}"

    tags {
        Name = "Flink-Task-Manager-1"
    }
}

# Public IP of Flink JobManager 1 EC2 instance
resource "aws_eip" "flink-job-manager-1-public-ip" {
  instance = "${aws_instance.flink-job-manager-1.id}"
}

# Public IP of Flink TaskManager 1 EC2 instance
resource "aws_eip" "flink-task-manager-1-public-ip" {
    instance = "${aws_instance.flink-task-manager-1.id}"
}

# Output the public IP of the EC2 instances runningFLink
output "flink-job-manager-1-public-ip" {
  value  = "${aws_eip.flink-job-manager-1-public-ip.public_ip}"
}
output "flink-task-manager-1-public-ip" {
  value  = "${aws_eip.flink-task-manager-1-public-ip.public_ip}"
}