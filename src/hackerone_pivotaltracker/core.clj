(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [environ.core :refer [env]]))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn port-env []
  (if-let [e (env :port)]
    (Integer/parseInt e)
    8080))

(defn -main
  [& args]
  (server/run-server app {:port (port-env)}))
