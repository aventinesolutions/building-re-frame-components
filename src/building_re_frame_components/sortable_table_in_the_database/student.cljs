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

(defn log [& args] (apply (.-log js/console) args))

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
            (fn [[_ key] _] (rf/subscribe [:table key]))
            (fn [table]
              (let [key       (:sort-key table)
                    direction (:sort-direction table)
                    rows      (cond->> (:rows table) key        (sort-by #(nth % key))
                                       (= :ascending direction) reverse)]
                (assoc table :rows rows))))

(rf/reg-event-fx :table-rotate-sort
                 (fn
                   [{:keys [db]}
                    [_ key index]]
                   (let [table (get-in db (:tables key))
                         sorts [(:sort-key table) (:sort-direction table)]
                         event {:dispatch (cond (= [index :ascending] sorts)  [:table-remove-sort key]
                                                (= [index :descending] sorts) [:table-sort-by key index :ascending]
                                                :else                         [:table-sort-by key index :descending])}]
                     (log (map #(str %) (:dispatch event))) event)))

(defn sortable-table [table-key]
  (let [table @(rf/subscribe [:table-sorted table-key])
        sorts [(:sort-key table) (:sort-disrection table)]]
    [:table
     {:style {:font-size "80%"}}
     [:tr
      (for [[index header] (map vector (range) (:header table))]
        [:th
         {:on-click #(rf/dispatch [:table-rotate-sort table-key index])}
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
     (for [row (:rows table)]
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