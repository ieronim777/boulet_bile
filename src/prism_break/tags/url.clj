(ns prism-break.tags.url
  "Validating URL reader with dispatch to multiple name addressing systems.

  The intent behind this module is similar to `prism-break.tags.l10n`: dispatch
  URL representation based on user's preferred name addressing system, provide
  meaningful fallbacks if that is not available."
  (:refer-clojure :exclude [read]))

(def registry (atom {}))

(def shortcut-resolver 'icann)

(def ^:dynamic *resolver* 'icann)

;; TODO: remove
(def fallbacks {})

(def projections
  {'bit/onion #(some-> % (get 'bit) (str ".onion"))})

(defn read [data]
  (let [data (if (map? data) data {'icann data})
        selector (apply juxt *resolver* (fallbacks *resolver*))]
    (first (drop-while nil? (selector data)))))
