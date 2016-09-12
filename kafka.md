
bin/zookeeper-server-start.sh config/zookeeper.properties

bin/kafka-server-start.sh config/server.properties

bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic test-data

lein run -m simplekafka.produce

lein cljfmt fix src/simplekafka/produce.clj

set
delete.topic.enable = true

in config/server.properties