## Discuss

We have a Matrix chat at [#prism-break:matrix.org][], and a Discourse forum at
<https://prism-break.discourse.group>.

[#prism-break:matrix.org]: https://riot.im/app/#/room/#prism-break:matrix.org

## Donate

Consider setting up a recurring donation to our project at Open Collective:
<https://opencollective.com/prism-break>

## Translate

Help us translate PRISM Break on Weblate:
<https://hosted.weblate.org/projects/prism-break/>

## Screen

https://gitlab.com/prism-break/prism-break/issues

## Mirror

Install [Clojure][]. Then, to build a non-minified version of the site, run:

```
clj -m prism-break.tasks.build
```

To build a minified version of the site, install [Brotli][], [Zopfli][], and
run:

```
clj -m prism-break.tasks.build-release
```

### Nix

Instead of managing dependencies manually, you can install [Nix][] and run
`nix-shell`. This will spawn a shell with all dependencies (Clojure, Brotli,
Zopfli) set up. Dependencies are pinned down and exactly match the versions
used to build <https://prism-break.org>.

Once you leave the shell, you will end up in your regular environment without
any dependencies as if nothing's happened.

Nix is only available for Linux and macOS.

### Windows

If you run Windows, you will need [Cygwin][] or [WSL][] to install Clojure.
It's because [the wrapper](https://github.com/clojure/brew-install) is written
in shell script.

This is subject to change. I (Yegor) will probably end up writing a
cross-platform wrapper in Go.

### Reproducible builds

Build commands print a [NAR][] hash of `public` directory. It is used to ensure
[reproducible builds][]. You can ask someone else to build the same [revision][]
as you, and verify that build result matches. You can also compare against
[hashes produced by our CI][CI jobs].

PRISM Break uses [its own NAR implementation][prism-break.data.nar]. If you
have [Nix][], you can compare the hash against `nix hash-path public`. Mind
that our NAR doesn't handle executables and symlinks the same way Nix does.
See [`prism-break.data.nar`][prism-break.data.nar] docs for more info.

[Clojure]: https://clojure.org/guides/getting_started
[Brotli]: https://github.com/google/brotli
[Zopfli]: https://github.com/google/zopfli
[Nix]: https://nixos.org/nix/
[Cygwin]: https://www.cygwin.com
[WSL]: https://docs.microsoft.com/en-us/windows/wsl/about
[NAR]: https://nixos.org/nixos/nix-pills/automatic-runtime-dependencies.html#idm140737316073472
[reproducible builds]: https://reproducible-builds.org
[revision]: http://dionisev.com/2016/03/git-revisions
[CI jobs]: https://gitlab.com/prism-break/prism-break/-/jobs
[prism-break.data.nar]: src/prism_break/data/nar.clj

## Update

## Hack

You will need to learn some Clojure. See Clojure's [Getting Started guide][],
and [Clojure for the Brave and True][] book.

If you're just starting out, install Clojure (covered in [Mirror](#mirror)
section) and run:

```
clj -A:dev -m prism-break.tasks.http
```

This will start an HTTP server listening on <http://localhost:9090>. It will
reload Clojure namespaces on each request, so this is a good quick way to make
small changes without building site over and over again.

To set up an [interactive environment][], see [CIDER][] for Emacs,
[vim-fireplace][] for Vim. While you're at it, install [Parinfer][]
or learn [Paredit][].

[Parinfer]: https://shaunlebron.github.io/parinfer/
[Paredit]: http://danmidwood.com/content/2014/11/21/animated-paredit.html

To start nREPL, run:

```
clj -A:dev -m prism-break.tasks.nrepl
```

[Getting Started guide]: https://clojure.org/guides/getting_started
[Clojure for the Brave and True]: https://www.braveclojure.com/clojure-for-the-brave-and-true/
[interactive environment]: http://tonsky.me/blog/interactive-development/
[CIDER]: https://github.com/clojure-emacs/cider
[vim-fireplace]: https://github.com/tpope/vim-fireplace
