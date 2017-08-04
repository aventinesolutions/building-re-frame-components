(ns building-re-frame-components.sortable-table.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(def data
  [["Name" "Weapon" "Side" "Height (m)"]
   ["Luke Skywalker" "Blaster" "Good" 1.72]
   ["Leia Organa" "Blaster" "Good" 1.5]
   ["Han Solo" "Blaster" "Good" 1.8]
   ["Obi-Wan Kenobi" "Light Saber" "Good" 1.82]
   ["Chewbacca" "Bowcaster" "Good" 2.28]
   ["Darth Vader" "Light Saber" "Bad" 2.03]
   ])

(rf/reg-event-db
 :initialize
 (fn [db _]
   {:tables {:new-hope {:header (first data)
                        :rows (vec (rest data))}}}))

(rf/reg-sub
 :table
 (fn [db [_ key]]
   (get-in db [:tables key])))

(defn sortable-table [table-key]
  (let [s (reagent/atom {})]
    (fn [table-key]
      (let [table @(rf/subscribe [:table table-key])]
        [:div (pr-str @s)
         [:table {:style {:font-size "80%"}}
          [:tr (for [[i h] (map vector (range) (:header table))]
                 [:th {:style {:line-height "1em"
                               :padding-right "1em"}}
                  [:div {:style {:display :inline-block}} h]
                  [:div {:style {:display :inline-block
                                 :vertical-align :middle
                                 :font-size "80%"
                                 :margin-left "0.33333em"
                                 :line-height "1em"}}
                   [:div {:on-click
                          #(swap! s assoc :sort-key i :sort-direction :ascending)}
                    "▲"]
                   [:div {:on-click
                          #(swap! s assoc :sort-key i :sort-direction :descending)}
                    "▼"]]])]
          (for [row (:rows table)]
            [:tr (for [v row] [:td v])])]]))))

(defn ui []
  [:div
   [sortable-table :new-hope]])

(when-some [el (js/document.getElementById "sortable-table--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
