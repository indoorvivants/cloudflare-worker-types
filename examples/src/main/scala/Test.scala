package examples

import com.indoorvivants.cloudflare.cloudflareWorkersTypes.*
import com.indoorvivants.cloudflare.std
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js
import scala.scalajs.js.typedarray.Uint8Array

object api:
  type Params = std.Record[String, scala.Any]

  @JSExportTopLevel(name = "onRequest", moduleID = "request_type")
  def request_type(context: EventContext[Any, String, Params]) =
    new global.Response(
      "hello, world. Your request comes with " + context.request.method
    )

  @JSExportTopLevel(name = "onRequest", moduleID = "request_headers")
  def request_headers(context: EventContext[Any, String, Params]) =
    val str = StringBuilder()
    context.request.headers.forEach { (_, value, key, _) =>
      str.append(s"Keys: $key, value: $value\n")
    }
    new global.Response("hello, world. Your request comes with " + str.result)

  @JSExportTopLevel(name = "onRequestPost", moduleID = "kv_example")
  def kv_example(context: EventContext[js.Dynamic, String, Params]) =
    val str = StringBuilder()
    val NAMESPACE = context.env.NAMESPACE.asInstanceOf[
      com.indoorvivants.cloudflare.cloudflareWorkersTypes.KVNamespace
    ]
    // Well I can't actually make it 400..
    def badRequest(msg: String) = global.Response(msg)
    def ok(msg: String) = global.Response(msg)

    val Key = "value"

    context.request.body match
      case body =>
        body
          .getReader()
          .read()
          .`then` { b =>
            val v = b.asInstanceOf[anon.Value[Any]]
            val arr = v.value.asInstanceOf[Uint8Array]
            val decoder = new global.TextDecoder().decode(arr.buffer)

            NAMESPACE.get(Key).`then` { value =>
              val current = if value == null then 0 else value.toInt
              println(s"Current is $current")
              decoder match
                case "increment" =>
                  NAMESPACE
                    .put(Key, (current + 1).toString)
                    .`then`(_ => ok("+1"))
                case "decrement" =>
                  NAMESPACE
                    .put(Key, (current - 1).toString)
                    .`then`(_ => ok("-1"))
                case other =>
                  badRequest(
                    s"Expected body to be one of 'increment' or 'decrement', got '$other' instead"
                  )
              end match
            }
          }
      case null => badRequest("Must supply request body!")
    end match

  end kv_example
end api
