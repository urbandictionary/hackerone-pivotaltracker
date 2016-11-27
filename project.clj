(defproject hackerone-pivotaltracker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [http-kit "2.2.0"]
                 [environ "1.1.0"]
                 [org.clojure/tools.logging "0.3.1"]]
  :main ^:skip-aot hackerone-pivotaltracker.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
