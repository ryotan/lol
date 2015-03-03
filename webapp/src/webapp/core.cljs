(ns webapp.core
  (:require [figwheel.client :as fw]
            [om.core :as om :include-macros true]
            [sablono.core :refer-macros [html]]))

(enable-console-print!)

(def app-state
  (atom
    {:people
     [{:type :student :first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
      {:type  :student :first "Alyssa" :middle-initial "P" :last "Hacker"
       :email "aphacker@mit.edu"}
      {:type  :professor :first "Gerald" :middle "Jay" :last "Sussman"
       :email "metacirc@mit.edu" :classes [:6001 :6946]}
      {:type :student :first "Eva" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
      {:type :student :first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
      {:type    :professor :first "Hal" :last "Abelson" :email "evalapply@mit.edu"
       :classes [:6001]}]
     :classes
     {:6001 "The Structure and Interpretation of Computer Programs"
      :6946 "The Structure and Interpretation of Classical Mechanics"
      :1806 "Linear Algebra"}}))

(extend-type js/String
  ICloneable
  (-clone [s] (js/String. s))
  om/IValue
  (-value [s] (str s)))

(defn display [show]
  (if show
    {}
    {:display "none"}))

(defn handle-change [e text owner]
  (om/transact! text #(.. e -target -value)))

(defn commit-change [text owner]
  (om/set-state! owner :editing false))

(defn editable [text owner]
  (reify
    om/IInitState
    (init-state [_]
      {:editing false})
    om/IRenderState
    (render-state [_ {:keys [editing]}]
      (html [:li
             [:span {:style (display (not editing))} (om/value text)]
             [:input {:style     (display editing)
                      :value     (om/value text)
                      :onChange  #(handle-change % text owner)
                      :onKeyDown #(when (= (.-key %) "Enter") (commit-change text owner))
                      :onBlur    #(commit-change text owner)}]
             [:button {:style   (display (not editing))
                       :onClick #(om/set-state! owner :editing true)} "Edit"]]))))

(defn middle-name [{:keys [middle middle-initial]}]
  (cond
    middle (str " " middle)
    middle-initial (str " " middle-initial ".")))

(defn display-name [{:keys [first last] :as contact}]
  (str last ", " first (middle-name contact)))

(defn student-view [student owner]
  (reify om/IRender
    (render [_]
      (html [:li (display-name student)]))))

(defn professor-view [professor owner]
  (reify om/IRender
    (render [_]
      (html [:li
             [:div (display-name professor)]
             [:label "Classes"]
             [:ul (map #(html [:li (om/value %)]) (:classes professor))]]))))

(defmulti entry-view (fn [person _] (:type person)))

(defmethod entry-view :student
  [person owner] (student-view person owner))

(defmethod entry-view :professor
  [person owner] (professor-view person owner))

(defn people [data]
  (->> data
    :people
    (mapv #(if (:classes %)
            (update-in % [:classes]
              (fn [cs] (mapv (:classes data) cs)))
            %))))

(defn registry-view [data owner]
  (reify om/IRenderState
    (render-state [_ state]
      (html [:div {:id "registry"}
             [:h2 "Registry"]
             [:ul (om/build-all entry-view (people data))]]))))

(defn classes-view [data owner]
  (reify om/IRender
    (render [_]
      (html [:div {:id "classes"}
             [:h2 "Classes"]
             [:ul (om/build-all editable (vals (:classes data)))]]))))

(om/root registry-view app-state
  {:target (. js/document (getElementById "registry"))})

(om/root classes-view app-state
  {:target (. js/document (getElementById "classes"))})

(fw/start {
           :load-warninged-code true
           :on-jsload (fn []
                        ;; (stop-and-start-my app)
                        )
           })
