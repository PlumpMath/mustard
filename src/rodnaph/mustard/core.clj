
(ns rodnaph.mustard.core
  (:require [aleph.http :refer [start-http-server]]
            [lamina.core :refer [receive permanent-channel read-channel filter* siphon]]))

(def broadcast
  (permanent-channel))

(defn- accept? [id message]
  (= id message))

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

