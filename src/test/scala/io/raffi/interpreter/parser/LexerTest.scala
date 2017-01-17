package io.raffi.interpreter.parser

import org.scalatest.{ FunSuite }

class LexerTest extends FunSuite {

	test ("Test the increment () function") {
		var lexer = new Lexer ("lambda x.x")
		assert ( lexer.index == 0 )
		lexer.nextToken
		assert ( lexer.index == 6 )
		lexer.nextToken
		assert ( lexer.index == 8 )
		lexer.nextToken
		assert ( lexer.index == 9 )
		lexer.nextToken
		assert ( lexer.index == 10 )
		lexer.nextToken
		assert ( lexer.index == 10 )
		lexer.nextToken
		assert ( lexer.index == 10 )
	}

	test ("Test the nextToken () function with type") {
		var lexer = new Lexer ("(lambda x.x)")
		assert ( lexer.nextToken.getType.equals ( Type.LEFT_PARENTHESIS ) )
		assert ( lexer.nextToken.getType.equals ( Type.LAMBDA ) )
		assert ( lexer.nextToken.getType.equals ( Type.VARIABLE ) )
		assert ( lexer.nextToken.getType.equals ( Type.DOT ) )
		assert ( lexer.nextToken.getType.equals ( Type.VARIABLE ) )
		assert ( lexer.nextToken.getType.equals ( Type.RIGHT_PARENTHESIS ) )
		assert ( lexer.nextToken.getType.equals ( Type.EOT ) )
		assert ( lexer.nextToken.getType.equals ( Type.EOT ) )
	}

	test ("Test the nextToken () function with value") {
		var lexer = new Lexer ("(lambda x.x)")
		assert ( lexer.nextToken.getValue.equals ("(") )
		assert ( lexer.nextToken.getValue.equals ("lambda") )
		assert ( lexer.nextToken.getValue.equals ("x") )
		assert ( lexer.nextToken.getValue.equals (".") )
		assert ( lexer.nextToken.getValue.equals ("x") )
		assert ( lexer.nextToken.getValue.equals (")") )
		assert ( lexer.nextToken.getValue.equals ("End-Of-Token") )
		assert ( lexer.nextToken.getValue.equals ("End-Of-Token") )
	}

	test ("Test the nextToken () function with lambda symbol") {
		var lexer = new Lexer ("λ x.x")
		var token = lexer.nextToken
		assert ( token.getValue.equals ("λ") )
		assert ( token.getType.equals ( Type.LAMBDA ) )
		token = lexer.nextToken
		assert ( token.getValue.equals ("x") )
		assert ( token.getType.equals ( Type.VARIABLE ) )
		token = lexer.nextToken
		assert ( token.getValue.equals (".") )
		assert ( token.getType.equals ( Type.DOT ) )
		token = lexer.nextToken
		assert ( token.getValue.equals ("x") )
		assert ( token.getType.equals ( Type.VARIABLE ) )
		token = lexer.nextToken
		assert ( token.getValue.equals ("End-Of-Token") )
		assert ( token.getType.equals ( Type.EOT ) )
		token = lexer.nextToken
		assert ( token.getValue.equals ("End-Of-Token") )
		assert ( token.getType.equals ( Type.EOT ) )
	}

	test ("Test the nextToken () function with empty input") {
		var lexer = new Lexer ("")
		var token = lexer.nextToken
		assert ( token.getValue.equals ("End-Of-Token") )
		assert ( token.getType.equals ( Type.EOT ) )
	}

	test ("Test the nextToken () function with unknown") {
		var lexer = new Lexer ("lambda &*")
		var token = lexer.nextToken
		assert ( token.getValue.equals ("lambda") )
		assert ( token.getType.equals ( Type.LAMBDA ) )
		token = lexer.nextToken
		assert ( token.getValue.equals ("&") )
		assert ( token.getType.equals ( Type.UNKNOWN ) )
		token = lexer.nextToken
		assert ( token.getValue.equals ("*") )
		assert ( token.getType.equals ( Type.UNKNOWN ) )
		token = lexer.nextToken
		assert ( token.getValue.equals ("End-Of-Token") )
		assert ( token.getType.equals ( Type.EOT ) )
		token = lexer.nextToken
	}

}