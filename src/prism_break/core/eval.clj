(ns prism-break.core.eval
  (:require [clojure.spec.alpha :as s]))

;; eval-of validates that symbol is reachable, conform appends metadata to
;; the symbol with result of its evaluation; invalid when points to Diffs or sets

;; eval-set-of conform resolves and flattens recursive symbol Diffs and sets
;; all endpoint symbols also have metadata with result of their evaluation

;; eval-map-of conform resolves and flattens recursive symbol Diffs and sets
;; in keys, more specific keys have value precedence

;; all operations are recursive, you end up with a metadata tree on each symbol
;; and that metadata tree can include other symbols with their own metadata trees

(defn of [spec] symbol?)

(defn map-of [kspec vspec]
  (s/map-of symbol? vspec))

(defn set-of [spec]
  (s/coll-of symbol?))

(comment
  (def specs->src
   {::application (io/resource "db/projects/applications.edn")
    ::application/category (io/resource "db/categories/applications.edn")
    ::label (io/resource "db/labels.edn")
    ::locale (io/resource "db/locales.edn")
    ::page (io/resource "db/pages.edn")
    ::platform (io/resource "db/projects/platforms.edn")
    ::platform/category (io/resource "db/categories/platforms.edn")
    ::resolver (io/resource "db/resolvers.edn")
    ::standard (io/resource "db/projects/standards.edn")
    ::standard/category (io/resource "db/categories/standards.edn")}))
