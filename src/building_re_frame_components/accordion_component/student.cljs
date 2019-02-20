(ns building-re-frame-components.accordion-component.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn accordion [& children] [:div children])

(defn ui []
  [:div
   [accordion
    "a"
    [:p "Choice A"]
    "b"
    [:p "Choice B"]
    "c"
    [:p "Choice C"]]])

(when-some [el (js/document.getElementById "accordion-component--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))