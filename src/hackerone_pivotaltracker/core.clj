(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [clojure.data.json :as json]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]))

(defn app [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "hello world!"})

(defn tracker-post-url []
  (format "https://www.pivotaltracker.com/services/v5/projects/%s/stories" (env :pivotaltracker-project-id)))

(defn tracker-create [attrs]
  (let [options {:body    (json/write-str attrs)
                 :headers {"Content-Type"   "application/json"
                           "X-TrackerToken" (env :pivotaltracker-api-key)}}
        response @(client/post (tracker-post-url) options)]
    (if (= 200 (:status response))
      (json/read-str (:body response))
      (throw (ex-info "Request failed" response)))))

(defn params->tracker [params]
  (tracker-create {:name (:title params)
                   :description ""
                   :story_type "bug"
                   :external_id "12345"}))

(defn -main
  [& args]
  (let [port (Integer/parseInt (env :port "8080"))]
    (log/info "Starting on port" port)
    (server/run-server app {:port port})))
