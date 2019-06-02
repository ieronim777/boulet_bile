(ns prism-break.core.db.spec
  "Spec for evaluation and validation of PRISM Break DB data structures.

  Mind that clojure.spec API is subject to change, see 'Maybe Not' talk:
  https://youtu.be/YR5WdGrpoug"
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.string :refer [blank?]]
            [prism-break.core.db.eval :as eval]
            [prism-break.data.edn :as edn]
            [prism-break.tags.l10n :refer [*locale*]]
            [prism-break.tags.spdx :as spdx]
            [prism-break.tags.url :refer [*resolver*]])
  (:import (org.apache.commons.validator.routines UrlValidator)))

(defn db [k])

(defn ^:private asterisk
  "Given a DB map, assoc * set that evaluates to all entries."
  [db])

(def ^:private res
  (comp io/file io/resource str))

(defn ^:private sub-ns
  [sym]
  (->> (str *ns* \. sym)
       (symbol)
       (create-ns)
       (alias sym)))

(sub-ns 'resolver)

(s/def ::resolver/alts
  (eval/vec-of ::resolver))

(s/def ::resolver
  (s/keys :opt-un #{::resolver/alts}))

(c/def ::resolver
  (db :resolvers))

(s/def ::text
  (s/and string? (complement blank?)))

(s/def ::description ::text)
(s/def ::name ::text)

(s/def ::label
  (s/keys :req-un #{::description
                    ::name}))

(c/def ::label
  (db :labels))

(s/def ::labels
  (eval/set-of ::label))

(sub-ns 'locale)

(s/def ::locale/alts
  (eval/vec-of ::locales))

(s/def ::locale
  (s/keys :req-un #{::name}
          :opt-un #{::locale/alts}))

(c/def ::locale
  (db :locales))

(s/def ::locales
  (eval/set-of ::locale))

(s/def ::page
  (s/keys :req-un #{::name}))

(c/def ::page
  (db :pages))

(def url-validator
  (UrlValidator. (into-array String #{"http" "https"})))

(s/def ::url
  #(.isValid url-validator %))

(s/def ::pages->url
  (eval/map-of ::page ::url))

(s/def ::project
  (s/keys :req-un #{::description
                    ::name}
          :opt-un #{::labels
                    ::spdx/license
                    ::locales
                    ::pages->url}))

(sub-ns 'platform)

(s/def ::platform/category
  (s/keys :req-un #{::name}))

(c/def ::platform/category
  (db :categories/platforms))

(s/def ::platform/categories
  (eval/set-of ::platform/category))

(s/def ::platform
  (s/and ::project
         (s/keys :req-un #{::platform/categories})))

(c/def ::platform
  (db :projects/platforms))

(sub-ns 'target)

(s/def ::target/category
  (eval/of ::platform/category))

(s/def ::target/platform
  (eval/of ::platform))

(s/def ::target
  (s/keys :req-un #{::target/category
                    ::target/platform}))

(c/def ::target
  {:args #{::platform/category ::platform}
   :deps #{}
   :fn (fn [{:keys [::platform/category ::platform]}]
         (intersect category platform))})

(sub-ns 'application)

(s/def ::application/category
  (s/keys :req-un #{::name}))

(c/def ::application/category
  (db :categories/applications))

(s/def ::application/categories->targets
  (eval/map-of ::application/category (eval/set-of ::platform/target)))

(s/def ::application
  (s/and ::project
         (s/keys :req-un #{::application/categories->targets})))

(c/def ::application
  (db :projects/applications))
