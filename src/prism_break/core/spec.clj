(ns prism-break.core.spec
  "
  Spec API is subject to change, see 'Maybe Not' talk:
  https://youtu.be/YR5WdGrpoug"
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [prism-break.core.eval :as eval]
            [prism-break.core.spec.application :as application]
            [prism-break.core.spec.locale :as locale]
            [prism-break.core.spec.platform :as platform]
            [prism-break.core.spec.resolver :as resolver]
            [prism-break.core.spec.standard :as standard]
            [prism-break.core.spec.target :as target]
            [prism-break.data.edn :as edn])
  (:import (org.apache.commons.validator.routines UrlValidator)))

(s/def ::text
  (s/and string? (complement str/blank?)))

(s/def ::description ::text)
(s/def ::name ::text)

(s/def ::+ set?)
(s/def ::- set?)

(s/def ::diff
  (s/keys :req-un #{::+ ::-}))

(s/def ::label
  (s/keys :req-un #{::description
                    ::name}))

(s/def ::labels
  (eval/set-of ::label))

;; TODO
(s/def ::license/expr)

(s/def ::license/free? boolean?)

(s/def ::license
  (s/keys :req-un #{::license/free?}
          :opt-un #{::license/form}))

(s/def ::locale/fallbacks ::locales)

(s/def ::locale
  (s/keys :req-un #{::name}
          :opt-un #{::locale/fallbacks}))

(s/def ::locales
  (eval/set-of ::locale))

(s/def ::page
  (s/keys :req-un #{::name}))

(def url-validator
  (UrlValidator. (into-array String #{"http" "https"})))

(s/def ::url
  #(.isValid url-validator %))

(s/def ::pages->url
  (eval/map-of ::page ::url))

(s/def ::resolver/fallbacks
  (eval/set-of ::resolver))

(s/def ::resolver
  (s/keys :opt-un #{::resolver/fallbacks}))

(s/def ::project
  (s/keys :req-un #{::description
                    ::name}
          :opt-un #{::labels
                    ::license
                    ::locales
                    ::pages->url}))

(s/def ::platform/category
  (s/keys :req-un #{::name}))

(s/def ::platform/categories
  (eval/set-of ::platform/category))

(s/def ::platform
  (s/and ::project
         (s/keys :req-un #{::platform/categories})))

(s/def ::application/category
  (s/keys :req-un #{::name}))

(s/def ::target/category
  (eval/of ::platform/category))

(s/def ::target/platform
  (eval/of ::platform))

(s/def ::target
  (s/keys :req-un #{::target/category
                    ::target/platform}))

(s/def ::application/categories->targets
  (eval/map-of ::application/category (eval/set-of ::platform/target)))

(s/def ::application
  (s/and ::project
         (s/keys :req-un #{::application/categories->targets})))
