(ns prism-break.data.po
  "gettext PO format reader/writer.

  Crowdsourced translation platforms like Weblate, Crowdin, Transifex are
  awesome. They drop the barrier to entry, help peer review translations,
  and improve translation consistency by building up a common glossary.
  
  PO is the best supported translation format on such platforms.

  This module represents PO files as a map where keys are original strings,
  and values are translated strings."
  (:refer-clojure :exclude [read-string])
  (:require [blancas.kern.core :as kern]
            [blancas.kern.lexer :as lex]
            [clojure.string :as str]))

(def po-style
  (assoc lex/basic-def :comment-line "#"))

(def po
  (lex/make-parsers po-style))

(def po-parser
  (let [{:keys [string-lit token trim]} po]
    (kern/bind [_ trim
                _ (token "msgid")
                k string-lit
                _ (token "msgstr")
                v (kern/many string-lit)]
      (kern/return [k (str/join v)]))))

(defn read-string [s]
  (into (array-map)
        (kern/value (kern/many po-parser) s)))

(def po-format
  "msgid \"%s\"
msgstr \"%s\"

")

(defn write-string [m]
  (str/join (map (partial apply format po-format) m)))
