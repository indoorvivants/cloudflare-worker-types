# Cloudflare workers types

[![worker-types Scala version support](https://index.scala-lang.org/indoorvivants/cloudflare-worker-types/worker-types/latest-by-scala-version.svg?targetType=Js)](https://index.scala-lang.org/indoorvivants/cloudflare-worker-types/worker-types)

This is a very basic Scala.js 3 facade for [Cloudflare Workers API](https://github.com/cloudflare/workers-types).

It's generated using the amazing [ScalablyTyped](https://scalablytyped.org/docs/readme.html) plugin, and published to Maven Central.
No additional changes to generated facades are performed, and for now there are no extra abstractions on top of them.

Motivating sample:

```scala
import com.indoorvivants.cloudflare.cloudflareWorkersTypes.*
import com.indoorvivants.cloudflare.std
import scala.scalajs.js.annotation.JSExportTopLevel

type Params = std.Record[String, scala.Any]

@JSExportTopLevel(name = "onRequest", moduleID = "request_headers")
def request_headers(context: EventContext[Any, String, Params]) =
  val str = StringBuilder()
  context.request.headers.forEach { (_, value, key, _) =>
    str.append(s"Keys: $key, value: $value\n")
  }
  global.Response("hello, world. Your request comes with \n" + str.result)
```

## Installation

As it's a regular Scala.js library, just add this to your...

**`build.sbt` file if you are using SBT**

```scala
libraryDependencies += "com.indoorvivants.cloudflare" %%% "worker-types" % "3.3.0"
```

**`build.sc` if you are using Mill**

```scala
def ivyDeps = Agg(
  ivy"com.indoorvivants.cloudflare::worker-types::3.3.0"
)
```

And start exploring!

## Compatibility

* Scala 3-only (you can easily publish your own Scala 2 bindings should you need them, or generate on demand)
* Each release is tagged with the corresponding version of upstream definitions
* As such, no semantic versioning or binary compatibility guarantees are provided
* Have fun with it
