# Newcastle University Dissertation
My third year dissertation project.

# Projects 
## TerraformInfrastructure
Contains the AWS configuration to be used in production. 

## CI
Contains the Continous Integration setup and tools to be used. (Jenkins)

## ClientService
The microservice to be used when serving the client to the users.

# Deploying a new Scala service
1. sbt docker:publishLocal
2. docker login
3. docker tag image-tag docker-username/image-name
4. docker push docker-username/image-name
5. update the terraform variables to have the new version number and docker image name. 
6. terraform plan (params)
7. terraform apply (params)

Example:
- docker tag clientservice:1-0-snapshot theguardian94/clientservice:1-0-snapshot
- docker push theguardian94/clientservice:1-0-snapshot

You need to tag the image with the name corresponding to your docker account, and give it the image name you wish to upload it as. 

#### Your image name and version number should only contain (A-Z, 0-9, -)

