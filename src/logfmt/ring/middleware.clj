(ns logfmt.ring.middleware
  "Ring middleware helpers."
  (:require [logfmt.core :refer [info]])
  (:import (java.util UUID)))

(defn wrap-logger
  "Wraps a Ring request outputting two logging messages: one for the request
  and another for the response. Includes useful attributes such as `path`,
  `method`, `status`, etc.

  Also adds a `request-id` attribute via the `x-request-id` header to both
  messages, so that messages can be matched. If the `x-request-id` header is
  not available, one will be generated.

  Heroku will add a `x-request-id` header to all requests automatically, so
  the generation feature is mainly used when the Ring application is deployed
  elsewhere (i.e. locally)."
  [handler]
  (fn [req]
    (let [start (System/currentTimeMillis)
          request-id (get-in req [:headers "x-request-id"] (UUID/randomUUID))
          method (clojure.string/upper-case (name (:request-method req)))
          path (str (:uri req)
                    (if-let [query-string (:query-string req)]
                      (str "?" query-string)))]
      (info (format "Started %s '%s'" method path)
            {:method method
             :path path
             :params (:params req)
             :request-id request-id})
      (let [response (handler req)
            status (:status response)
            duration (str (- (System/currentTimeMillis) start) "ms")]
        (info (format "Completed %s in %s" status duration)
              {:method method
               :path path
               :status status
               :duration duration
               :request-id request-id})
        response))))
