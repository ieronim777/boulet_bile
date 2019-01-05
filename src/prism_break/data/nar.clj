(ns prism-break.data.nar
  "Nix archive format writer: https://git.io/fhUlj

  Hashes are used to ensure reproducible builds, so that when you build on
  several different systems, you always get the exact same result.

  It's only possible to hash bytes. There's no standard scheme for hashing
  directories, because there's no standard way to represent one in bytes.

  NAR is a good representation because it's designed to be reproducible, it's
  easy to implement, it's minimal, and there is a third-party tool `nix
  hash-path` that can verify the hash independent of this build system.

  NAR targets Unix systems. On Windows, every file is executable, and paths in
  symlinks differ from Unix convention. This module ignores executable bits and
  treats symlinks as if they were regular files, so that Windows users get the
  same output as everyone else.

  This module's representation will only match Nix's if given path doesn't
  contain any executables or symlinks. This is always true for `public/`,
  because prism-break.io.fs can only create regular files and directories."
  (:import (java.io File OutputStream ByteArrayOutputStream)
           (java.nio ByteBuffer ByteOrder))
  (:require [clojure.java.io :as io]))

(set! *warn-on-reflection* true)

(def ^:private magic "nix-archive-1")

(defn ^:private pad [len n]
  (mod (- n (mod len n)) n))

(defn ^:private uint64-le [n]
  (let [u64 (Long/parseUnsignedLong (str n))
        buf (doto (ByteBuffer/allocate 8)
              (.order ByteOrder/LITTLE_ENDIAN)
              (.putLong u64))] 
    (.array buf)))

(defprotocol ^:private Length
  (length [x]))

(extend-protocol Length
  File
  (length [f] (.length f))
  String
  (length [s] (.length s)))

(defn ^:private write-datum [x ^OutputStream out]
  (let [len (length x)]
    (doseq [chunk [(uint64-le len) x (byte-array (pad len 8))]]
      (io/copy chunk out))))

(defn ^:private write-data [xs ^OutputStream out]
  (doseq [x xs]
    (write-datum x out)))

(defn ^:private write-regular-file [^File f ^OutputStream out]
  (write-data ["type" "regular" "contents" f] out))

(declare write-file)

(defn ^:private write-directory [^File f ^OutputStream out]
  (write-data ["type" "directory"] out)
  (doseq [^File child (sort (.listFiles f))]
    (write-data ["entry" "(" "name" (.getName child) "node"] out)
    (write-file child out)
    (write-data [")"] out)))

(defn ^:private write-file [^File f ^OutputStream out]
  (write-data ["("] out)
  (if (.isDirectory f)
    (write-directory f out)
    (write-regular-file f out))
  (write-data [")"] out))

(defn write
  "Writes NAR representation of java.io.File to OutputStream."
  [f ^OutputStream out]
  (write-data [magic] out)
  (write-file f out))

(defn write-bytes
  "Returns NAR representation of java.io.File as a byte array."
  [f]
  (let [out (ByteArrayOutputStream.)]
    (write f out)
    (.toByteArray out)))
