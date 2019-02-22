(ns building-re-frame-components.draggable-list.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn draggable-list [& items]
  (let [items (vec items)
        state (reagent/atom {:order (range (count items))})]
    (fn []
      [:div
       [:code (pr-str @state)]
       [:ul
        (for [index (:order @state)]
          [:li
           {:key           index
            :draggable     true
            :on-drag-start #(swap! state assoc :drag-index index)
            :on-drag-end #(swap! state dissoc :drag-index)
            :on-drag-over (fn [event]
                            (.preventDefault event)
                            (swap! state assoc :drag-over index) )}
           (get items index)])]])))

(defn ui []
  [:div
   [draggable-list
    "ximo"
    "flip"
    "quip"
    "pequercky"
    "synon"]])

(when-some [el (js/document.getElementById "draggable-list--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))