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
      (let [child-height (:child-height @s)]
        [:div
       [:div
        {:on-click #(swap! s update :open not)
         :style {:background-color "#ddd"
                 :padding "0 1em"}}
        title]
       [:div {:style {:max-height (if (:open @s)
                                child-height
                                0)
                      :transition "max-height 0.8s"
                      :overflow "hidden"}}
        [:div
         {:ref #(when %
                  (swap! s assoc :child-height (.-clientHeight %)))
          :style {:background-color "#eee"
                  :padding "0 1em"}} child]]]))))

(defn ui []
  [:div
     [panel "Frimmel" [example-component]]])

(when-some [el (js/document.getElementById "collapsible-panel--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
