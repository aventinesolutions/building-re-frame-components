(ns building-re-frame-components.draggable-list.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn change-position [order position item]
  (let [order (remove #{item} order)
        head  (take position order)
        tail  (drop position order)]
    (concat head [item] tail)))

(defn draggable-list [{:keys [on-reorder] :or {on-reorder (fn [])}} & items]
  (let [items (vec items)
        state (reagent/atom {:order (range (count items))})]
    (fn []
      [:div
       [:ul
        (doall
          (for [[index position] (map vector (:order @state) (range))]
            [:li
             {:key           index
              :draggable     true
              :style         {:border (when (= index (:drag-index @state)) "1px solid green")}
              :on-drag-start #(swap! state assoc :drag-index index)
              :on-drag-end   (fn []
                               (swap! state dissoc :drag-index :drag-over)
                               (on-reorder (:order @state)))
              :on-drag-over  (fn [event]
                               (.preventDefault event)
                               (swap! state assoc :drag-over position)
                               (swap! state update :order
                                      change-position (:drag-over @state) (:drag-index @state)))
              :on-drag-leave (fn [event]
                               (swap! state assoc :drag-over :nothing))}
             (get items index)]))]])))

(defn ui []
  [:div
   [draggable-list
    {:on-reorder (fn [new-order] (js/console.log (pr-str new-order)))}
    "ximo"
    "flip"
    "quip"
    "pequercky"
    "synon"]])

(when-some [el (js/document.getElementById "draggable-list--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))