# Activity Analysis Infrastructure

The production infrastructure required to run Apache Flink with the activityanalysisservice tasks publicly on AWS. 

## Requirements

- Terraform must be installed and on the PATH of the cmd
    - https://www.terraform.io/downloads.html
- You must have created a key pair for SSH connectivity to the ec2 instances
    - https://console.aws.amazon.com/ec2/
    - You must store the private key locally on the machine for Terraform to use it to configure the ec2 instances.


## Steps Executed 

(Both Machines)
sudo apt-get -q -y update
sudo apt-get -q -y install default-jre
sudo apt-get install openssh-server openssh-client
wget http://apache.mirror.anlx.net/flink/flink-1.4.1/flink-1.4.1-bin-scala_2.11.tgz
tar -xzf flink-1.4.1-bin-scala_2.11.tgz
nano flink-1.4.1/conf/flink-conf.yaml
    Add this line: jobmanager.rpc.address: Job Machine IP
    HEAP SIZE JAVA

nano flink-1.4.1/conf/slaves 
add Task Manager IP

(Local Machine)
//generate key pair for the machines to talk
ssh-keygen -t rsa -P "" 

//upload key to machines
scp -i flink-key-pair.pem /home/joe/.ssh/id_rsa.pub ubuntu@JOB-IP:~/.ssh/id_rsa.pub
scp -i flink-key-pair.pem /home/joe/.ssh/id_rsa ubuntu@JOB-IP:~/.ssh/id_rsa
scp -i flink-key-pair.pem /home/joe/.ssh/id_rsa.pub ubuntu@TASK-IP:~/.ssh/id_rsa.pub

(Task Machine)
//on task machine, make authorised key
chmod 700 ~/.ssh/
chmod 644 authorized_keys
cat id_rsa.pub >> authorized_keys

(Job Machine)
//set correct permission on job machine to use the key
chmod 700 id_rsa
flink-1.4.1/bin/start-cluster.sh

# TOD0
find out why start cluster doesnt work remotly 
Potentially need to add SSH config to task managers
works when manually call
cleanup readme with instructions
add the running of a task in the cluster