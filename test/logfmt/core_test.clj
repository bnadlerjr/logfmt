(ns logfmt.core-test
  (:require [clojure.test :refer :all]
            [logfmt.core :refer :all]))

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
