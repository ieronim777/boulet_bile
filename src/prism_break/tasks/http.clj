(ns prism-break.tasks.http
  "Starts an HTTP server that reloads modified namespaces on each request."
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]))

(defn handler [request]
  {:status 200
   :headers {}
   :body ""})

(defn -main []
  (run-jetty (wrap-reload #'handler) {:port 9090}))
