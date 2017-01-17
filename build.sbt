name := "Untyped Lambda Calculus Interpretor (ULCI)"
version := "1.0.0"
organization := "io.raffi"
mainClass in (Compile, run) := Some ("io.raffi.interpreter.Interpreter")
scalaVersion := "2.11.8"
libraryDependencies ++= Seq (
	"org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)