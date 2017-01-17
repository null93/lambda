package io.raffi.interpreter.parser

import org.scalatest.{ FunSuite }

class TypeTest extends FunSuite {

	test ("Test the toString () function") {
		assert ( Type.LAMBDA.toString.equals ("LAMBDA") )
		assert ( Type.DOT.toString.equals ("DOT") )
		assert ( Type.VARIABLE.toString.equals ("VARIABLE") )
		assert ( Type.LEFT_PARENTHESIS.toString.equals ("LEFT_PARENTHESIS") )
		assert ( Type.RIGHT_PARENTHESIS.toString.equals ("RIGHT_PARENTHESIS") )
		assert ( Type.UNKNOWN.toString.equals ("UNKNOWN") )
		assert ( Type.EOT.toString.equals ("EOT") )
	}

}