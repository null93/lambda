package io.raffi.interpreter.scope

import io.raffi.interpreter.ast.{ AbstractNode, Application, Function, Variable }
import org.scalatest.{ FunSuite }

class SymbolTest extends FunSuite {

	test ("Test that data sets correctly") {
		var variable = new Variable ("test_var")
		var symbol = new Symbol ( "test", variable )
		assert ( symbol.identifier.equals ("test") )
		assert ( symbol.node == variable )
	}

	test ("Test that all types of AbstractNode are accepted") {
		var variable = new Variable ("3")
		var function = new Function ( Array ( variable ), variable )
		var application = new Application ( variable, variable )
		var symbol1 = new Symbol ( "var", variable )
		var symbol2 = new Symbol ( "fun", function )
		var symbol3 = new Symbol ( "app", application )
		assert ( symbol1.identifier.equals ("var") )
		assert ( symbol2.identifier.equals ("fun") )
		assert ( symbol3.identifier.equals ("app") )
		assert ( symbol1.node == variable )
		assert ( symbol2.node == function )
		assert ( symbol3.node == application )
	}

}