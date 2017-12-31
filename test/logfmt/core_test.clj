(ns logfmt.core-test
  (:require [clojure.test :refer :all]
            [logfmt.core :refer :all]))

(defmacro with-err-str
  "Same behavior as `with-out-str` except this macro binds to *err*
  instead of *out*."
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*err* s#]
       ~@body
       (str s#))))

(deftest info-test
  (testing "info message in dev mode"
    (let [config {:dev-mode true}]
      (is (= "info | Some message.\n"
             (with-out-str (info config "Some message."))))

      (is (= "info | A message. duration=10ms\n"
             (with-out-str (info config "A message." {:duration "10ms"}))))

      (is (= "info | My message. duration=12ms method=GET\n"
             (with-out-str (info config "My message." {:duration "12ms"
                                                       :method "GET"}))))

      (is (= "info | Message w/ string attribute. method=POST path=\"/foo\"\n"
             (with-out-str (info config "Message w/ string attribute." {:method "POST"
                                                                        :path "/foo"}))))

      (is (= "info | Message w/ nested map attributes. method=GET params={:name \"jdoe\"}\n"
             (with-out-str (info config "Message w/ nested map attributes." {:method "GET"
                                                                             :params {:name "jdoe"}}))))))

  (testing "info message not in dev mode"
    (let [config {:dev-mode false}]
      (is (= "at=info msg=\"Some message.\"\n"
             (with-out-str (info config "Some message."))))

      (is (= "at=info msg=\"A message.\" duration=10ms\n"
             (with-out-str (info config "A message." {:duration "10ms"}))))

      (is (= "at=info msg=\"My message.\" duration=12ms method=GET\n"
             (with-out-str (info config "My message." {:duration "12ms"
                                                       :method "GET"}))))

      (is (= "at=info msg=\"Message w/ string attribute.\" method=POST path=\"/foo\"\n"
             (with-out-str (info config "Message w/ string attribute." {:method "POST"
                                                                        :path "/foo"}))))

      (is (= "at=info msg=\"Message w/ nested map attributes.\" method=GET params={:name \"jdoe\"}\n"
             (with-out-str (info config "Message w/ nested map attributes." {:method "GET"
                                                                             :params {:name "jdoe"}})))))))

(deftest error-test
  (testing "error message in dev mode"
    (let [config {:dev-mode true}]
      (is (= "error | Some message.\n"
             (with-err-str (error config "Some message."))))

      (is (= "error | A message. duration=10ms\n"
             (with-err-str (error config "A message." {:duration "10ms"}))))

      (is (= "error | My message. duration=12ms method=GET\n"
             (with-err-str (error config "My message." {:duration "12ms"
                                                        :method "GET"}))))

      (is (= "error | Message w/ string attribute. method=POST path=\"/foo\"\n"
             (with-err-str (error config "Message w/ string attribute." {:method "POST"
                                                                         :path "/foo"}))))

      (is (= "error | Message w/ nested map attributes. method=GET params={:name \"jdoe\"}\n"
             (with-err-str (error config "Message w/ nested map attributes." {:method "GET"
                                                                              :params {:name "jdoe"}}))))))
  (testing "error message not in dev mode"
    (let [config {:dev-mode false}]
      (is (= "at=error msg=\"Some message.\"\n"
             (with-err-str (error config "Some message."))))

      (is (= "at=error msg=\"A message.\" duration=10ms\n"
             (with-err-str (error config "A message." {:duration "10ms"}))))

      (is (= "at=error msg=\"My message.\" duration=12ms method=GET\n"
             (with-err-str (error config "My message." {:duration "12ms"
                                                        :method "GET"}))))

      (is (= "at=error msg=\"Message w/ string attribute.\" method=POST path=\"/foo\"\n"
             (with-err-str (error config "Message w/ string attribute." {:method "POST"
                                                                         :path "/foo"}))))

      (is (= "at=error msg=\"Message w/ nested map attributes.\" method=GET params={:name \"jdoe\"}\n"
             (with-err-str (error config "Message w/ nested map attributes." {:method "GET"
                                                                              :params {:name "jdoe"}})))))))
