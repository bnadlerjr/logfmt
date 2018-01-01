# logfmt

A Clojure library for emitting [logfmt](https://brandur.org/logfmt). Read the
[annotated source](http://bobnadler.com/logfmt/) for details. See the
[changelog](https://github.com/bnadlerjr/logfmt/blob/master/CHANGELOG.md) for
latest updates.

[![Clojars Project](https://img.shields.io/clojars/v/bnadlerjr/logfmt.svg)](https://clojars.org/bnadlerjr/logfmt)
[![Build Status](https://travis-ci.org/bnadlerjr/logfmt.svg?branch=master)](https://travis-ci.org/bnadlerjr/logfmt)

## Quickstart
### Install
Add the dependency to your `project.clj` file:

```
[bnadlerjr/logfmt "0.1.0"]
```

### Examples
#### Basic Logging

```clojure
(require '[logfmt.core :as log])

(log/info "Some message text." {:foo 1 :bar 2 :baz 3})
```

The above will print a message formatted like this to `STDOUT`:

```
at=info msg="Some message text." foo=1 bar=2 baz=3
```

#### Ring Middleware
This project also provides Ring middleware for request logging. For example:

```clojure
(ns hello-world.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [logfmt.ring.middleware :refer [wrap-logger]))

(defroutes app-routes
  (GET "/hello" [] "Hello World)
  (route/not-found "Page not found"))

(def app
  (-> app-routes
      wrap-logger))
```

Any GET requests to `/hello` will result in two messages being logged to `STDOUT`
like this:

```
at=info msg="Started GET '/hello'" method=GET path="/hello" params={} request-id=abc123
at=info msg="Completed 200 in 10ms" method=GET path="/hello" status=200 duration=10ms request-id=abc123
```

### Development Mode Logging
By default the var `dev-mode` is set to `false`. Setting it to `true` like so:

```clojure
(require '[logfmt.core :as log])

(log/set-dev-mode! true)
(log/info "Some message text." {:foo 1 :bar 2 :baz 3})
```

will result in messages being output in a more readable format suitable for
local development.

```
info | Some message text. foo=1 bar=2 baz=3
```

## Development
Pre-requisites:

1. [Leiningen](https://leiningen.org/)

To install a local snapshot:

```
$ lein install
```

To push a new release to Clojars:

1. Document added, removed , fixed, etc. in CHANGELOG
1. Update the version in `project.clj`
1. `lein doc`
1. `git add . && git commit`
1. `git tag -a vx.x.x -m "Tag version x.x.x"`
1. `git push --tags && git push`
1. Wait for successful TravisCI build
1. `lein deploy clojars`

### Contributing
Bug reports and pull requests are welcome on [GitHub](https://github.com/bnadlerjr/logfmt).

1. Fork it
1. Create your feature branch (`git checkout -b my-new-feature`)
1. Commit your changes (`git commit -am 'Add some feature'`)
1. Push to the branch (`git push origin my-new-feature`)
1. Create new Pull Request

## License

Copyright © 2017-2018 Bob Nadler, Jr.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
