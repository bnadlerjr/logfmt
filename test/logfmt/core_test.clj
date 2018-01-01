(ns logfmt.core-test
  (:require [clojure.test :refer :all]
            [io.aviso.ansi :as ansi]
            [logfmt.core :refer :all]))

(import [java.io ByteArrayOutputStream PrintStream])

(defmacro with-system-out-str [& body]
  `(let [out-buffer# (ByteArrayOutputStream.)
         original-out# System/out
         tmp-out# (PrintStream. out-buffer# true "UTF-8")]
     (try
       (System/setOut tmp-out#)
       ~@body
       (finally
         (System/setOut original-out#)))
     (.toString out-buffer# "UTF-8")))

(deftest info-test
  (testing "info message in dev mode"
    (set-dev-mode! true)
    (let [col ansi/cyan
          prefix (str "\n" (col "info") " | ")]
      (is (= (str prefix (col "Some message.") "\n")
             (with-system-out-str (info "Some message."))))

      (is (= (str prefix (col "A message.") " duration=10ms\n")
             (with-system-out-str (info "A message." {:duration "10ms"}))))

      (is (= (str prefix (col "My message.") " duration=12ms method=GET\n")
             (with-system-out-str (info "My message." {:duration "12ms"
                                                       :method "GET"}))))

      (is (= (str prefix (col "Message w/ string attribute.") " method=POST path=\"/foo\"\n")
             (with-system-out-str (info "Message w/ string attribute." {:method "POST"
                                                                        :path "/foo"}))))

      (is (= (str prefix (col "Message w/ nested map attributes.") " method=GET params={:name \"jdoe\"}\n")
             (with-system-out-str (info "Message w/ nested map attributes." {:method "GET"
                                                                             :params {:name "jdoe"}}))))))

  (testing "info message not in dev mode"
    (set-dev-mode! false)
    (is (= "at=info msg=\"Some message.\"\n"
           (with-system-out-str (info "Some message."))))

    (is (= "at=info msg=\"A message.\" duration=10ms\n"
           (with-system-out-str (info "A message." {:duration "10ms"}))))

    (is (= "at=info msg=\"My message.\" duration=12ms method=GET\n"
           (with-system-out-str (info "My message." {:duration "12ms"
                                                     :method "GET"}))))

    (is (= "at=info msg=\"Message w/ string attribute.\" method=POST path=\"/foo\"\n"
           (with-system-out-str (info "Message w/ string attribute." {:method "POST"
                                                                      :path "/foo"}))))

    (is (= "at=info msg=\"Message w/ nested map attributes.\" method=GET params={:name \"jdoe\"}\n"
           (with-system-out-str (info "Message w/ nested map attributes." {:method "GET"
                                                                           :params {:name "jdoe"}}))))))

(deftest error-test
  (testing "error message in dev mode"
    (set-dev-mode! true)
    (let [col ansi/red
          prefix (str "\n" (col "error") " | ")]
      (is (= (str prefix (col "Some message.") "\n")
             (with-system-out-str (error "Some message."))))

      (is (= (str prefix (col "A message.") " duration=10ms\n")
             (with-system-out-str (error "A message." {:duration "10ms"}))))

      (is (= (str prefix (col "My message.") " duration=12ms method=GET\n")
             (with-system-out-str (error "My message." {:duration "12ms"
                                                        :method "GET"}))))

      (is (= (str prefix (col "Message w/ string attribute.") " method=POST path=\"/foo\"\n")
             (with-system-out-str (error "Message w/ string attribute." {:method "POST"
                                                                         :path "/foo"}))))

      (is (= (str prefix (col "Message w/ nested map attributes.") " method=GET params={:name \"jdoe\"}\n")
             (with-system-out-str (error "Message w/ nested map attributes." {:method "GET"
                                                                              :params {:name "jdoe"}}))))))

  (testing "error message not in dev mode"
    (set-dev-mode! false)
    (is (= "at=error msg=\"Some message.\"\n"
           (with-system-out-str (error "Some message."))))

    (is (= "at=error msg=\"A message.\" duration=10ms\n"
           (with-system-out-str (error "A message." {:duration "10ms"}))))

    (is (= "at=error msg=\"My message.\" duration=12ms method=GET\n"
           (with-system-out-str (error "My message." {:duration "12ms"
                                                      :method "GET"}))))

    (is (= "at=error msg=\"Message w/ string attribute.\" method=POST path=\"/foo\"\n"
           (with-system-out-str (error "Message w/ string attribute." {:method "POST"
                                                                       :path "/foo"}))))

    (is (= "at=error msg=\"Message w/ nested map attributes.\" method=GET params={:name \"jdoe\"}\n"
           (with-system-out-str (error "Message w/ nested map attributes." {:method "GET"
                                                                            :params {:name "jdoe"}}))))))
