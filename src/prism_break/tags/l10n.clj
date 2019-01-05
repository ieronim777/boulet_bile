(ns prism-break.tags.l10n
  "Localizing reader that consults PO files based on requested locale.

  Locale codes are IANA language tags:
  https://www.w3.org/International/articles/language-tags/

  If requested locale does not contain a translation, locales in `fallbacks`
  are consulted next, if any. For example, zh-Hant (Traditional Chinese)
  locale might consult zh-Hans (Simplified Chinese).

  If nothing else is available, original untranslated string is returned.
 
  Additionally, all strings that have been seen during process lifetime are
  collected into `registry` atom. That can then be used to create a POT file:
  see `prism-break.tasks.make-pot` module."
  (:refer-clojure :exclude [read])
  (:require [clojure.java.io :as io]
            [prism-break.data.po :as po]))

(def registry (atom #{}))

(def ^:dynamic *locale* 'en)

(defn ^:private locale [id]
  (some->> id name (format "po/%s.po") io/resource slurp po/read-string))

(def ^:private fallbacks
  "Keys are locales for which there are better fallbacks than the original
  string, values are better fallbacks in order of descending preference."
  '{zh-Hans [zh-Hant]
    zh-Hant [zh-Hans]})

(defn ^:private fallbacked-locale [id]
  (apply merge (map locale (concat [id] (fallbacks id)))))

(defn read [s]
  (let [locale (fallbacked-locale *locale*)]
    (swap! registry conj s)
    (get locale s s)))
