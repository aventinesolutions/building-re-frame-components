(ns building-re-frame-components.sortable-table-in-the-database.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(def data
  [["Name" "Weapon" "Side" "Height (m)"]
   ["Luke Skywalker" "Blaster" "Good" 1.72]
   ["Leia Organa" "Blaster" "Good" 1.5]
   ["Han Solo" "Blaster" "Good" 1.8]
   ["Obi-Wan Kenobi" "Light Saber" "Good" 1.82]
   ["Chewbacca" "Bowcaster" "Good" 2.28]
   ["Darth Vader" "Light Saber" "Bad" 2.03]])

(rf/reg-event-db
 :initialize
 (fn [db _]
   (assoc db :tables
          {:new-hope {:header (first data)
                      :rows   (rest data)}})))

(rf/reg-sub
 :table
 (fn [db [_ key]]
   (get-in db [:tables key])))

(rf/reg-event-db
 :table-sort-by
 (fn [db [_ key index direction]]
   (update-in db [:tables key] assoc :sort-key index :sort-direction direction)))

(rf/reg-event-db
 :table-remove-sort
 (fn [db [_ key]]
   (update-in db [:tables key] dissoc :sort-key :sort-direction)))

(rf/reg-sub :table-sorted
            (fn [[_ key] _] (rf/subscribe [:table table-key]))
            (fn []
              (let [key (:sort-key table)
                    direction (:sort-direction table)
                    rows (cond->> (:rows table)
                    key (sort-by #(nth % key)) (= :ascending direction) reverse)])))

(defn sortable-table [table-key]
  (let [table @(rf/subscribe [:table table-key])
        key   (:sort-key table)
        dir   (:sort-direction table)
        rows  (cond->> (:rows table)
                       key                (sort-by #(nth % key))
                       (= :ascending dir) reverse)
        sorts [key dir]]
    [:table
     {:style {:font-size "80%"}}
     [:tr
      (for [[index header] (map vector (range) (:header table))]
        [:th
         {:on-click #(cond
                      (= [index :ascending] sorts)
                      (rf/dispatch [:table-remove-sort table-key])
                      (= [index :descending] sorts)
                      (rf/dispatch [:table-sort-by table-key index :ascending])
                      :else
                      (rf/dispatch [:table-sort-by table-key index :descending]))}
         [:div {:style {:display :inline-block}}
          header]
         [:div
          {:style {:display     :inline-block
                   :line-height :1em
                   :font-size   :60%}}
          [:div
           {:style {:color (if (= [index :descending] sorts)
                             :black
                             "#aaa")}}
           "▲"]
          [:div
           {:style {:color (if (= [index :ascending] sorts)
                             :black
                             "#aaa")}}
           "▼"]]])]
     (for [row rows]
       [:tr
        (for [v row]
          [:td v])])]))

(defn ui []
  [:div
   [:p [sortable-table :new-hope]]
   [:p [sortable-table :new-hope]]])

(when-some [el (js/document.getElementById "sortable-table-in-the-database--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))