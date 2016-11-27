(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn -main
  [& args]
  (let [port (Integer/parseInt (env :port "8080"))]
    (log/info "Starting on port" port)
    (server/run-server app {:port port})))
