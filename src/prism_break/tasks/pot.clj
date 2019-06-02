(ns prism-break.tasks.pot
  "Extracts strings into gettext PO template file."
  (:require [prism-break.data.po :as po]
            [prism-break.tags.l10n :as l10n]))

(defn -main []
  (spit "resources/po/template.pot"
        (po/write-string (zipmap @l10n/registry (repeat "")))))
