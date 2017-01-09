(ns quill
  (:require
   [reagent.core :as r])


(defn quill-toolbar [id]
  [:div {:id (str "quill-toolbar-" id)}

   [:span {:class "ql-formats"}
    [:select {:class "ql-header"}
     [:option {:value "1"}]
     [:option {:value "2"}]
     [:option {:value "3"}]
     [:option {:value "4"}]
     [:option {:value "5"}]
     [:option]]]

   [:span {:class "ql-formats"}
    [:select {:class "ql-font"}
     [:option]
     [:option {:value "serif"}]
     [:option {:value "monospace"}]]]

   [:span {:class "ql-formats"}
    [:select {:class "ql-size"}
     [:option {:value "small"}]
     [:option]
     [:option {:value "large"}]
     [:option {:value "huge"}]]]

   [:span {:class "ql-formats"}
    [:button {:class "ql-bold"}]
    [:button {:class "ql-italic"}]
    [:button {:class "ql-underline"}]
    [:button {:class "ql-strike"}]
    [:button {:class "ql-blockquote"}]]

   [:span {:class "ql-formats"}
    [:select {:class "ql-align"}]]

   [:span {:class "ql-formats"}
    [:button {:class "ql-script" :value "sub"}]
    [:button {:class "ql-script" :value "super"}]]

   [:span {:class "ql-formats"}
    [:button {:class "ql-indent" :value "-1"}]
    [:button {:class "ql-indent" :value "+1"}]]

   [:span {:class "ql-formats"}
    [:select {:class "ql-color"}]
    [:select {:class "ql-background"}]]

   [:span {:class "ql-formats"}
    [:button {:class "ql-clean"}]]])


(defn editor [{:keys [id content selection on-change-fn]}]
  (let [dom-mount (r/atom nil)
        this (r/atom nil)
        value (atom nil)]
    (r/create-class
     {:component-did-mount
      (fn [component]
        (reset! dom-mount (r/dom-node component))
        (reset! this
                (js/Quill.
                 (aget (.-children @dom-mount) 1)
                 #js {:modules #js {:toolbar (aget (.-children @dom-mount) 0)}
                      :theme "snow"
                      :placeholder "Compose an epic..."}))
        (.on @this "text-change"
             (fn [delta old-delta source]
               (reset! value (.. @this -container -firstChild -innerHTML))
               (on-change-fn source @value)))

        (if (= selection nil)
          (.setSelection @this nil)
          (.setSelection @this (first selection) (second selection) "api")))

      :component-will-receive-props
      (fn [component next-props]
        (if
            (or
             (not= (:content (second next-props)) @value)
             (not= (:id (r/props component)) (:id (second next-props))))
          (do
            (if (= selection nil)
              (.setSelection @this nil)
              (.setSelection @this (first selection) (second selection) "api"))
            (.pasteHTML @this (:content (second next-props))))))

      :display-name  (str "quill-editor-" id)

      :reagent-render
      (fn []
        [:div {:id (str "quill-wrapper-" id)}
         [quill-toolbar id]
         [:div {:id (str "quill-editor-" id)
                :class "quill-editor"
                :dangerouslySetInnerHTML {:__html content}}]])})))
