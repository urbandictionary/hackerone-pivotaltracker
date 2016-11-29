(ns hackerone-pivotaltracker.core-test
  (:require [midje.sweet :refer :all]
            [environ.core :refer [env]]
            [hackerone-pivotaltracker.core :refer :all]))

(fact "from env var"
      (tracker-api-key) => "foobar"
      (provided
        (env :pivotaltracker-api-key) => "foobar"))

(fact "missing"
      (tracker-api-key) => (throws "Tracker API key is not set")
      (provided
        (env :pivotaltracker-api-key) => nil))