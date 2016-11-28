(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [clojure.data.json :as json]
            [ring.util.response :refer [redirect]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]))

(defn story-url [id]
  (format "https://www.pivotaltracker.com/n/projects/%s/stories/%s" (env :pivotaltracker-project-id) id))

(def create-url
  (format "https://www.pivotaltracker.com/services/v5/projects/%s/stories" (env :pivotaltracker-project-id)))

(def tracker-request-headers
  {"Content-Type" "application/json" "X-TrackerToken" (env :pivotaltracker-api-key)})

(def server-opts
  {:port (Integer/parseInt (env :port "8080")) :max-line (* 1024 64)})

(defn parse-response [{:keys [status body] :as response}]
  (if (= 200 status)
    (json/read-str body)
    (throw (ex-info "Request failed" response))))

(defn create-tracker-story [attrs]
  (->> {:headers tracker-request-headers :body (json/write-str attrs)}
       (client/post create-url)
       deref
       parse-response))

(defn params->tracker [{:keys [title details]}]
  {:name        title
   :description details
   :labels      [{:name "hackerone"}]
   :story_type  "bug"})

(defroutes app
           (GET "/create" request
                (-> request
                    :params
                    params->tracker
                    create-tracker-story
                    (get "url")
                    redirect))
           (GET "/view" request
                (-> request
                    :params
                    :id
                    story-url
                    redirect))
           (route/not-found "Not Found"))

(defn -main
  [& args]
  (log/info server-opts)
  (server/run-server (-> #'app
                         wrap-keyword-params
                         wrap-params
                         wrap-reload) server-opts))
