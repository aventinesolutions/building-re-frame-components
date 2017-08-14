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
 :inc-actual (fn [db [_ x]] (update db :actual + x)))

(defonce _interval (js/setInterval #(rf/dispatch [:inc-actual 2.3]) 1000))

(defn progress [done total]
  (let [percent (* 100 (/ done total))]
    [:div percent]))

(defn ui []
  [:div
   @(rf/subscribe [:actual]) " / " @(rf/subscribe [:expected])
   (progress @(rf/subscribe [:actual])
             @(rf/subscribe [:expected]))])

(when-some [el (js/document.getElementById "progress-bar--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
