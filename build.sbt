name          := "scalaz-playground"

version       := "0.1.0"

scalaVersion  := "2.11.7"

scalacOptions ++= Seq(
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-encoding",
  "utf8"
)

libraryDependencies ++= {
  val scalazV = "7.2.0"
  Seq(
    "org.scalaz"  %%  "scalaz-core"                 % scalazV,
    //"org.scalaz"  %%  "scalaz-effect"               % scalazV,
    //"org.scalaz"  %%  "scalaz-stream"               % scalazV,
    "org.scalaz"  %%  "scalaz-concurrent"           % scalazV
  )
}
