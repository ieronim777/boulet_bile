(ns prism-break.tags.diff
  (:refer-clojure :exclude [read]))

(defrecord Diff [+ -])

(defn Diff? [x]
  (instance? Diff x))

(def read map->Diff)
