(ns prism-break.core.db.eval
  "À la carte symbolic evaluation mechanism."
  (:require [clojure.spec.alpha :as s]))

;; of validates that symbol is reachable, conform appends metadata to
;; the symbol with result of its evaluation; invalid when points to Diffs or sets
(defn of [spec] symbol?)

;; map-of conform resolves and flattens recursive symbol Diffs and sets
;; in keys, more specific keys have value precedence
(defn map-of [kspec vspec]
  (s/map-of symbol? vspec))

;; set-of conform resolves and flattens recursive symbol Diffs and sets
;; all endpoint symbols also have metadata with result of their evaluation
(defn set-of [spec]
  (s/coll-of symbol?))

(defn vec-of [spec]
  (s/coll-of symbol?))

;; all operations are recursive, you end up with a metadata tree on each symbol
;; and that metadata tree can include other symbols with their own metadata trees

;; ^{::result {}} 'a
