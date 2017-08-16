name := "QACinema"

version := "1.0"

lazy val `playproject` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

libraryDependencies +=  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.5-play25"

libraryDependencies += "org.scoverage" % "scalac-scoverage-plugin_2.11" % "1.1.1" % "provided"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test"

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"

libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator

libraryDependencies += "com.stripe" % "stripe-java" % "1.4.2"

libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.35.0" % "test"