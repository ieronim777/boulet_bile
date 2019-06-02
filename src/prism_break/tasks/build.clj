(ns prism-break.tasks.build
  (:require [clojure.java.io :as io]
            [prism-break.data.svg :as svg]
            [prism-break.utils :refer [res]]
            [prism-break.tasks.hash-path :refer [hash-path]]
            [prism-break.views.css :as css])
  (:import (java.io File)
           (org.apache.commons.io FileUtils)))

(defn ->fs
  "Writes a file tree represented by m to f.

  Directories are maps, where keys are filenames and values are either
  directories represented by `java.io.File` directories or nested maps,
  or files represented by anything coercable to `java.io.InputStream`
  (see `clojure.java.io/input-stream` for coercions)."
  [m f]
  (.mkdir (io/file f))
  (doseq [[k v] m]
    (let [dest (io/file f k)]
      (cond
        (and (instance? File v)
             (.isDirectory v))
        (->fs (zipmap (.list v) (.listFiles v))
              (.getPath dest))

        (map? v)
        (->fs v (.getPath dest))

        :else
        (io/copy v dest)))))

(def logo
  (io/file "logo.svg"))

(def static
  {"apple-touch-icon.png" (svg/->png logo 152)
   "favicon.png" (svg/->png logo 64)
   "icons" (res "icons")
   "style.css" css/stylesheet})

(defn delete-dir [path]
  (-> path io/file FileUtils/deleteDirectory))

(defn build []
  (let [output "public"]
    (delete-dir output)
    (->fs static output)))

(defn -main []
  (println (hash-path (build))))
