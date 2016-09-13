(ns simplekafka.produce
  (:require [clj-kafka.admin :as admin]
            [clj-kafka.new.producer :as np]
            [cognitect.transit :as transit])
  (:import (java.io ByteArrayOutputStream))
  (:gen-class))

(def default-topic "test-data")

(defn transit-encode
  [data]
  (let [out    (ByteArrayOutputStream. 4096)
        writer (transit/writer out :msgpack)]
    (transit/write writer data)
    (.toByteArray out)))

(defn make-kafka-topic!
  [topic-name]
  (with-open [zk (admin/zk-client "localhost:2181")]
    (admin/create-topic zk topic-name
                        {:partitions 1
                         :replication-factor 1})))

(defn kill-kafka-topic!
  [topic-name]
  (with-open [zk (admin/zk-client "localhost:2181")]
    (when (admin/topic-exists? zk topic-name)
      (admin/delete-topic zk topic-name))))

(defn bounce-topic!
  [topic]
  (kill-kafka-topic! topic)
  (Thread/sleep 1000)
  (make-kafka-topic! topic))

(defn publish-kafka-messages!
  [topic-name input-record]
  (with-open [p (np/producer
                 {"bootstrap.servers" "localhost:9092"}
                 (np/byte-array-serializer)
                 (np/byte-array-serializer))]
    (println "publishing messages")
    (dotimes [_ 50000]
      @(np/send p (np/record topic-name input-record)))))

(def row-data {:AAAAAAA 1
               :BBBBBBB "adasdasdad"
               :STATE "TT"
               :BIRTH_YEAR 1999
               :ADDR_LINE_1 "sadas asd asd asd as"
               :CCCCCCC "zxcxzcz"
               :ADDR_LINE_2 ""
               :DDDDDD ""
               :ZIPCODE "000000"
               :EEEEEEEEEE "Active"
               :LAST_PROCESSED_TXN_DT 1472511600000
               :LAST_NAME "SDsadaefpasfsa"
               :FFFFFFF 419439.0
               :FIRST_NAME "salkdjsado9uasds"
               :CITY "asdsadasdas"
               :GGGGGGG "Active"
               :HHHHHHHH "Horizon"
               :INACTV_DT ""
               :TITLE "sdsadasd"
               :ENRL_DT 553734000000
               :COUNTRY "sdsadasdasd"
               :KKKKKKKKK 80
               :LLLLLLLLLL "dasdsadasdasdasd"})

(defn -main
  [& args]
  ;; Recreate topic
  (bounce-topic! default-topic)

  (publish-kafka-messages! default-topic
                           (transit-encode {:type :data/record
                                            :data (repeat 100 row-data)})))

