package io.raffi.interpreter.scope

import io.raffi.interpreter.ast.{ Variable }
import org.scalatest.{ FunSuite }

class SymbolTableTest extends FunSuite {

	test ("Test the size () function") {
		assert ( SymbolTable.size == 0 )
		SymbolTable.insert ( "test", new Variable ("test") )
		assert ( SymbolTable.size == 1 )
		SymbolTable.reset
		assert ( SymbolTable.size == 0 )
	}

	test ("Test the enterScope () function") {
		assert ( SymbolTable.lookup ("test") == None )
		SymbolTable.enterScope
		SymbolTable.insert ( "test", new Variable ("test") )
		assert ( SymbolTable.lookup ("test") != None )
		SymbolTable.exitScope
		assert ( SymbolTable.lookup ("test") == None )
		SymbolTable.reset
	}

	test ("Test the exitScope () function") {
		SymbolTable.insert ( "test", new Variable ("test") )
		assert ( SymbolTable.lookup ("test") != None )
		SymbolTable.enterScope
		assert ( SymbolTable.lookup ("test") != None )
		SymbolTable.exitScope
		SymbolTable.exitScope
		assert ( SymbolTable.lookup ("test") == None )
		SymbolTable.reset
	}

	test ("Test the insert (String,AbstractNode) function") {
		var variable = new Variable ("test")
		SymbolTable.insert ( "test", variable )
		assert ( SymbolTable.lookup ("test") != None )
		SymbolTable.reset
	}

	test ("Test the exists (String) function") {
		assert ( !SymbolTable.exists ("test") )
		SymbolTable.insert ( "test", new Variable ("test") )
		assert ( SymbolTable.exists ("test") )
	}

	test ("Test the lookup (String) function") {
		var variable = new Variable ("testval")
		SymbolTable.insert ( "test", variable )
		SymbolTable.lookup ("test") match {
			case Some ( result ) => {
				result.node match {
					case resNode : Variable => assert ( resNode.variable.equals ("testval") )
					case _ => assert ( false )
				}
			}
			case _ => assert ( false )
		}
		SymbolTable.reset
	}

	test ("Test the reset () function") {
		assert ( SymbolTable.size == 0 )
		SymbolTable.insert ( "test1", new Variable ("test") )
		assert ( SymbolTable.size == 1 )
		SymbolTable.insert ( "test2", new Variable ("test") )
		assert ( SymbolTable.size == 2 )
		SymbolTable.insert ( "test3", new Variable ("test") )
		assert ( SymbolTable.size == 3 )
		SymbolTable.reset
		assert ( SymbolTable.size == 0 )
	}

}