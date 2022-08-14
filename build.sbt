Global / onChangedBuildSource := ReloadOnSourceChanges
import commandmatrix.extra.*

lazy val Versions = new {
  val Scala3 = "3.1.3"

  val scala = List(Scala3)

  val cloudflareWorkers = "3.3.0"
}

lazy val root = project
  .in(file("."))
  .aggregate(examples.projectRefs*)
  .aggregate(cloudflare.projectRefs*)
  .settings(noPublish)

lazy val examples = projectMatrix
  .in(file("examples"))
  .allVariations(
    Versions.scala,
    List(VirtualAxis.js)
  )
  .dependsOn(cloudflare)
  .enablePlugins(ScalaJSPlugin)
  .settings(noPublish)
  .settings(
    scalaJSLinkerConfig ~= { conf =>
      conf.withModuleKind(ModuleKind.ESModule)
    },
    Test / scalaJSLinkerConfig ~= { conf =>
      conf.withModuleKind(ModuleKind.CommonJSModule)
    }
  )

lazy val cloudflare = projectMatrix
  .in(file("sources"))
  .allVariations(
    Versions.scala,
    List(VirtualAxis.js)
  )
  .enablePlugins(ScalablyTypedConverterGenSourcePlugin)
  .settings(
    organization := "com.indoorvivants.cloudflare",
    moduleName := "worker-types",
    Compile / npmDependencies ++= Seq(
      "@cloudflare/workers-types" -> Versions.cloudflareWorkers
    ),
    stOutputPackage := "com.indoorvivants.cloudflare",
    stMinimize := Selection.AllExcept("@cloudflare/workers-types"),
    Compile / packageSrc / mappings ++= {
      val base = (Compile / sourceManaged).value
      val files = (Compile / managedSources).value
      files.map { f => (f, f.relativeTo(base).get.getPath) }
    }
  )

inThisBuild(
  Seq(
    organization := "com.indoorvivants",
    organizationName := "Anton Sviridov",
    homepage := Some(
      url("https://github.com/indoorvivants/scalajs-serverless")
    ),
    startYear := Some(2022),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "keynmol",
        "Anton Sviridov",
        "velvetbaldmime@protonmail.com",
        url("https://blog.indoorvivants.com")
      )
    )
  )
)

ThisBuild / concurrentRestrictions += Tags.limit(ScalablyTypedTag, 1)

val noPublish = Seq(
  publish / skip := true,
  publishLocal / skip := true
)
