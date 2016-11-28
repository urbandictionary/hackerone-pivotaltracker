(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [clojure.data.json :as json]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]))

(defroutes app
           (GET "/create" request
                (prn-str (:params request)))
           (route/not-found "Not Found"))

(defn stories-url []
  (format "https://www.pivotaltracker.com/services/v5/projects/%s/stories" (env :pivotaltracker-project-id)))

(defn parse-or-throw [response]
  (if (= 200 (:status response))
    (json/read-str (:body response))
    (throw (ex-info "Request failed" response))))

(def tracker-request-headers
  {"Content-Type"   "application/json"
   "X-TrackerToken" (env :pivotaltracker-api-key)})

(defn create-tracker-story [attrs]
  (parse-or-throw @(client/post (stories-url) {:body    (json/write-str attrs)
                                               :headers tracker-request-headers})))

(defn params->tracker [params]
  (create-tracker-story {:name        (:title params)
                         :description ""
                         :story_type  "bug"
                         :external_id "12345"}))

(defn -main
  [& args]
  (let [port (Integer/parseInt (env :port "8080"))]
    (log/info "Starting on port" port)
    (server/run-server (-> #'app wrap-keyword-params wrap-params wrap-reload) {:port port :max-line (* 1024 64)})))
