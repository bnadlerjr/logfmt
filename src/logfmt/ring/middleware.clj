(ns logfmt.ring.middleware
  (:require [logfmt.core :refer [info]])
  (:import (java.util UUID)))

(defn wrap-logger
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
