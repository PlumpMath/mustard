
(ns rodnaph.mustard.core
  (:require [cheshire.core :refer [parse-string]]
            [aleph.http :refer [start-http-server]]
            [lamina.core :refer [receive permanent-channel read-channel filter* siphon]]))

(def broadcast
  (permanent-channel))

(defn- in?
  [x xs]
  (some #(= x %) xs))

(defn- accept? [id message]
  (let [data (parse-string message true)]
    (in? id (:users data))))

(defn- register [ch id]
  (let [accept (partial accept? id)
        local (filter* accept broadcast)]
    (siphon local ch)
    (siphon ch local)))

(defn- start []
  (start-http-server
    (fn [ch _]
      (receive ch
        (partial register ch)))
    {:port 8080 :websocket true}))

(defn -main []
  (start))

