# Flink Infrastructure

The production infrastructure required to run Apache Flink publicly on AWS. 

## Requirements

- Terraform must be installed and on the PATH of the cmd
    - https://www.terraform.io/downloads.html
- You must have created a key pair for SSH connectivity to the ec2 instances
    - https://console.aws.amazon.com/ec2/
    - You must store the private key locally on the machine for Terraform to use it to configure the ec2 instances.

## To Run

1. You need to create an RSA key that can be used by the machines within the flink cluster to communicate with passwordless SSH. 
    - On your local machine run: ssh-keygen -t rsa -P ""
    - Take note of where the 2 key files were generated: id_rsa (private key) and id_rsa.pub (public key)
    - Use the full path names including the file name for the variables: flink_rsa_private_key_file_path, flink_rsa_public_key_file_path respectively
2. Run: terraform init
3. Create a secret.tfvars file in the current directory. This should include any variables needed, see variables.tf for what is expected. (format: key = "key_value")
4. In the current directory: terraform plan (params) -var-file="secret.tfvars"
5. terraform apply (params) -var-file="secret.tfvars"

The output parameter of the Job Manager gives you the IP to access the Flink UI, This should be done on port 8081. 

## To Destroy

1. In the current directory: terraform plan -destroy (params) -var-file="secret.tfvars"
2. terraform destroy (params) -var-file="secret.tfvars"

## Running a Job on the Cluster

To add a Job to run on the Flink cluster you have 2 options. 

1. Use the UI and go to the submit new job page and upload you're jar.
2. Use the command line interface to upload and run the task.

### Using the CLI to run a Job

1. Open a terminal and go to your Scala project directory.
2. Run: sbt clean assembly. This will generate a fat jar (output will show file location) that will allow you to run the application. 
3. Download Flink locally:  http://mirror.ox.ac.uk/sites/rsync.apache.org/flink/flink-1.4.1/flink-1.4.1-bin-scala_2.11.tgz 
4. Unzip FLink: tar -xzf flink-1.4.1-bin-scala_2.11.tgz
5. Run the command: flink-1.4.1/bin/flink run --jobmanager job-manager-ip:6123 jarfilepath (any extra command line arguments for your jar)
6. This will run the project on the Flink cluster.