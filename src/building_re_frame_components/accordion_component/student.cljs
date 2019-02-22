(ns building-re-frame-components.accordion-component.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn accordion [options & children]
  (let [state (reagent/atom {:current (:active options)})]
    (fn [options & children]
      [:div
       (pr-str @state)
       (let [groups (partition 2 children)
             groups (map conj groups (range))]
         (for [[index header content] groups]
           [:div
            [:div
             {:style
              {:background-color "#aaa"}
              :on-click (fn []
                          (swap! state update :current #(if (= index %) nil index)))}
             header]
            [:div
             {:style
              {:background-color "#ccc"
               :height           (if (= index (:current @state)) (when-let [element (get-in @state  [:refs index])] (.-clientHeight element)) 0)
               :overflow         "hidden"
               :transition       "height 0.5s"}}
             [:div {:ref #(swap! state assoc-in [:refs index] %)} content]]]))])))

(defn ui []
  [:div
   [accordion
    "abbie"
    [:ul [:li "bemb"] [:li "xani"] [:li "frip"]]
    "blimp"
    [:p "frap"]
    "chuck"
    [:p [:em "pamp"]]]])

(when-some [el (js/document.getElementById "accordion-component--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))