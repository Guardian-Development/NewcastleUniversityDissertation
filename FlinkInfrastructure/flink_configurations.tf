# Flink configuration file 
data "template_file" "flink-config" {
    template = "${file("./flink_configuration/flink_configuration.tpl")}"
    vars {
        job_manager = "${aws_instance.flink-job-manager-1.public_ip}"
    }
}

# Slaves configuration file 
data "template_file" "flink-slaves-config" {
    template = "${file("./flink_configuration/slaves_configuration.tpl")}"
    vars {
        slave_1 = "${aws_instance.flink-task-manager-1.public_ip}"
        slave_2 = "${aws_instance.flink-task-manager-2.public_ip}"
    }
}

# SSH configuration file 
data "template_file" "flink-ssh-configuration" {
    template = "${file("./flink_configuration/ssh_configuration.tpl")}"
}