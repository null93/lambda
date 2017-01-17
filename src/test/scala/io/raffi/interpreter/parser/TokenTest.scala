package io.raffi.interpreter.parser

import io.raffi.interpreter.{ Color }
import org.scalatest.{ FunSuite }

class TokenTest extends FunSuite {

	var token = new Token ( Type.LAMBDA, "LAMBDA", 10 )

	test ("Test the equals (Type.Value) function") {
		assert ( token.equals ( Type.LAMBDA ) )
		assert ( !token.equals ( Type.DOT ) )
		assert ( !token.equals ( Type.VARIABLE ) )
		assert ( !token.equals ( Type.LEFT_PARENTHESIS ) )
		assert ( !token.equals ( Type.RIGHT_PARENTHESIS ) )
		assert ( !token.equals ( Type.UNKNOWN ) )
		assert ( !token.equals ( Type.EOT ) )
	}

	test ("Test the getType () function") {
		assert ( token.getType.equals ( Type.LAMBDA ) )
		assert ( !token.getType.equals ( Type.DOT ) )
		assert ( !token.getType.equals ( Type.VARIABLE ) )
		assert ( !token.getType.equals ( Type.LEFT_PARENTHESIS ) )
		assert ( !token.getType.equals ( Type.RIGHT_PARENTHESIS ) )
		assert ( !token.getType.equals ( Type.UNKNOWN ) )
		assert ( !token.getType.equals ( Type.EOT ) )
	}

	test ("Test the getValue () function") {
		assert ( token.getValue.equals ("LAMBDA") )
	}

	test ("Test the toString () function") {
		var index = 10
		var value = "LAMBDA"
		var kind = Type.LAMBDA
		assert ( token.toString.equals ( Color.colorize (f"#r$kind#d '$value' @ $index") ) )
	}

}