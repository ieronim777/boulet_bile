(ns prism-break.io.fs
  "Persistent virtual filesystem.

  Directories are maps, where keys are filenames and values are either other
  maps (nested directories) or anything coercable to `java.io.InputStream`
  (regular files, see `clojure.java.io/input-stream` for coercions)."
  (:require [clojure.java.io :as io]))

(defn mount
  "Mount java.io.File as lazy filesystem."
  [f]
  (if (.isFile f)
    f
    (let [files (.listFiles f)
          names (map #(.getName %) files)]
      (zipmap names (map mount files)))))

(defn write
  "Write filesystem to disk."
  [path fs]
  (.mkdir (io/file path))
  (doseq [[k v] fs]
    (let [f (io/file path k)]
      (if (map? v)
        (write (.getPath f) v)
        (io/copy v f)))))
