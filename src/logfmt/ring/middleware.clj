(ns logfmt.ring.middleware
  (:require [logfmt.core :refer [info]]))

(defn wrap-logger
  [handler]
  (fn [request]
    (let [start (System/currentTimeMillis)
          method (clojure.string/upper-case (name (:request-method request)))
          path (str (:uri request)
                    (if-let [query-string (:query-string request)]
                      (str "?" query-string)))]
      (info "Starting Ring request"
            {:method method
             :path path
             :params (:params request)})
      (let [response (handler request)]
        (info "Finished Ring request"
              {:method method
               :path path
               :status (:status response)
               :duration (str (- (System/currentTimeMillis) start) "ms")})
        response))))
