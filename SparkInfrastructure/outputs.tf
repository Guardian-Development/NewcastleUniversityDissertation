# Output the public IP of the EC2 instances running Spark
output "spark-master-1-public-ip" {
  value  = "${aws_instance.spark-master-1.public_ip}"
}

output "spark-worker-1-public-ip" {
  value  = "${aws_instance.spark-worker-1.public_ip}"
}

output "spark-worker-2-public-ip" {
  value  = "${aws_instance.spark-worker-2.public_ip}"
}