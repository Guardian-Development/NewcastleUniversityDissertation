# Spark Insfrastructure

The production infrastructure required to run Apache Spark publicly on AWS. Sets up a 3 node Spark cluster (1 node master, 2 nodes workers)

## Requirements

- Terraform must be installed and on the PATH of the cmd
    - https://www.terraform.io/downloads.html
- You must have created a key pair for SSH connectivity to the ec2 instances
    - https://console.aws.amazon.com/ec2/
    - You must store the private key locally on the machine for Terraform to use it to configure the ec2 instances.

## To Run

1. You need to create an RSA key that can be used by the machines within the spark cluster to communicate with passwordless SSH. 
    - On your local machine run: ssh-keygen -t rsa -P ""
    - Take note of where the 2 key files were generated: id_rsa (private key) and id_rsa.pub (public key)
    - Use the full path names including the file name for the variables: spark_rsa_private_key_file_path, spark_rsa_public_key_file_path respectively
2. Run: terraform init
3. Create a secret.tfvars file in the current directory. This should include any variables needed, see variables.tf for what is expected. (format: key = "key_value")
4. In the current directory: terraform plan (params) -var-file="secret.tfvars"
5. terraform apply (params) -var-file="secret.tfvars"

The output parameter of the Job Manager gives you the IP to access the Spark UI, This should be done on port 8080. 

## To Destroy

1. In the current directory: terraform plan -destroy (params) -var-file="secret.tfvars"
2. terraform destroy (params) -var-file="secret.tfvars"