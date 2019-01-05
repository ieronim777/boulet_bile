(ns prism-break.tasks.compress-path
  "Destructively compresses files in path. Requires Brotli/Zopfli."
  (:import org.apache.commons.io.FilenameUtils) 
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))

(defn brotli [path]
  (sh "brotli" "-f" path))

(defn zopfli [path]
  (sh "zopflipng" "-y" path path))

(def compressors
  {"css" brotli
   "html" brotli
   "js" brotli
   "png" zopfli
   "svg" brotli})

(defn compress [f]
  (if (.isDirectory f)
    (run! compress (.listFiles f))
    (let [path (str f)]
      (when-let [z (compressors (FilenameUtils/getExtension path))]
        (z path)))))

(defn compress-path [path]
  (compress (io/file path)))

(def -main compress-path)
