# Terraform Infrastructure
The infrastructure required to run the application in production on AWS.

![alt text](https://github.com/Guardian-Development/NewcastleUniversityDissertation/blob/master/TerraformInfrastructure/Diagram/InfrastructureDiagram.png)

# Requirements 
- Terraform must be installed and on the PATH on the cmd
- You must specify the parameters:
    - access_key (AWS)
    - secret_key (AWS)
    - application_name
    - application_version (A-Z, 0-9, -)
    - docker_image_source
    - docker_application_image_name

# To Run
1. In the current directory: terraform plan (params)
2. terraform apply (params)

You now have the infrastructure specified on AWS.

## To Destroy
1. In the current directory: terraform plan -destroy (params)
2. terraform destroy (params)

