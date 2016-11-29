(ns hackerone-pivotaltracker.core-test
  (:require [midje.sweet :refer :all]
            [environ.core :refer [env]]
            [org.httpkit.fake :refer [with-fake-http]]
            [hackerone-pivotaltracker.core :refer :all]))

(facts "tracker-api-key"
       (fact "from env var"
             (tracker-api-key) => "foobar"
             (provided (env :pivotaltracker-api-key) => "foobar"))

       (fact "missing"
             (tracker-api-key) => (throws "Tracker API key is not set")
             (provided (env :pivotaltracker-api-key) => nil)))

(facts "tracker-project-id"
       (fact "from env var"
             (tracker-project-id) => 1234
             (provided (env :pivotaltracker-project-id) => "1234"))

       (fact "invalid"
             (tracker-project-id) => (throws "For input string: \"asdf\"")
             (provided (env :pivotaltracker-project-id) => "asdf")))

(facts "create-tracker-story"
       (fact "sends request and parses the response"
             (with-fake-http [{:url    "https://www.pivotaltracker.com/services/v5/projects/1234/stories"
                               :headers {"Content-Type" "application/json" "X-TrackerToken" "foobar"}
                               :method :post}
                              {:status 200
                               :body   "{\"a\":\"b\"}"}]
                             (create-tracker-story {:foo "bar"}) => {"a" "b"}
                             (provided (env :pivotaltracker-project-id) => "1234")
                             (provided (env :pivotaltracker-api-key) => "foobar"))))