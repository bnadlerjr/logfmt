(ns logfmt.core)

(defn- safe-println [context & more]
  "The Clojure `println` function has a race condition. See [this blog post](http://yellerapp.com/posts/2014-12-11-14-race-condition-in-clojure-println.html) for more information."
  (.write context (str (clojure.string/join " " more) "\n")))

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
         (safe-println *out* "info |" message)
         (safe-println *out* "info |" message attr-str))
       (if (= "" attr-str)
         (safe-println *out* "at=info" (format "%s=\"%s\"" "msg" message))
         (safe-println *out* "at=info" (format "%s=\"%s\"" "msg" message) attr-str))))))

(defn error
  ([config message]
   (error config message {}))
  ([config message attrs]
   (let [attr-str (clojure.string/join " " (map format-key-value-pairs attrs))]
     (if (:dev-mode config)
       (if (= "" attr-str)
         (safe-println *err* "error |" message)
         (safe-println *err* "error |" message attr-str))
       (if (= "" attr-str)
         (safe-println *err* "at=error" (format "%s=\"%s\"" "msg" message))
         (safe-println *err* "at=error" (format "%s=\"%s\"" "msg" message) attr-str))))))
