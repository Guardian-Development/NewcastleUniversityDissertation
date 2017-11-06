# Cluster
resource "aws_ecs_cluster" "application-cluster" {
    name = "${var.application_name}-cluster"
}

resource "aws_launch_configuration" "ecs_cluster_launch" {
    name = "${var.application_name}-cluster"
    instance_type = "t2.micro"
    image_id = "${lookup(var.amis-ecs, var.region)}"
    iam_instance_profile = "${aws_iam_instance_profile.ecs_profile.name}"
    security_groups = [
        "${aws_security_group.allow_all_outbound.id}",
        "${aws_security_group.allow_cluster.id}"
    ]
    user_data = "#!/bin/bash\necho ECS_CLUSTER='${var.application_name}-cluster' > etc/ecs/ecs.config"
}

resource "aws_autoscaling_group" "ecs_cluster" {
    name = "ecs-auto-scaling"
    availability_zones = ["eu-west-1a", "eu-west-1b"]
    min_size = 1
    max_size = 2
    launch_configuration = "${aws_launch_configuration.ecs_cluster_launch.name}"
    load_balancers = ["${aws_elb.client-service-elb.name}"]
}

# Application Load Balancer
resource "aws_elb" "client-service-elb" {
    name = "${var.client_service_application_name}-${var.client_service_application_version}-elb"
    availability_zones = ["eu-west-1a", "eu-west-1b"]
    security_groups = [
        "${aws_security_group.allow_cluster.id}",
        "${aws_security_group.allow_all_inbound.id}",
        "${aws_security_group.allow_all_outbound.id}"
    ]

    listener {
        instance_port = 9000
        instance_protocol = "http"
        lb_port = 80
        lb_protocol = "http"
    }

    health_check {
        healthy_threshold = 2
        unhealthy_threshold = 10
        target = "HTTP:9000/"
        interval = 5
        timeout = 4
    }
}

# Service with Task
resource "aws_ecs_task_definition" "client-application-service-task" {
    family = "${var.client_service_application_name}-${var.client_service_application_version}-task"
    container_definitions = "${data.template_file.client-service-task.rendered}"
}

data "template_file" "client-service-task" {
  template = "${file("task-definitions/client-service-task.tpl")}"

  vars {
    client_service_application_name = "${var.client_service_application_name}"
    client_service_application_version = "${var.client_service_application_version}"
    docker_image_source = "${var.docker_image_source}"
    docker_client_service_image_name = "${var.docker_client_service_image_name}"
  }
}

resource "aws_ecs_service" "client-service" {
    name = "${var.client_service_application_name}-${var.client_service_application_version}-service"
    cluster = "${aws_ecs_cluster.application-cluster.id}"
    task_definition = "${aws_ecs_task_definition.client-application-service-task.arn}"
    desired_count = 1
    iam_role = "${aws_iam_role.ecs_elb.arn}"
    depends_on = ["aws_iam_policy_attachment.ecs_elb"]

    load_balancer {
        elb_name = "${aws_elb.client-service-elb.name}"
        container_name = "${var.client_service_application_name}-${var.client_service_application_version}"
        container_port = 9000
    }
}

