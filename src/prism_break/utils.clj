(ns prism-break.utils
  (:require [clojure.java.io :as io]))

(defn res [path]
  (io/file (io/resource (str "prism_break/" path))))
