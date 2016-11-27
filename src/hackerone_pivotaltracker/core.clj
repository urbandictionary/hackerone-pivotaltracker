(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [org.httpkit.server :as server]))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello HTTP!"})

(defn -main
  [& args]
  (server/run-server app {:port 8080}))
