# Neo4j Infrastructure

The production infrastructure required to run Neo4j database publicly on AWS. 

## Requirements

- Terraform must be installed and on the PATH of the cmd
    - https://www.terraform.io/downloads.html
- You must have created a key pair for SSH connectivity to the ec2 instances
    - https://console.aws.amazon.com/ec2/
    - You must store the private key locally on the machine for Terraform to use it to configure the ec2 instances.

## To Run

1. Run: terraform init
2. Create a secret.tfvars file in the current directory. This should include any variables needed, see variables.tf for what is expected. (format: key = "key_value")
3. In the current directory: terraform plan (params) -var-file="secret.tfvars"
4. terraform apply (params) -var-file="secret.tfvars"

The output parameter of the Neo4j instance gives you the IP to access the Neo4j UI, This should be done on port 7474. 

## To Destroy

1. In the current directory: terraform plan -destroy (params) -var-file="secret.tfvars"
2. terraform destroy (params) -var-file="secret.tfvars"