
<!-- generated with openai: standard-12 -->

```clojure
(ns robertluo.clerk-doc.readme
  (:require [robertluo.clerk-doc :as doc]
            [robertluo.clerk-doc.ai :as ai]))
```
 # io.github.robertluo/clerk-doc

 [![CI](https://github.com/robertluo/clerk-doc/actions/workflows/main.yml/badge.svg)](https://github.com/robertluo/clerk-doc/actions/workflows/main.yml)

 Turns Clojure source files into markdown. Usually [clerk](https://github.com/nextjournal/clerk) notebooks are good 
 candidates, when you do not feel like write README.md file for your Github projects.
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
  - If `:eval-code?` set to true, the clojure code in the file will be evaluated, 
    the result will be append to the generated code blocks.
  - Thanks to [openai-clojure](https://github.com/wkok/openai-clojure) the
    `:ai-improve?` flag using OpenAI to improve your result md file.
    You need set up your environment, please refer to the above link.
  > You have to have a paid account of OpenAI, otherwise the OpenAI's rate limit will generate
  > a 400 error for anything longer than one simple sentence.
 ## [UnLicense](https://unlicense.org/)
 2023 Robert Luo
