# Kafka Insfrastructure

The production infrastructure required to run Apache Kafka publicly on AWS.

## Requirements 
- Terraform must be installed and on the PATH of the cmd
    - https://www.terraform.io/downloads.html

## To Run 
1. Create a secret.tfvars file in the current directory. This should include youe access_key and secret_key for AWS. (format: key = "key_value")
1. In the current directory: terraform plan (params) -var-file="secret.tfvars"
2. terraform apply (params) -var-file="secret.tfvars"

## To Destroy
1. In the current directory: terraform plan -destroy (params) -var-file="secret.tfvars"
2. terraform destroy (params) -var-file="secret.tfvars"