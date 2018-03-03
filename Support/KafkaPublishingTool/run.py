from command_line_processor import process_command_line_arguments
from file_readers.json_file_reader import read_json_file
from kafka_producers.kafka_producers import KafkaJsonMessageProducer

if __name__ == '__main__':
    """
    Parses command line input, and start support tool
    """
    ARGUMENTS = process_command_line_arguments()

    json_contents = read_json_file(ARGUMENTS.messagefile)
    json_messages = json_contents["ordered_messages"]

    message_producer = KafkaJsonMessageProducer(ARGUMENTS.kafkaurl.split(","), ARGUMENTS.kafkatopic)

    for message in json_messages:
        message_producer.produce_message(message)
