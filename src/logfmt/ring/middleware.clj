(ns logfmt.ring.middleware
  (:require [logfmt.core :refer [info]])
  (:import (java.util UUID)))

(defn wrap-logger
  [handler]
  (fn [request]
    (let [start (System/currentTimeMillis)
          request-id (get-in request [:headers "x-request-id"] (UUID/randomUUID))
          method (clojure.string/upper-case (name (:request-method request)))
          path (str (:uri request)
                    (if-let [query-string (:query-string request)]
                      (str "?" query-string)))]
      (info "Starting Ring request"
            {:method method
             :path path
             :params (:params request)
             :request-id request-id})
      (let [response (handler request)]
        (info "Finished Ring request"
              {:method method
               :path path
               :status (:status response)
               :duration (str (- (System/currentTimeMillis) start) "ms")
               :request-id request-id})
        response))))
