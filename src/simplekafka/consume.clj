(ns simplekafka.consume
  (:require [clj-kafka.consumer.zk :as cz]
            [cognitect.transit :as transit]
            [clj-kafka.core :as cc])
  (:import (java.io ByteArrayInputStream))
  (:gen-class))

(def config {"zookeeper.connect" "localhost:2181"
             "group.id" "simplekafka.consume"
             "auto.offset.reset" "smallest"
             "auto.commit.enable" "false"})

(defn transit-decode
  [bytes]
  (let [in     (ByteArrayInputStream. (.value bytes))
        reader (transit/reader in :msgpack)]
    (transit/read reader)))

(defn -main
  [& args]
  (cc/with-resource [c (cz/consumer config)]
    cz/shutdown
    (time (count
              (map transit-decode (take 50000 (cz/messages c "test-data")))))))