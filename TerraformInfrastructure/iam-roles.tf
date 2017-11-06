resource "aws_iam_role" "ecs" {
    name = "ecs"
    assume_role_policy = "${file("policies/ecs-policy.json")}"
}

resource "aws_iam_policy_attachment" "ecs_for_ec2" {
    name = "ecs-for-ec2"
    roles = ["${aws_iam_role.ecs.id}"]
    policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerServiceFullAccess"
}

resource "aws_iam_role" "ecs_elb" {
    name = "ecs-elb"
    assume_role_policy = "${file("policies/ecs-elb-policy.json")}"
}

resource "aws_iam_policy_attachment" "ecs_elb" {
    name = "ecs_elb"
    roles = ["${aws_iam_role.ecs_elb.id}"]
    policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceRole"
}

resource "aws_iam_instance_profile" "ecs_profile" {
    name = "ecs-instance-profile"
    role = "${aws_iam_role.ecs.name}"
}