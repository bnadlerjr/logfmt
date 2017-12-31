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

(defn- log
  ([level config message]
   (log level config message {}))
  ([level config message attrs]
   (let [attr-str (clojure.string/join " " (map format-key-value-pairs attrs))
         context (determine-output-context level)]
     (if (:dev-mode config)
       (if (= "" attr-str)
         (safe-println context (name level) "|" message)
         (safe-println context (name level) "|" message attr-str))
       (if (= "" attr-str)
         (safe-println context (format "%s=%s" "at" (name level)) (format "%s=\"%s\"" "msg" message))
         (safe-println context (format "%s=%s" "at" (name level)) (format "%s=\"%s\"" "msg" message) attr-str))))))

(def info (partial log :info))
(def error (partial log :error))
