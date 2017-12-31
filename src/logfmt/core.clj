(ns logfmt.core)

(defn- safe-println [context & more]
  "The Clojure `println` function has a race condition. See [this blog post](http://yellerapp.com/posts/2014-12-11-14-race-condition-in-clojure-println.html) for more information."
  (.write context (str (clojure.string/join " " more) "\n")))

(defn- string-with-slash-or-whitespace?
  [s]
  (and (string? s)
       (re-find #"(\/|\s)" s)))

(defn- format-key-value-pairs
  [[k v]]
  (let [format-str (if (string-with-slash-or-whitespace? v)
                     "%s=\"%s\""
                     "%s=%s")]
    (format format-str (name k) v)))

(defn- determine-output-context
  [level]
  (if (= :info level)
    *out*
    *err*))

(defn- full-message
  [dev-mode level message attrs]
  (let [level-str (name level)
        attr-str (clojure.string/join " " (map format-key-value-pairs attrs))]
    (if dev-mode
      (str level-str " | " message " " attr-str)
      (str (format "at=%s msg=\"%s\" " level-str message) attr-str))))

(defn- log
  ([level config message]
   (log level config message {}))
  ([level config message attrs]
   (let [context (determine-output-context level)
         full-message (full-message (:dev-mode config) level message attrs)]
     (safe-println context (clojure.string/trim full-message)))))

(def info (partial log :info))
(def error (partial log :error))
