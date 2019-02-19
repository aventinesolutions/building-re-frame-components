(ns building-re-frame-components.markdown-preview.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defonce converter (new js/showdown.Converter))

(defn ->html [markdown]
  (.makeHtml converter markdown))

(defn markdown-view [state]
  [:div.markdown
   {:dangerouslySetInnerHTML {:__html (->html state)}}])

(defn markdown-editor-with-preview [initial-value]
  (let [state (reagent/atom {:value initial-value})]
    (fn []
      [:div
       [:textarea
        {:style     {:width "100%"}
         :value     (:value @state)
         :on-change #(swap! state assoc :value (-> % .-target .-value))}]
       [markdown-view (:value @state)]])))

(defn ui []
  [:div
   [markdown-editor-with-preview "# Aventine Solutions"]])

(when-some [el (js/document.getElementById "markdown-preview--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))