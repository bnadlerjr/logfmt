(ns logfmt.core-test
  (:require [clojure.test :refer :all]
            [logfmt.core :refer :all]))

(deftest info-test
  (testing "info message in dev mode"
    (is (= "info | Some message.\n"
           (with-out-str (info {:dev-mode true} "Some message."))))

    (is (= "info | A message. duration=10ms\n"
           (with-out-str (info {:dev-mode true} "A message." {:duration "10ms"}))))

    (is (= "info | My message. duration=12ms method=GET\n"
           (with-out-str (info {:dev-mode true} "My message." {:duration "12ms"
                                                               :method "GET"}))))

    (is (= "info | Message w/ string attribute. method=POST path=\"/foo\"\n"
           (with-out-str (info {:dev-mode true} "Message w/ string attribute." {:method "POST"
                                                                                :path "/foo"})))))

  (testing "info message not in dev mode"
    (is (= "at=info msg=\"Some message.\"\n"
           (with-out-str (info {:dev-mode false} "Some message."))))

    (is (= "at=info msg=\"A message.\" duration=10ms\n"
           (with-out-str (info {:dev-mode false} "A message." {:duration "10ms"}))))

    (is (= "at=info msg=\"My message.\" duration=12ms method=GET\n"
           (with-out-str (info {:dev-mode false} "My message." {:duration "12ms"
                                                                :method "GET"}))))

    (is (= "at=info msg=\"Message w/ string attribute.\" method=POST path=\"/foo\"\n"
           (with-out-str (info {:dev-mode false} "Message w/ string attribute." {:method "POST"
                                                                                 :path "/foo"}))))))
