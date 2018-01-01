(ns logfmt.core
  "Utility library for emitting messages to `STDOUT` in
  [logfmt](https://brandur.org/logfmt). Intended to be used for projects that
  will be deployed to Heroku."
  (:require [io.aviso.ansi :as ansi]))

(def ^{:doc "Indicates whether development mode is active."}
  dev-mode false)

(defn set-dev-mode!
  "Updates `dev-mode`."
  [value]
  (alter-var-root #'dev-mode (constantly value)))

(defn- string-with-slash-or-whitespace?
  [s]
  (and (string? s)
       (re-find #"(\/|\s)" s)))

(defn- format-key-value-pairs
  "Formats a key / value pair as `k=v`. If the value contains a slash or
  whitespace, it is escaped with double quotes."
  [[k v]]
  (let [format-str (if (string-with-slash-or-whitespace? v)
                     "%s=\"%s\""
                     "%s=%s")]
    (format format-str (name k) v)))

(defn- determine-color-fn
  "When `dev-mode` is true, messages are colorized according to `level`."
  [level]
  (cond
    (= :info level) ansi/cyan
    (= :error level) ansi/red
    :else identity))

(defn- full-message
  "Build the message based on `level`, `text` and `attrs`. When `dev-mode` is
  `true`, messages are output in a slightly more readable format:

  ```<level> | <text> <attr_key>=<attr-value> ...```

  When `dev-mode` is `false`, `level` and `message` are converted to key /
  value pairs and formatted just like `attrs`:

  ```at=<level> msg=<text> <attr_key>=<attr_value> ...```
  "
  [level text attrs]
  (let [level-str (name level)
        color (determine-color-fn level)
        attr-str (clojure.string/join " " (map format-key-value-pairs attrs))]
    (if dev-mode
      (clojure.string/trimr
        (format "\n%s | %s %s" (color level-str) (color text) attr-str))
      (clojure.string/trim
        (format "at=%s msg=\"%s\" %s" level-str text attr-str)))))

(defn- log
  "Generic log function. For JVM projects, Heroku recommends using
  `System.out.println`, so we use that as opposed to `*out*` or Clojure's
  `println`."
  ([level message]
   (log level message {}))
  ([level message attrs]
   (.println System/out (full-message level message attrs))))

(def ^{:doc "Log information level messages."}
  info (partial log :info))

(def ^{:doc "Log error level messages."}
  error (partial log :error))
