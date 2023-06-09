(ns robertluo.clerk-doc.ai
  "experimental AI text improvement"
  (:require [wkok.openai-clojure.api :as api]))

(defn ask-for-comment
  "returns an improved version of `text` with help of OPENAI"
  [text]
  (-> (api/create-completion
       {:model "text-davinci-003" 
        :top_p 1.0
        :max_tokens 2048
        :prompt (str "Correct this to standard English in Markdown format: " text)})
      (get-in [:choices 0 :text])))

(comment
  (ask-for-comment "Maps and collections are butter and bread in Clojure, and we store all kinds of data. Therefore, getting data (aka query) is very important, and get-in and select-keys are very common in our code.
                   However, if maps are deeply nested, this approach becomes cumbersome. Some excellent libraries are trying to make it easy, like Specter and Datomic pull API.
                   Lasagna-pull’s query turns a query pattern (a kind of data structure) into a function:")
  )