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
      (cond
       (= first before)
       (cons item (remove #{item} order))
       (= first item)
       (change-position rest before item)
       :else
       (cons first (change-position rest before item))))))

(defn draggable-list [& items]
  (let [items (vec items)
        state (reagent/atom {:order (range (count items))})]
    (fn []
      [:div
       [:ul
        (for [[index position] (map vector (:order @state) (range))]
          [:li
           {:key           index
            :draggable     true
            :style         {:border (when (= index (:drag-index @state)) "1px solid green")}
            :on-drag-start #(swap! state assoc :drag-index index)
            :on-drag-end   #(swap! state dissoc :drag-index :drag-over)
            :on-drag-over  (fn [event]
                             (.preventDefault event)
                             (swap! state assoc :drag-over position)
                             (swap! state update :order
                                    change-position (:drag-over @state) (:drag-index @state)))
            :on-drag-leave (fn [event]
                             (swap! state assoc :drag-over :nothing)
                             (swap! state update :order
                                    change-position (:drag-over @state) (:drag-index @state)))}
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