(ns prism-break.tools.svg
  (:require [clojure.java.io :as io])
  (:import (org.apache.batik.transcoder TranscoderInput TranscoderOutput)
           (org.apache.batik.transcoder.image PNGTranscoder)))

(defn ->png
  "Render SVG to PNG. Returns a byte array." 
  [svg width]
  (let [input (io/input-stream svg)
        output (java.io.ByteArrayOutputStream.)]
    (doto (PNGTranscoder.)
      (.addTranscodingHint PNGTranscoder/KEY_WIDTH (float width))
      (.transcode
       (TranscoderInput. input)
       (TranscoderOutput. output)))
    (.toByteArray output)))
