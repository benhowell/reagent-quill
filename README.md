# reagent-quill
Basic [reagent](http://reagent-project.github.io/) recipe for [Quill rich text editor](http://quilljs.com/)

This is by no means a complete implementation. See the [Quill documentation](http://quilljs.com/docs/quickstart/) for a full rundown of what's possible. Note that the toolbar can have many more functions including your own custom stuff. Also note that this example deals with only HTML and therefore does not use [deltas](https://quilljs.com/guides/designing-the-delta-format/) and as such does no delta <-> HTML conversion. I have built this particular example to both create text and display text (so for just displaying text, building an editor without a toolbar is a as straight forward as it sounds). This recipe provides a clear enough template to riff on.

## Usage

Add the css theme file (adjusting paths to suit) to the head of your file
```cljs
[:head
 ;; other head stuff ...
 (include-css "/quill/css/quill.snow.css")]
```

Add the cljsjs dependency to the `:dependencies` vector of your `project.clj`
```cljs
[cljsjs/quill "1.1.0-3"]
```

reagent-quill takes the following parameters:
 * id (string): a unique id to associate with the editor and toolbar of your quill component
 * content (string | html): the content to display
 * selection (vector [start-index finish-index] | nil): the text to select
 * on-change-fn (function): the function to call when editor text changes

Make sure to require cljsjs.quill somewhere in your project so it is added to your compiled ClojureScript code. I put all my cljsjs deps in core.cljs but it really doesn't matter.

In your reagent app, use quill like so:
```cljs
(ns your.component.or.view
  (:require
   [quill :as quill]))
   
   [:div
    [quill/editor
    {:id "my-quill-component-id"
     :content "welcome to reagent-quill!"
     :selection nil
     :on-change-fn #(if (= % "user")
                      (println (str "text changed: " %2))}]]
```
