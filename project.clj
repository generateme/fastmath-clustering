(defproject generateme/fastmath-clustering "1.0.0"
  :description "SMILE Clustering wrapper"
  :url "https://github.com/generateme/fastmath-clustering"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [com.github.haifengl/smile-core "2.6.0"]
                 [com.github.haifengl/smile-mkl "2.6.0"]

                 [org.bytedeco/arpack-ng "3.7.0-1.5.4"]
                 [org.bytedeco/arpack-ng-platform "3.7.0-1.5.4"]
                 [org.bytedeco/openblas "0.3.10-1.5.4"]
                 [org.bytedeco/openblas-platform "0.3.10-1.5.4"]
                 [org.bytedeco/javacpp "1.5.4"]]
  :scm {:name "git"
        :url "https://github.com/generateme/fastmath-clustering"}
  :pedantic? false
  :profiles {:dev-codox {:codox {:source-uri "https://github.com/generateme/fastmath-clustering/blob/master/{filepath}#L{line}"}}})
