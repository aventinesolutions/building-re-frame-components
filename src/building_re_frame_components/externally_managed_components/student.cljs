(ns building-re-frame-components.externally-managed-components.student
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]))

(defn log [& args] (apply js/console.log args))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {}))

(defn create-codemirror [element options]
  (js/CodeMirror. element (clj->js options)))

(defn codemirror [initial-value options on-blur]
  (let [state (reagent/atom {:value initial-value})]
    (reagent/create-class
     {:reagent-render (fn [] [:div])
      :component-did-mount
      (fn [component]
        (let [editor (create-codemirror (reagent/dom-node component)
                                        (assoc options :value initial-value))]
          (when on-blur (.on editor "blur" #(on-blur (:value @state))))
          (.on editor "change" #(swap! state assoc :value (.getValue editor)))))})))

(defn ui []
  [:div
   [codemirror "Aventine Solutions" {:lineNumbers true} (fn [value] (js/console.log (pr-str value)))]])

(when-some [el (js/document.getElementById "externally-managed-components--student")]
  (defonce _init (rf/dispatch-sync [:initialize]))
  (reagent/render [ui] el))