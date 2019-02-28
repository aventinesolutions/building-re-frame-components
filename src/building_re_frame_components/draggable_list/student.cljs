(ns building-re-frame-components.draggable-list.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn change-position [order before item]
  (if (empty? order)
    (list item)
    (let [[first & rest] order]
      (cond (= first before) (cons item (remove #{item} order))))))

(defn draggable-list [& items]
  (let [items (vec items)
        state (reagent/atom {:order (range (count items))})]
    (fn []
      [:div
       [:ul
        (for [index (:order @state)]
          [:li
           {:key           index
            :draggable     true
            :on-drag-start #(swap! state assoc :drag-index index)
            :on-drag-end   #(swap! state dissoc :drag-index :drag-over)
            :on-drag-over  (fn [event]
                             (.preventDefault event)
                             (swap! state assoc :drag-over index))
            :on-drag-leave (fn [] (swap! state assoc :drag-index :nothing))}
           (get items index)])]
       [:code (pr-str @state)]])))

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