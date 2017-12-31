(ns logfmt.core)

(defn- safe-println [& more]
  "The Clojure `println` function has a race condition. See [this blog post](http://yellerapp.com/posts/2014-12-11-14-race-condition-in-clojure-println.html) for more information."
  (.write *out* (str (clojure.string/join " " more) "\n")))

(defn- format-key-value-pairs
  [[k v]]
  (let [format-str (if (and (string? v) (re-find #"(\/|\s)" v))
                     "%s=\"%s\""
                     "%s=%s")]
    (format format-str (name k) v)))

(defn info
  ([config message]
   (info config message {}))
  ([config message attrs]
   (let [attr-str (clojure.string/join " " (map format-key-value-pairs attrs))]
     (if (:dev-mode config)
       (if (= "" attr-str)
         (safe-println "info |" message)
         (safe-println "info |" message attr-str))
       (if (= "" attr-str)
         (safe-println "at=info" (format "%s=\"%s\"" "msg" message))
         (safe-println "at=info" (format "%s=\"%s\"" "msg" message) attr-str))))))
