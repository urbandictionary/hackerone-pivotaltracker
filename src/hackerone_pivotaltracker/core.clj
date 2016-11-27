(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn get-port []
  (if-let [e (env :port)]
    (Integer/parseInt e)
    8080))

(defn -main
  [& args]
  (let [port (get-port)]
    (log/info "Starting on port" port)
    (server/run-server app {:port port})))
