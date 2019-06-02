(ns prism-break.tags.spdx
  "SPDX tag reader that validates syntax, free software status,
  and provides license metadata."
  (:refer-clojure :exclude [read])
  (:require [clojure.spec.alpha :as s])
  (:import (org.spdx.rdfparser.license ConjunctiveLicenseSet
                                       DisjunctiveLicenseSet
                                       License
                                       LicenseException
                                       LicenseInfoFactory
                                       WithExceptionOperator)))

(set! *warn-on-reflection* true)

(def ^:private extra-free-licenses
  "Some licenses are not (yet) approved by FSF. Or maybe we just disagree.
  To allow projects under one of such licenses to be recommended on the site
  *without* non-free label, add it here.
   
  Please don't abuse this feature. We will never, ever recommend users to run
  unsandboxed binaries without having access to corresponding source code. We
  might, in rare circumstances, temporarily recommend a source-available
  project or a proprietary service, but such licenses must not go in here."
  #{"0BSD"})

(defn ^:private prefer-upstream
  "Ordering function that prefers upstream licenses to SPDX license registry.
  Upstream usually provides more context on what their license is about."
  [s]
  (re-find #"^https?://(www\.)?opensource\.org" s))

(defprotocol Expression
  (expr [this] "Emits abstract syntax tree for this SPDX expression.")
  (free? [this] "Checks if this expression is compatible with free software."))

(extend-protocol Expression
  ConjunctiveLicenseSet
  (expr [this]
    (cons :and (map expr (.getMembers this))))
  (free? [this]
    (every? free? (.getMembers this)))
  DisjunctiveLicenseSet
  (expr [this]
    (cons :or (map expr (.getMembers this))))
  (free? [this]
    (some free? (.getMembers this)))
  License
  (expr [this]
    {:name (str this)
     :url (first (sort-by prefer-upstream (.getSeeAlso this)))})
  (free? [this]
    (or (.isFsfLibre this)
        (contains? extra-free-licenses (str this))))
  LicenseException
  (expr [this]
    (let [id (.getLicenseExceptionId this)]
      {:name id
       :url (str "https://spdx.org/licenses/" id ".html")}))
  WithExceptionOperator
  (expr [this]
    `(:with ~(expr (.getLicense this))
            ~(expr (.getException this))))
  (free? [this]
    (free? (.getLicense this))))

(defn ^:private string->spdx [s]
  (LicenseInfoFactory/parseSPDXLicenseString s))

(defn read [s]
  (let [spdx (string->spdx s)]
    (comment
      (doseq [warning (.verify spdx)]
        (throw (ex-info warning {}))))
    {:expr (expr spdx)
     :free? (free? spdx)}))

(s/def ::expr any?) ; TODO!

(s/def ::free? boolean?)

(s/def ::license
  (s/keys :req-un #{::free?}
          :opt-un #{::expr}))
