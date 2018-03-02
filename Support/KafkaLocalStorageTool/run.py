from command_line_procesor import process_command_line_arguments
from kafka_consumers.kafka_consumers import KafkaJsonMessageConsumer
from output_producers.output_producers import produce_json_file_output

if __name__ == '__main__':
    """
    Parses command line input, and start support tool
    """
    ARGUMENTS = process_command_line_arguments()

    consumer = KafkaJsonMessageConsumer(ARGUMENTS.kafkaurl.split(","), ARGUMENTS.kafkatopic)
    messages = consumer.consume_messages_in_topic()
    print(messages)

    produce_json_file_output(ARGUMENTS.kafkatopic, messages)

