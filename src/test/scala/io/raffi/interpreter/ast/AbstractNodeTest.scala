package io.raffi.interpreter.ast

import org.scalatest.{ FunSuite }
import scala.collection.mutable.ArrayBuffer

class AbstractNodeTest extends FunSuite {

	test ("Test the default results to all parent methods") {
		var an = new AbstractNode
		assert ( !an.simplify )
		assert ( !an.resolve )
		assert ( an.apply ( new ArrayBuffer [String] () ) == an )
		assert ( !an.applicable )
		assert ( !an.equals ( an ) )
		assert ( an.highlighted.equals ("null") )
		assert ( an.toString.equals ("null") )
		assert ( an.clone == an )
	}

}