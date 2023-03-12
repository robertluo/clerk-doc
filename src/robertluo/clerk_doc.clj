(ns robertluo.clerk-doc
 "create documentation from notebooks"
  (:require
   [rewrite-clj.zip :as zip]
   [clojure.string :as str]))

(defn comment-doc
  [node]
  (let [{:keys [prefix s]} node]
    (when (and (= ";" prefix) (str/starts-with? s ";"))
      (subs s 1))))

^:rct/test
(comment
  (comment-doc {:s ";hello" :prefix ";"}) ;=> "hello"
  )

(def ^:dynamic *eval-code?*
  "if we should eval the code blocks, default true"
  true)

(defn code->md
  [zloc]
  (cond-> (zip/string zloc)
    *eval-code?* (str (when-let [eval-rst (when (zip/sexpr-able? zloc) (eval (zip/sexpr zloc)))]
                       (format "\n(comment\n  ;=>\n  %s\n  )" (pr-str eval-rst))))))

^:rct/test
(comment
  (code->md (zip/of-string "3")) ;=>> #"(?s)3.*\(comment.*;=>.*3.*\)"
  )

(defn transform-loc
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

(defn merge-blocks
  [blocks]
  (let [headers (->> (map first blocks) set)
        header (first headers)]
    (when-not (= 1 (count headers))
      (throw (ex-info "blocks has to have the same header" {:header header})))
    [header
     (cond->> (apply str (map second blocks))
       (= :code header) (format "```clojure\n%s\n```\n"))]))

^:rct/test
(comment
  (transform-loc (zip/of-string ";;ok\n5\n")) ;=>> #(= 1 (count %)) 
  )

(defn process-loc
  [zloc]
  (->> zloc
       transform-loc
       (partition-by first)
       (map #(-> % merge-blocks second))
       (apply str)))

^:rct/test
(comment
  (process-loc (zip/of-string "{:a 1};;ok\n;;world\n"));=>>#"(?s)```clojure.*```"
  )

(defn notebook->md
  [{:keys [from to fn-improve] :or {fn-improve identity}}]
  (assert (and from to) "You have to specify from and to keys")
  (->> (zip/of-file from) (process-loc) (fn-improve) (spit to)))

(comment
  ;;do the real transformation
  (notebook->md {:from "../lasagna-pull/notebook/introduction.clj" :to "../lasagna-pull/README.md"})
  )