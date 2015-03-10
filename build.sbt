name := "GPUDemo"

version := "1.0"

scalaVersion := "2.10.5"

val dl4jVersion = "0.0.3.2"
val nd4jVersion = "0.0.3.5.5.2"

libraryDependencies ++= Seq(
 "org.apache.spark" % "spark-assembly_2.10" % "1.1.1",
//  "org.deeplearning4j" % "deeplearning4j-core" % dl4jVersion,
//  "org.deeplearning4j" % "deeplearning4j-nlp" % dl4jVersion,
  "org.nd4j" % "nd4j-jcublas-6.0" % nd4jVersion
)