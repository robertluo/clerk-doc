```clojure
(ns robertluo.clerk-doc.readme
  (:require [robertluo.clerk-doc :as doc]
            [robertluo.clerk-doc.ai :as ai]))
```
 # io.github.robertluo/clerk-doc
 Turn Clojure source files into markdown. Usually [clerk](https://github.com/nextjournal/clerk) are good 
 candidates, when you do not feel like write README.md file for your Github projects.
 ## Usage
 
 Write a clojure source file just like this, then call `doc/notebook->md` function.
```clojure
(comment
  (with-bindings {#'doc/*eval-code?* true}
    (doc/notebook->md {:from "src/robertluo/clerk_doc/readme.clj" :to "README.md" :fn-improve ai/ask-for-comment}))
  )
```
 ## Goodies

  - If `doc/*eval-code?*` is true (default), the Clojure code in the source file will be evaluated, 
    the result will be appended to the generated code blocks.
  - `ai/ask-for-comment` uses ChatGPT to improve your result md file, you may want to merge
    the improvement back. If you are not going to use it, just do not set the `:fn-improve`
    option.
  > To use `ai/ask-for-comment`, sign up for a paid account of OpenAI, otherwise the OpenAI's rate limiting will generate
  > a HTTP 400 error if the comment you sent is longer than a simple sentence.
 ## [UnLicense](https://unlicense.org/)
 2023 Robert Luo
