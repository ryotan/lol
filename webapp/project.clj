(defproject webapp "0.1.1-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2985"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.omcljs/om "0.8.8"]
                 [ring "1.3.2"]
                 [compojure "1.3.2"]
                 [com.datomic/datomic-free "0.9.5130" :exclusions [joda-time]]
                 [sablono "0.3.4"]]

  :plugins [[lein-cljsbuild "1.0.5"]]

  :source-paths ["src/clj" "src/cljs"]
  :resource-paths ["resources"]

  :hooks [leiningen.cljsbuild]

  :cljsbuild {:builds
              {:client
               {:source-paths ["src/cljs"]
                :compiler     {:output-to     "resources/public/js/webapp.min.js"
                               :main          webapp.core
                               :optimizations :advanced
                               :pretty-print  false}}}}

  :profiles {:dev {:dependencies [[figwheel "0.2.5"]]
                   :plugins      [[lein-figwheel "0.2.5"]]
                   :source-paths ["dev_src/clj" "dev_src/cljs"]
                   :cljsbuild    {:builds
                                  {:figwheel
                                   {:source-paths ["src/cljs" "dev_src/cljs"]
                                    :compiler     {:output-to            "resources/public/js/webapp.min.js"
                                                   :output-dir           "resources/public/js/out"
                                                   :main                 webapp.dev
                                                   :optimizations        :none
                                                   :asset-path           "js/out"
                                                   :source-map           "resources/public/js/webapp.min.js.map"
                                                   :source-map-timestamp true
                                                   :pretty-print         true
                                                   :cache-analysis       true}}}}
                   :figwheel     {:http-server-root "public" ;; default and assumes "resources"
                                  :server-port      3449    ;; default
                                  :css-dirs         ["resources/public/css"] ;; watch and update CSS

                                  ;; Start an nREPL server into the running figwheel process
                                  ;; :nrepl-port 7888

                                  ;; Server Ring Handler (optional)
                                  ;; if you want to embed a ring handler into the figwheel http-kit
                                  ;; server, this is simple ring servers, if this
                                  ;; doesn't work for you just run your own server :)
                                  ;; :ring-handler hello_world.server/handler
                                  :ring-handler     webapp.core/handler

                                  ;; To be able to open files in your editor from the heads up display
                                  ;; you will need to put a script on your path.
                                  ;; that script will have to take a file path and a line number
                                  ;; ie. in  ~/bin/myfile-opener
                                  ;; #! /bin/sh
                                  ;; emacsclient -n +$2 $1
                                  ;;
                                  ;; :open-file-command "myfile-opener"

                                  ;; if you want to disable the REPL
                                  ;; :repl false

                                  ;; to configure a different figwheel logfile path
                                  :server-logfile   "tmp/figwheel-logfile.log"
                                  }}}

  :clean-targets ^{:protect false} [:target-path "resources/public/js/out" "resources/public/js/webapp.min.js"]
  )
