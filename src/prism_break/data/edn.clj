(ns prism-break.data.edn
  "EDN reader that is aware of PRISM Break specific tagged literals specified
  in prism-break.tags namespace. Mind that read result depends on l10n/*locale*
  and url/*resolver* dynamic vars."
  (:refer-clojure :exclude [read read-string])
  (:require [clojure.edn :as edn]
            [prism-break.tags.diff :as diff]
            [prism-break.tags.l10n :as l10n]
            [prism-break.tags.l10n-url :as l10n-url]
            [prism-break.tags.spdx :as spdx]
            [prism-break.tags.url :as url]))

(def ^:private readers
  {'prism-break/diff diff/read
   'prism-break/l10n l10n/read
   'prism-break/l10n-url l10n-url/read
   'prism-break/spdx spdx/read
   'prism-break/url url/read})

(def read 
  (partial edn/read {:readers readers}))

(def read-string
  (partial edn/read-string {:readers readers}))
