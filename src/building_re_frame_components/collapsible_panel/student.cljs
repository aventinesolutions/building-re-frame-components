(ns building-re-frame-components.collapsible-panel.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {}))

(defn example-component []
  (let [s (reagent/atom 0)]
    (js/setInterval #(swap! s inc) 1000)
    (fn []
      [:div @s])))

(defn panel [title child]
  (let [s (reagent/atom {:open false})]
    (fn [title child]
      [:div
       [:div
        {:on-click #(swap! s update :open not)}
        title]
       [:div (if (:open @s)
               child)]])))

(defn ui []
  [:div
     [panel "Frimmel" [example-component]]])

(when-some [el (js/document.getElementById "collapsible-panel--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
