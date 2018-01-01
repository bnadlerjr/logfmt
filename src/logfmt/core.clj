(ns logfmt.core)

(def dev-mode false)

(defn set-dev-mode!
  [value]
  (alter-var-root #'dev-mode (constantly value)))

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

(defn- full-message
  [level message attrs]
  (let [level-str (name level)
        attr-str (clojure.string/join " " (map format-key-value-pairs attrs))]
    (if dev-mode
      (clojure.string/trimr
        (format "\n%s | %s %s" level-str message attr-str))
      (clojure.string/trim
        (format "at=%s msg=\"%s\" %s" level-str message attr-str)))))

(defn- log
  ([level message]
   (log level message {}))
  ([level message attrs]
   (let [full-message (full-message level message attrs)]
     (.println System/out full-message))))

(def info (partial log :info))
(def error (partial log :error))
