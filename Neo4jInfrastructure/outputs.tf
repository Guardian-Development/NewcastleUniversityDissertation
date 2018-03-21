# Output the public IP of the EC2 instances runningFLink
output "neo4j-instance-1-public-ip" {
  value  = "${aws_instance.neo4j-instance-1.public_ip}"
}