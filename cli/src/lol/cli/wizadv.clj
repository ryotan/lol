(ns lol.cli.wizadv)

(defonce init-status
  {:location         :living-room
   :object-locations {:whiskey :living-room
                      :bucket  :living-room
                      :chain   :garden
                      :frog    :garden}})

(defonce ^:private app-status (atom init-status))

(defonce nodes {:living-room "you are in living-room. a wizard is snoring loudly on the couch"
                :garden      "you are in a beautiful garden. there is a well in front of you."
                :attic       "you are in the attic. there is a giant welding torch in the corner."})

(defonce edges {:living-room {:garden [:west :door]
                              :attic  [:upstairs :ladder]}
                :garden      {:living-room [:east :door]}
                :attic       {:living-room [:downstairs :ladder]}})

(defonce objects [:whiskey :bucket :frog :chain])

(defn- describe-location [location nodes]
  (location nodes))

(defn- describe-path [edge]
  (let [v         (val edge)
        way       (name (second v))
        direction (name (first v))]
    (str "there is a " way " going " direction " from here.")))

(defn- describe-paths [location edges]
  (map describe-path (edges location)))

(defn- objects-at [loc objs obj-locs]
  (letfn [(at-loc [obj] (= loc (obj-locs obj)))]
    (filter at-loc objs)))

(defn- describe-objects [loc objs obj-loc]
  (letfn [(describe-obj [obj] (str "you see a " (name obj) " on the floor."))]
    (map describe-obj (objects-at loc objs obj-loc))))

(defn- find-path [direction paths]
  (letfn [(same-direction [p] (= direction (-> p val first)))]
    (some #(if (same-direction %) %) paths)))

(defn look []
  (let [st @app-status]
    (concat
      (cons (describe-location (:location st) nodes) '())
      (describe-paths (:location st) edges)
      (describe-objects (:location st) objects (:object-locations st)))))

(defn walk [direction]
  (let [paths (edges (:location @app-status))]
    (if-let [to (find-path direction paths)]
      (do
        (swap! app-status assoc :location (key to))
        (look))
      "you cannot go that way")))

(defn pickup [object]
  (let [st @app-status]
    (if (some #{object} (objects-at (:location st) objects (:object-locations st)))
      (do
        (swap! app-status assoc-in [:object-locations object] :body)
        (str "you are now carrying the " (name object)))
      "you cannot get that.")))

(defn inventory []
  (objects-at :body objects (:object-locations @app-status)))

(defn initialize []
  (reset! app-status init-status))
