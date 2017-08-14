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

(defn progress [done total]
  [:div])

(defn ui []
  [:div
   @(rf/subscribe [:actual]) " / " @(rf/subscribe [:expected])
   (progress @(rf/subscribe [:actual])
             @(rf/subscribe [:expected]))])

(when-some [el (js/document.getElementById "progress-bar--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
