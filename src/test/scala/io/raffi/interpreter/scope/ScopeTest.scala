package io.raffi.interpreter.scope

import io.raffi.interpreter.ast.{ Variable }
import org.scalatest.{ FunSuite }

class ScopeTest extends FunSuite {

	test ("Test the size () function") {
		var scope = new Scope ("Test Scope")
		assert ( scope.size == 0 )
		scope.insert ( "test", new Variable ("test") )
		assert ( scope.size == 1 )
		scope.insert ( "test", new Variable ("test2") )
		assert ( scope.size == 1 )
	}

	test ("Test the exists (String) function") {
		var scope = new Scope ("Test Scope")
		assert ( !scope.exists ("test2") )
		scope.insert ( "test", new Variable ("test") )
		assert ( !scope.exists ("test2") )
		scope.insert ( "test2", new Variable ("test2") )
		assert ( scope.exists ("test2") )
	}

	test ("Test the find (String) function") {
		var scope = new Scope ("Test Scope")
		scope.insert ( "test", new Variable ("test") )
		assert ( scope.find ("test").isInstanceOf [Some [Symbol]] )
	}

	test ("Test the insert (String,AbstractNode) function") {
		var scope = new Scope ("Test Scope")
		var var1 = new Variable ("test1")
		var var2 = new Variable ("test2")
		scope.insert ( "test", var1 )
		scope.find ("test").foreach {
			i => assert ( i.node == var1 )
		}
		scope.insert ( "test", var2 )
		scope.find ("test").foreach {
			i => assert ( i.node == var2 )
		}
	}

}