(ns building-re-frame-components.tag-editor.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(defn log [& args] (apply js/console.log args))

(rf/reg-event-db
 :initialize
 (fn [db _]
   (select-keys db (filter #(= "teacher" (namespace %)) (keys db)))))

(rf/reg-event-db :save-tag
                 (fn [db [_ tag]]
                   (update db :tags (fnil conj [] tag) (log (pr-str (:tags db))))))

(rf/reg-sub :tags (fn [db _] (:tags db)))

(defn tag-editor []
  (let [state (reagent/atom "")
        key   (reagent/atom "")]
    (fn []
      [:div
       [:p "state: " @state]
       [:p "key: " @key]
       [:input
        {:type      :text
         :style     {:width "100%"}
         :value     @state
         :on-key-up (fn [event]
                      (let [pressed (.-key event)]
                        (reset! key pressed)
                        (when
                          (or (= "Enter" pressed) (= " " pressed))
                          (rf/dispatch [:save-tag @state])
                          (reset! state ""))))
         :on-change #(reset! state (-> % .-target .-value))}]
       [:div
        (doall
         (for [tag @(rf/subscribe [:tags])]
           [:div
            {:style {:color            :white
                     :background-color :grey
                     :display          :inline-block
                     :margin           "0.1em"
                     :padding          "0.2em"}}
            tag]))]])))

(defn ui []
  [:div [tag-editor]])

(when-some [el (js/document.getElementById "tag-editor--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))