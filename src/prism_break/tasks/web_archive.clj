(ns prism-break.tasks.web-archive
  "Probes all ICANN URLs in the project.

  If URL is reachable, Web Archive snapshots the URL. If it's not,
  the latest available snapshot is printed to standard output."
  (:require [prism-break.tags.url :as url]
            [yegortimoshenko.io.http :as http]))

(def user-agent
  "PRISM Break <https://prism-break.org>")

(defn snapshot [url]
  (http/request {:headers {:user-agent [user-agent]}
                 :method :POST}
                (str "https://web.archive.org/save/" url)))

(defn -main []
  (doseq [url (:icann @url/registry)]
    (snapshot url)))
