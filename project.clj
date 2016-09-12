(defproject simplekafka "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :jvm-opts ["-Xmx6g"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-kafka "0.3.4"]
                 [com.cognitect/transit-clj "0.8.285"]]
  :main ^:skip-aot simplekafka.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
