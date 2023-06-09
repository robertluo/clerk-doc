(ns robertluo.clerk-doc
  "create documentation from notebooks"
  (:require
   [rewrite-clj.zip :as zip]
   [clojure.string :as str]
   [robertluo.clerk-doc.ai :as ai]))

(defn- comment-doc
  [node]
  (let [{:keys [prefix s]} node]
    ; we only treat ;; style comment as document
    (when (and (= ";" prefix) (str/starts-with? s ";"))
      (subs s 1))))

^:rct/test
(comment
  (comment-doc {:s ";hello" :prefix ";"}) ;=> "hello"
  )

(def ^:dynamic *eval-code?*
  "if we should eval the code blocks, default true"
  true)

(defn- code->md
  "turns `zloc` code into `markdown`"
  [zloc]
  (letfn [(of-ex [e] #:error{:type (.getClass e)
                             :message (ex-message e)
                             :data (ex-data e)})
          (eval-pr [form]
            (try (format " ;=> %s" (pr-str (eval form)))
                 (catch Throwable e (format " ;throws=>%s" (of-ex e)))))]
    (cond-> (zip/string zloc)
      *eval-code?*
      (str (when (zip/sexpr-able? zloc)
             (-> (zip/sexpr zloc) (eval-pr)))))))

^:rct/test
(comment
  (code->md (zip/of-string "(+ 1 2)")) ;=> "(+ 1 2) ;=> 3"
  (code->md (zip/of-string "(println 3)")) ;=>> #";=> nil"
  (code->md (zip/of-string "(throw (Exception. \"error\"))")) ;=>> #";throws=>"
  )

(defn- transform-loc
  "transform `zloc` into block pairs"
  [zloc]
  (loop [loc zloc acc []]
    (if (zip/end? loc)
      acc
      (let [[dir elem]
            (case (zip/tag loc)
              :forms [zip/down*]
              :comment [zip/right* [:comment (comment-doc (zip/node loc))]]
              (:whitespace :newline) [zip/right*]
              [zip/right* [:code (code->md loc)]])]
        (recur (dir loc) (if elem (conj acc elem) acc))))))

(defn- merge-blocks
  "merge block pairs"
  [blocks]
  (let [headers (->> (map first blocks) set)
        header (first headers)]
    (when-not (= 1 (count headers))
      (throw (ex-info "blocks has to have the same header" {:header header})))
    [header
     (cond->> (map second blocks)
       (= :code header) (interpose "\n")
       true             (apply str)
       (= :code header) (format "```clojure\n%s\n```\n"))]))

^:rct/test
(comment
  (transform-loc (zip/of-string ";;ok\n5\n")) ;=>> #(= 1 (count %)) 
  )

(defn process-loc
  [ai-improve? zloc]
  (let [f-improve
        (fn [block]
          (cond-> block
            (and ai-improve? (= (first block) :comment))
            (update 1 (comp #(str % "\n") ai/ask-for-comment))))]
    (transduce
     (comp (partition-by first)
           (map merge-blocks)
           (map f-improve)
           (map second))
     str
     (transform-loc zloc))))

^:rct/test
(comment
  (process-loc false (zip/of-string "(+ 1 1)\n;;ok\n;;mine")) ;=> "```clojure\n(+ 1 1) ;=> 2\n```\nok\nmine"
  )


;; ===============
;; Public API

(defn clj->md
  "Convert a clojure source file `from` into markdown file `to`.
   `:ai-improve?` flag send the file to openai to improve it.
   `:eval-code?` flag evals code and attach them to result."
  [{:keys [from to ai-improve? eval-code?]
    :or {ai-improve? false eval-code? false}}]
  (assert (and from to) "You have to specify from and to keys")
  (with-bindings {#'*eval-code?* eval-code?}
    (->> (zip/of-file from)
         (process-loc ai-improve?)
         (spit to))))
