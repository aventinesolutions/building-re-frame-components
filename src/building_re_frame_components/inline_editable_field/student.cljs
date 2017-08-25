(ns building-re-frame-components.inline-editable-field.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
  :initialize
  (fn [db _]
    (assoc db :movies {"tt0095989"
              {:title "Return of the Killer Tomatoes!"
               :description "Crazy old Professor Gangreen has developed a way to make tomatoes look human for a second invasion."}})))

(rf/reg-sub
  :movies
  (fn [db _]
    (:movies db)))

(defn inline-editor [text]
  (let [s (reagent/atom {})]
    (fn [text]
      [:span {:on-click #(swap! s assoc :editing? true)}
       text])))

(defn ui []
  [:div
   (pr-str @(rf/subscribe [:movies]))
   (for [[movie-id movie] @(rf/subscribe [:movies])]
     [:div {:key movie-id}
      [:h3 [inline-editor (:title movie)
            #(rf/dispatch [:movie/title movie-id %])]]
      [:div [inline-editor (:description movie)
             #(rf/dispatch [:movie/description movie-id %])]]])])

(when-some [el (js/document.getElementById "inline-editable-field--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (defonce _render (reagent/render [ui] el)))
