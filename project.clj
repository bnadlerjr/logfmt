(defproject bnadlerjr/logfmt "0.1.0-SNAPSHOT"
  :description "A clojure library for emitting logfmt."
  :url "https://github.com/bnadlerjr/logfmt"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :aliases {"lint" ["do" ["ancient"] ["kibit"] ["eastwood"]]
            "doc" ["do" ["marg" "-f" "index.html"]]}

  :dependencies [[io.aviso/pretty "0.1.34"]
                 [org.clojure/clojure "1.9.0"]]

  :plugins [[jonase/eastwood "0.2.5"]
            [lein-ancient    "0.6.15"]
            [lein-kibit      "0.1.6"]
            [lein-marginalia "0.9.1"]])
