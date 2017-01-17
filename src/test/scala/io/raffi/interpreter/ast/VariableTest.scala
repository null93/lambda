package io.raffi.interpreter.ast

import org.scalatest.{ FunSuite }

class VariableTest extends FunSuite {

	test ("Test the equals () function") {
		var variable1 = new Variable ("a")
		var variable2 = new Variable ("b")
		var variable3 = new Variable ("a")
		assert ( !variable1.equals ( variable2 ) )
		assert ( variable1.equals ( variable3 ) )
	}

	test ("Test the clone () function") {
		var variable1 = new Variable ("a")
		var variable2 = new Variable ("b")
		var variable3 = new Variable ("c")
		assert ( variable1.clone.equals ( variable1 ) )
		assert ( variable2.clone.equals ( variable2 ) )
		assert ( variable3.clone.equals ( variable3 ) )
	}

}