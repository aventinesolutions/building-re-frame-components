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
      [:form
       (pr-str @s)
       [:input {:type (if (:show? @s) :text :password)
                :style {:width "100%"}
                :value (:value @s)
                :on-change #(swap! s assoc
                                   :value
                                   (-> % .-target .-value))}]
       [:label [:input {:type :checkbox
                        :checked (:show? @s)
                        :on-change #(swap! s assoc
                                           :show?
                                           (-> % .-target .-checked))}]
        " Show password?"]])))

(defn ui []
  [:div
   [password-box ""]])

(when-some [el (js/document.getElementById "password-box--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))
