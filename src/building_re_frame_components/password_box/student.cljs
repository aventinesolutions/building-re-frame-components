(ns building-re-frame-components.password-box.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn password-box [pw]
  (let [s (reagent/atom {:value pw})]
    (fn []
      [:input {:type :password :value (:value @s)}])))

(defn ui []
  [:div
   [password-box ""]])

(when-some [el (js/document.getElementById "password-box--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
