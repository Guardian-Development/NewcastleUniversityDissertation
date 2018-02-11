# Kafka Insfrastructure

The production infrastructure required to run Apache Kafka publicly on AWS.

## Requirements 
- Terraform must be installed and on the PATH of the cmd
    - https://www.terraform.io/downloads.html
- You must have created a key pair for SSH connectivity to the ec2 instance
    - https://console.aws.amazon.com/ec2/
    - You must store the private ket locally on the machine for Terraform to use it to configure the ec2 instances.

## To Run 
1. Create a secret.tfvars file in the current directory. This should include youe access_key and secret_key for AWS. (format: key = "key_value")
1. In the current directory: terraform plan (params) -var-file="secret.tfvars"
2. terraform apply (params) -var-file="secret.tfvars"

## To Destroy
1. In the current directory: terraform plan -destroy (params) -var-file="secret.tfvars"
2. terraform destroy (params) -var-file="secret.tfvars"