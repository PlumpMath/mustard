
(ns rodnaph.mustard.core
  (:require [confo.core :refer [confo]]
            [cheshire.core :refer [parse-string]]
            [aleph.http :refer [start-http-server]]
            [lamina.core :refer [receive permanent-channel read-channel filter* siphon]]))

(def config (confo :mustard
                   :port 8080))

(def broadcast
  (permanent-channel))

(defn- in?
  [x xs]
  (some #(= x %) xs))

(defn- accept? [id message]
  (let [data (parse-string message true)]
    (in? id (:users data))))

(defn- register [ch id]
  (let [accept (partial accept? id)]
    (siphon (filter* accept broadcast) ch)
    (siphon ch broadcast)))

(defn- start []
  (start-http-server
    (fn [ch _]
      (receive ch
        (partial register ch)))
    {:port (:port config)
     :websocket true}))

(defn -main []
  (start))

