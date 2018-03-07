# Output the public IP of the EC2 instances runningFLink
output "flink-job-manager-1-public-ip" {
  value  = "${aws_instance.flink-job-manager-1.public_ip}"
}

output "flink-task-manager-1-public-ip" {
  value  = "${aws_instance.flink-task-manager-1.public_ip}"
}

output "flink-task-manager-2-public-ip" {
  value  = "${aws_instance.flink-task-manager-2.public_ip}"
}