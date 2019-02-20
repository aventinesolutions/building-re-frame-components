(ns building-re-frame-components.accordion-component.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn accordion [& children]
  (let [state (reagent/atom {:current nil})]
    (fn [& children]
      [:div
       (pr-str @state)
       (let [groups (partition 2 children)
             groups (map conj groups (range))]
         (for [[index header content] groups]
           [:div
            [:div
             {:style
              {:background-color "#aaa"}
              :on-click (fn [] (swap! state assoc :current index))}
             index
             header]
            [:div
             {:style
              {:background-color "#ccc"
               :height           (if (= index (:current @state)) :auto 0)
               :overflow         "hidden"}}
             content]]))])))

(defn ui []
  [:div
   [accordion
    "abbie"
    [:ul [:li "bemb"] [:li "xani"] [:li "frip"]]
    "blimp"
    [:p "frap"]
    "chuck"
    [:p [:em "pamp"]]]])

(when-some [el (js/document.getElementById "accordion-component--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))