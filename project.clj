(defproject hackerone-pivotaltracker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [http-kit.fake "0.2.1"]
                 [ring/ring-mock "0.3.0"]
                 [environ "1.1.0"]
                 [compojure "1.5.1"]
                 [ring/ring-devel "1.5.0"]
                 [prone "1.1.4"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/tools.logging "0.3.1"]
                 [midje "1.8.3"]
                 [crypto-equality "1.0.0"]]
  :min-lein-version "2.6.1"
  :plugins [[lein-environ "1.1.0"]
            [lein-midje "3.2.1"]]
  :main ^:skip-aot hackerone-pivotaltracker.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
