(ns lol.cli.ch2.core)

(def ^:private status (atom {:min 1 :max 100 :current 50}))

(defn- guess-my-number []
  (-> @status
      (as-> s (+ (:min s) (:max s)))
      (bit-shift-right 1)))

(defn- update-current! []
  (swap! status assoc :current (guess-my-number))
  (:current @status))

(defn smaller []
  (->> (:current @status)
       dec
       (swap! status assoc :max))
  (update-current!))

(defn bigger []
  (->> (:current @status)
       inc
       (swap! status assoc :min))
  (update-current!))

(defn start-over []
  (reset! status {:min 1 :max 100 :current 50})
  (:current @status))

