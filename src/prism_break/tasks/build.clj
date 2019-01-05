(ns prism-break.tasks.build
  (:import org.apache.commons.io.FileUtils)
  (:require [clojure.java.io :as io]
            [prism-break.io.fs :as fs]
            [prism-break.tasks.hash-path :refer [hash-path]]
            [prism-break.tools.svg :as svg]
            [prism-break.views.css :as css]))

(def logo
  (io/resource "icons/prism-break.svg"))

(def static
  {"apple-touch-icon.png" (svg/->png logo 152)
   "favicon.png" (svg/->png logo 64)
   "icons" (fs/mount (io/file (io/resource "icons")))
   "style.css" css/stylesheet})

(defn delete-dir [path]
  (-> path io/file FileUtils/deleteDirectory))

(defn build []
  (doto "out"
    (delete-dir)
    (fs/write static)))

(defn -main []
  (println (hash-path (build))))
