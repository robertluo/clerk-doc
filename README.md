```clojure
(ns robertluo.clerk-doc.readme
  (:require [robertluo.clerk-doc :as doc]
            [robertluo.clerk-doc.ai :as ai]))
```
 # io.github.robertluo/clerk-doc
 [![CI](https://github.com/robertluo/clerk-doc/actions/workflows/main.yml/badge.svg)](https://github.com/robertluo/clerk-doc/actions/workflows/main.yml)
 Turns your Clojure source files into markdown. [clerk](https://github.com/nextjournal/clerk) notebooks are good 
 candidates, when you don't want to write README.md file for your Github projects.
 ## Usage
 
 Write a Clojure source file just like this.
 Add an alias in your `deps.edn`: 
```clojure
(comment
  {:clerk-doc {:extra-deps {io.github.robertluo/clerk-doc {:git/tag "v0.1.0" :git/sha "xxxxxx"}}
               :exec-fn    robertluo.clerk-doc/clj->md
               :exec-args  {:from "your-source.clj" :to "README.md"
                            :eval-code? true :ai-improve? false}}}
  )
```
 and call it with `clojure -X:clerk` that's it, by default it converts from `your-source.clj` to `README.md`.   
 ## Goodies
  - If `:eval-code?` set to true, it will evaluate the Clojure code within the file and append the result to 
    the generated code blocks.
  - Thanks to [openai-clojure](https://github.com/wkok/openai-clojure), with the
    `:ai-improve?` flag you can rely on OpenAI to improve your md file.  A few other
    requirements for this functionality: 
    - Open a free account on OpenAI, the API rate limit will generate  a 400 error for anything longer than one simple sentence.
    - Set up an environment variable named `OPENAI_KEY` with your paid API key from OpenAI.
    - Turn on the `ai-improve?` flag in your `deps.edn`.
    
 ## [UnLicense](https://unlicense.org/)
 2023 Robert Luo
