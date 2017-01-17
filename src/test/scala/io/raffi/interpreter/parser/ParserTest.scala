package io.raffi.interpreter.parser

import io.raffi.interpreter.ast.{ Application, Function, Variable }
import org.scalatest.{ FunSuite }
import org.scalatest.Assertions._

class ParserTest extends FunSuite {

	test ("Test the valid input") {
		var parser = new Parser ("lambda x.x")
		parser.root match {
			case function : Function => assert ( function.body.isInstanceOf [Variable] )
			case _ => assert ( false )
		}
		parser = new Parser ("((((2))))")
		parser.root match {
			case variable : Variable => assert ( variable.variable.equals ("2") )
			case _ => assert ( false )
		}
		parser = new Parser ("((lambda x.x)(3))")
		parser.root match {
			case application : Application => {
				assert ( application.lhs.isInstanceOf [Function] )
				assert ( application.rhs.isInstanceOf [Variable] )
			}
			case _ => assert ( false )
		}
	}

	test ("Test the invalid input") {
		intercept[ParseException] {
      		new Parser ("lambda . x")
      		assert ( false )
    	}
		assert ( true )
		intercept[ParseException] {
      		new Parser ("lambda x x")
      		assert ( false )
    	}
		assert ( true )
		intercept[ParseException] {
      		new Parser ("lambda x x :")
      		assert ( false )
    	}
		assert ( true )
		intercept[ParseException] {
      		new Parser ("lambda x.x (")
      		assert ( false )
    	}
		assert ( true )
	}

	test ("Test the root node after parse") {
		var function = new Parser ("lambda x . x")
		var application = new Parser ("(lambda x . x) 3")
		var variable = new Parser ("3")
		assert ( function.root.toString.equals ("(λx.x)") )
		assert ( application.root.toString.equals ("((λx.x) 3)") )
		assert ( variable.root.toString.equals ("3") )
	}

}