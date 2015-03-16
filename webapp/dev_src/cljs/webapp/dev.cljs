(ns webapp.dev
  (:require
    [webapp.core]
    [figwheel.client :as fw]))

(fw/start {
           :load-warninged-code true
           :websocket-url       "ws://localhost:3449/figwheel-ws"
           :on-jsload           (fn []
                                  ;; (stop-and-start-my app)
                                  )})
