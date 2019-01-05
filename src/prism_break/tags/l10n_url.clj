(ns prism-break.tags.l10n-url
  (:refer-clojure :exclude [read])
  (:require [prism-break.tags.l10n :as l10n]
            [prism-break.tags.url :as url]))

(def read
  (comp l10n/read url/read))
