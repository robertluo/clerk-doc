```clojure
(ns robertluo.clerk-doc.readme
  (:require [robertluo.clerk-doc :as doc]
            [robertluo.clerk-doc.ai :as ai]))
```
 # io.github.robertluo/clerk-doc
 
 [![CI](https://github.com/robertluo/clerk-doc/actions/workflows/main.yml/badge.svg)](https://github.com/robertluo/clerk-doc/actions/workflows/main.yml)

 Turns Clojure source files into markdown, e.g. derive README.md file from each notebook 
 in [clerk](https://github.com/nextjournal/clerk), you do not have to write README.md 
 file by yourself.
 ## Usage
 
 Write a clojure source file just like this.
 Add an alias in your `deps.edn`: 
```clojure
(comment
  {:clerk-doc {:extra-deps {io.github.robertluo/clerk-doc {:git/tag "v0.1.0" :git/sha "xxxxxx"}}
               :exec-fn    robertluo.clerk-doc/clj->md
               :exec-args  {:from "your-source.clj" :to "README.md"
                            :eval-code? true :ai-improve? false}}}
  )
```
 and call it by `clojure -X:clerk`
 ## Goodies
  - If `:eval-code?` is *true*, the clojure code in the file will be evaluated, put 
    the result back to the original code blocks. An good example will be [the clojure tutorial from Clojure for the Brave and True](https://github.com/Nextjournal/lfba/blob/master/taking-your-first-steps-in-clojure-1.clj).
  - Thanks to [openai-clojure](https://github.com/wkok/openai-clojure) the
    `:ai-improve?` flag using OpenAI to improve your result md file.
    You need set up your environment, please refer to the above link.
  > You have to have a paid account of OpenAI, otherwise the OpenAI's rate limit will generate
  > a 400 error for anything longer than one simple sentence.
 ## [UnLicense](https://unlicense.org/)
 2023 Robert Luo
