(ns building-re-frame-components.externally-managed-components.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn [element options] (js/CodeMirror. element (clj->js options)))

(defn codemirror [initial-value option]
  (let [state (reagent/atom [:value initial-value])]
    (reagent/create-class {:reagent-render (fn [] [:div])
                           :component-did-mount (fn [component])})))

(defn ui []
  [:div
   "Put the CodeMirror editor here."])

(when-some [el (js/document.getElementById "externally-managed-components--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))