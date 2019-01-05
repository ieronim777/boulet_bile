(ns prism-break.tasks.build-release
  (:require [prism-break.tasks.build :refer [build]]
            [prism-break.tasks.compress-path :refer [compress-path]]
            [prism-break.tasks.hash-path :refer [hash-path]]))

(defn -main []
  (let [public (build)]
    (println "before compression:" (hash-path public))
    (compress-path public)
    (println "after:" (hash-path public))))
