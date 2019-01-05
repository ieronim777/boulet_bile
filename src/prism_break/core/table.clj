(ns prism-break.core.table
  "Reactive, spreadsheet-like data view."
  (:require [clojure.java.io :as io]
            [prism-break.data.edn :as edn]
            [prism-break.tags.l10n :refer [*locale*]]
            [prism-break.tags.url :refer [*resolver*]]))

(def cache
  (atom {}))

(defn context []
  {:locale *locale*
   :resolver *resolver*})

(def registry (atom {}))

(defn resource [path]
  {:args []
   :fn #(edn/read (io/resource "hello.edn"))
   :when (fn [ch spec])})

(comment
  (assoc-in {} [::application (impurities)]
            {'duckduckgo {:name "DuckDuckGo"}})

  (def cache
    (atom {::application {{:locale 'en
                           :resolver 'icann}
                          {'duckduckgo {:name "DuckDuckGo"}}}}))

  (def cache
    (atom {{:key ::application
            :locale 'en
            :resolver 'icann} {}}))

  (c/def ::locale
    {:args []
     :fn #(+ 1 1)
     :when (fn [ch spec])}
     
    (c/resource "po/ru.po"))

  (c/def ::application
    (c/resource "hello.edn"))

  (c/def ::application
    {:args []
     :fn #(edn/read (io/resource "hello.edn"))
     :when (fn [ch spec]
             (fs-notify ch spec "hello.edn"))}))
