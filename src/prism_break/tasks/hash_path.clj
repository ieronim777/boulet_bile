(ns prism-break.tasks.hash-path
  "Prints SHA-512 hash of NAR representation of the given path.

  Output should be identical to `nix hash-path <path>`, with some caveats.
  See `prism-break.data.nar` docs."
  (:import java.security.MessageDigest
           javax.xml.bind.DatatypeConverter) 
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [prism-break.data.nar :as nar]))

(defn ^:private hex [in]
  (str/lower-case (DatatypeConverter/printHexBinary in)))

(defn ^:private digest [in]
  (.digest (MessageDigest/getInstance "SHA-512") in))

(defn hash-path [path]
  (-> path io/file nar/write-bytes digest hex))

(defn -main [path]
  (println (hash-path path)))
