Global / onChangedBuildSource := ReloadOnSourceChanges
import commandmatrix.extra._

lazy val Versions = new {
  val Scala3 = "3.1.0"

  val scala = List(Scala3)
}

lazy val examples = projectMatrix
  .in(file("examples"))
  .allVariations(
    Versions.scala,
    List(VirtualAxis.js)
  )
  .dependsOn(sources)
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSLinkerConfig ~= { conf => conf.withModuleKind(ModuleKind.ESModule) }
  )

lazy val sources = projectMatrix
  .in(file("sources"))
  .allVariations(
    Versions.scala,
    List(VirtualAxis.js)
  )
  .enablePlugins(ScalablyTypedConverterGenSourcePlugin)
  .settings(
    moduleName := "cloudflare-functions",
    Compile / npmDependencies ++= Seq(
      "@cloudflare/workers-types" -> "3.3.0"
    ),
    stOutputPackage := "com.indoorvivants.cloudflare",
    stMinimize := Selection.AllExcept("@cloudflare/workers-types")
  )

inThisBuild(
  Seq(
    organization := "com.indoorvivants",
    organizationName := "Anton Sviridov",
    homepage := Some(
      url("https://github.com/indoorvivants/cloudflare-functions")
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
