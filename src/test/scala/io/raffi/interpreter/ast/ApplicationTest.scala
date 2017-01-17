package io.raffi.interpreter.ast

import io.raffi.interpreter.parser.{ Parser }
import org.scalatest.{ FunSuite }
import scala.collection.mutable.ArrayBuffer

class ApplicationTest extends FunSuite {

	test ("Test the simplify () function") {
		var application1 = new Parser ("(lambda x . x)3").root
		var application2 = new Parser ("(3 4)").root
		var application3 = new Parser ("(lambda x y.z)3").root
		assert ( !application1.simplify )
		assert ( !application2.simplify )
		assert ( application3.simplify )
	}

	test ("Test the resolve () function") {
		var application1 = new Parser ("(lambda x . x)3").root
		var application2 = new Parser ("(3 4)").root
		var application3 = new Parser ("(lambda x y.z)3").root
		assert ( !application1.resolve )
		assert ( !application2.resolve )
		assert ( !application3.resolve )
	}

	test ("Test the apply () function") {
		var application1 = new Parser ("(lambda x . x)3").root
		var application2 = new Parser ("(3 4)").root
		var application3 = new Parser ("(lambda x y.z)3").root
		assert ( application1.apply ( new ArrayBuffer [String] () ) != application1 )
		assert ( application2.apply ( new ArrayBuffer [String] () ) == application2 )
		assert ( application3.apply ( new ArrayBuffer [String] () ) != application3 )
	}

	test ("Test the applicable () function") {
		var application1 = new Parser ("(lambda x . x)3").root
		var application2 = new Parser ("(3 4)").root
		var application3 = new Parser ("(lambda x y.z)3").root
		assert ( application1.applicable )
		assert ( !application2.applicable )
		assert ( application3.applicable )
	}

	test ("Test the equals () function") {
		var application1 = new Parser ("(lambda x . x)3").root
		var application2 = new Parser ("(3 4)").root
		var application3 = new Parser ("(lambda x y.z)3").root
		assert ( application1.equals ( application1 ) )
		assert ( application2.equals ( application2 ) )
		assert ( application3.equals ( application3 ) )
	}

	test ("Test the clone () function") {
		var application1 = new Parser ("(lambda x . x)3").root
		var application2 = new Parser ("(3 4)").root
		var application3 = new Parser ("(lambda x y.z)3").root
		assert ( application1.clone.equals ( application1 ) )
		assert ( application2.clone.equals ( application2 ) )
		assert ( application3.clone.equals ( application3 ) )
	}

}