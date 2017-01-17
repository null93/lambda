package io.raffi.interpreter.ast

import io.raffi.interpreter.parser.{ Parser }
import org.scalatest.{ FunSuite }
import scala.collection.mutable.ArrayBuffer

class FunctionTest extends FunSuite {

	test ("Test the simplify () function") {
		var function1 = new Parser ("lambda x y . z").root
		var function2 = new Parser ("lambda x . y").root
		var function3 = new Parser ("lambda x x d f g . y").root
		assert ( function1.simplify )
		assert ( !function2.simplify )
		assert ( function3.simplify )
	}

	test ("Test the resolve () function") {
		var function1 = new Parser ("lambda x y . z").root
		var function2 = new Parser ("lambda x . y").root
		var function3 = new Parser ("lambda x x d f g . y").root
		assert ( !function1.resolve )
		assert ( !function2.resolve )
		assert ( !function3.resolve )
	}

	test ("Test the apply () function") {
		var function1 = new Parser ("lambda x y . z").root
		var function2 = new Parser ("lambda x . y").root
		var function3 = new Parser ("lambda x x d f g . y").root
		assert ( function1.apply ( new ArrayBuffer [String] () ) == function1 )
		assert ( function2.apply ( new ArrayBuffer [String] () ) == function2 )
		assert ( function3.apply ( new ArrayBuffer [String] () ) == function3 )
	}

	test ("Test the applicable () function") {
		var function1 = new Parser ("lambda x y . z").root
		var function2 = new Parser ("lambda x . y").root
		var function3 = new Parser ("lambda x x d f g . y").root
		assert ( !function1.applicable )
		assert ( !function2.applicable )
		assert ( !function3.applicable )
	}

	test ("Test the equals () function") {
		var function1 = new Parser ("lambda x y . z").root
		var function2 = new Parser ("lambda y z . z").root
		var function3 = new Parser ("lambda x y . u").root
		var function4 = new Parser ("lambda x y . z").root
		assert ( !function1.equals ( function4 ) )
		assert ( !function1.equals ( function3 ) )
		assert ( !function1.equals ( function2 ) )
		assert ( function1.equals ( function1 ) )
	}

	test ("Test the clone () function") {
		var function1 = new Parser ("lambda x y . z").root
		var function2 = new Parser ("lambda y z . z").root
		var function3 = new Parser ("lambda x y . u").root
		var function4 = new Parser ("lambda x y . z").root
		assert ( function1.clone.equals ( function1 ) )
		assert ( function2.clone.equals ( function2 ) )
		assert ( function3.clone.equals ( function3 ) )
		assert ( function4.clone.equals ( function4 ) )
	}

}