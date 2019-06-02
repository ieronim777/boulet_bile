(ns prism-break.tags.diff
  (:refer-clojure :exclude [read])
  (:require [clojure.spec.alpha :as s]))

(defrecord Diff [+ -])

(defn Diff? [x]
  (instance? Diff x))

(def read map->Diff)

(s/def ::+ set?)
(s/def ::- set?)

(s/def ::diff
  (s/keys :req-un #{::+ ::-}))
