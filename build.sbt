name := "Concordance"

version := "0.0.1"



libraryDependencies ++= {
          Seq(
                    "com.googlecode.clearnlp" % "clearnlp" % "1.4.2"
          )
}

mainClass := Some("org.concordance.Concordance")


