(ns hackerone-pivotaltracker.core
  (:gen-class)
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [clojure.data.json :as json]
            [ring.util.response :refer [redirect]]
            [ring.util.response :refer [status]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.reload :refer [wrap-reload]]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [crypto.equality :as crypto]))

(defn tracker-api-key []
  (or (env :pivotaltracker-api-key)
      (throw (RuntimeException. "Tracker API key is not set"))))

(defn app-secret-key []
  (or (env :app-secret-key)
      (throw (RuntimeException. "Application secret key is not set"))))

(defn tracker-project-id []
  (Integer/parseInt (env :pivotaltracker-project-id)))

(def server-opts
  {:port (Integer/parseInt (env :port "8080")) :max-line (* 1024 64)})

(defn tracker-request-headers []
  {"Content-Type" "application/json" "X-TrackerToken" (tracker-api-key)})

(defn create-url []
  (format "https://www.pivotaltracker.com/services/v5/projects/%d/stories" (tracker-project-id)))

(defn story-url [id]
  (format "https://www.pivotaltracker.com/n/projects/%d/stories/%d" (tracker-project-id) (Integer/parseInt id)))

(defn parse-response [{:keys [status body] :as response}]
  (if (= 200 status)
    (json/read-str body)
    (throw (ex-info "Request failed" response))))

(defn create-tracker-story [attrs]
  (->> {:headers (tracker-request-headers) :body (json/write-str attrs)}
       (client/post (create-url))
       deref
       parse-response))

(defn params->tracker [{:keys [title details]}]
  {:name        title
   :description details
   :labels      [{:name "hackerone"}]
   :story_type  "bug"})

(defroutes routes
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

(defn verify-app-secret [app]
 (fn [req]
   (if (crypto/eq? (get-in req [:params :secret]) (app-secret-key)) (app req) (status req 403))))

(def app
  (cond-> #'routes
          ;; note: verify-app-secret has to be loaded after the
          ;; wrap-keyword-params and wrap-params middleware due
          ;; to the `params` dependency in verify-app-secret
          ;; middleware, changing the order here will cause the
          ;; get-in to return nil without a hard fail.
          true verify-app-secret
          true wrap-keyword-params
          true wrap-params
          (env :dev) wrap-reload
          (env :dev) wrap-exceptions))

(defn -main
  [& args]
  (log/info server-opts)
  (server/run-server app server-opts))
