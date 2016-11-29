(ns hackerone-pivotaltracker.core-test
  (:require [midje.sweet :refer :all]
            [environ.core :refer [env]]
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