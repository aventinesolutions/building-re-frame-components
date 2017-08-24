(ns building-re-frame-components.progress-bar.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
  :initialize
  (fn [_ _]
    {:actual 0 :expected 83}))

(rf/reg-sub
 :actual
 (fn [db] (:actual db)))

(rf/reg-sub
 :expected
 (fn [db] (:expected db)))

(rf/reg-event-db
 :inc-actual
 (fn [db [_ x]]
   (let [new (+ x (:actual db))]
     (if (>= new (:expected db))
       (assoc db :actual (:expected db))
       (assoc db :actual new)))))

(rf/reg-event-db
 :reset-expected
 (fn [db [_ expected]]
   (assoc db :expected expected :actual 0)))

(rf/dispatch [:reset-expected 83])

(defonce _interval (js/setInterval #(rf/dispatch [:inc-actual 2.3]) 1000))

(defn progress [done total]
  (let [s (reagent/atom {})]
    (fn [done total]
      (let [percent (str (.toFixed (* 100 (/ done total)) 1) "%")]
        [:div percent
         [:div {:style {:position :relative :line-height "1.3em"}}
          [:div {:style {:background-color :green 
                         :top 0
                         :bottom 0
                         :transition "width 0.1s"
                         :width percent
                         :position :absolute
                         :overflow :hidden}}
           [:div percent]]
          [:div {:style {:text-align :center}}
           [:span {:ref #(swap! s assoc :left 0)}
            percent]]]]))))

(defn ui []
  [:div
   @(rf/subscribe [:actual]) " / " @(rf/subscribe [:expected])
   (progress @(rf/subscribe [:actual])
             @(rf/subscribe [:expected]))])

(when-some [el (js/document.getElementById "progress-bar--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
