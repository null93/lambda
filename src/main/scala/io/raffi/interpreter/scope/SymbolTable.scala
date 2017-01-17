package io.raffi.interpreter.scope

import io.raffi.interpreter.{ Color }
import io.raffi.interpreter.ast.{ AbstractNode }

/**
 * This class simply attempts to act like a symbol table.  The symbol table contains scopes which
 * contain symbols.  Symbols bind a string identifier to an abstract node in the AST as the value.
 * This singleton objects helps to enter scopes, exit scopes, and aids in finding and inserting
 * symbols into our scope table with the assistance of the methods in the Scope class.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.scope
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
object SymbolTable {

	// Initialize an array of scopes
	var scopes = Array [Scope] ()

	// Initially, set up the global scope
	reset

	/**
	 * This method returns the number of symbols in all the scopes.  This is combined using the size
	 * method for each scope.
	 * @return      Int                                     Number of symbols in the symbol table
	 */
	def size : Int = {
		// Initialize to zero
		var sum = 0
		// For each scope, add up the scopes size
		scopes.foreach {
			sum += _.size
		}
		// Return the sum
		return sum
	}

	/**
	 * In order to enter a new scope, we append a scope into the scope array, with an incremented
	 * scope name.
	 */
	def enterScope = {
		// Add a new scope
		scopes = scopes :+ new Scope ( "Scope #" + scopes.length )
	}

	/**
	 * When we exit a scope, we simply drop the last scope entry in the array of scopes.
	 */
	def exitScope = {
		// Drop the current scope
		scopes = scopes.dropRight ( 1 )
	}

	/**
	 * This method takes in a string identifier and an abstract AST node as a value, and it inserts
	 * a new symbol into the last scope in the array of scopes.
	 * @param       String              identifier          The identifier to use to bind to data
	 * @param       AbstractNode        value               The value to bind the the identifier
	 */
	def insert ( identifier : String, value : AbstractNode ) = {
		// Insert into the current scope
		scopes.last.insert ( identifier, value )
	}

	/**
	 * This method takes in an identifier and looks through each scope in reverse order, if the
	 * symbol exists in that scope.
	 * @param       String              target              The identifier to search for
	 * @return      Boolean                                 Symbol with the identifier exist?
	 */
	def exists ( target : String ) : Boolean = {
		// Loop through all the scopes in reverse order
		for ( scope <- scopes.reverse ) {
			// If the identifier exists in the scope
			if ( scope.exists ( target ) ) {
				// Return true
				return true
			}
		}
		// Otherwise return false
		return false
	}

	/**
	 * This method takes in an identifier and checks to see if it is in any scope other than the
	 * global scope.
	 * @param       String              target              The identifier to search for
	 * @return      Boolean                                 Symbol with the identifier exist?
	 */
	def existsNotInGlobal ( target : String ) : Boolean = {
		// Loop through all the scopes in reverse order without global scope
		for ( scope <- scopes.drop (1).reverse ) {
			// If the identifier exists in the scope
			if ( scope.exists ( target ) ) {
				// Return true
				return true
			}
		}
		// Otherwise return false
		return false
	}

	/**
	 * This method looks through all the scopes in the scopes array in reverse order, and asks each
	 * scope if the symbol with the passed identifier exists.
	 * @param       String              target              The identifier to look for
	 * @return      Option[Symbol]                          The option if found, None otherwise
	 */
	def lookup ( target : String ) : Option [ Symbol ] = {
		// Loop through all the scopes in reverse order
		for ( scope <- scopes.reverse ) {
			// Initialize the result to be a search in the scope
			var result = scope.find ( target )
			// Match against the result
			result match {
				case None => {}
				case _ => return result
			}
		}
		// By default return false
		return None
	}

	/**
	 * This resets the symbol table by removing all the current scopes in the scope array, by
	 * re-initializing the scopes array.  It then appends the global scope into the scopes array.
	 */
	def reset = {
		// Reinitialize scopes array to a new array
		scopes = Array [Scope] ()
		// Append the global scope
		scopes = scopes :+ new Scope (f"Global Scope")
	}

	/**
	 * This method simply prints out the contents of the symbol table into system out.  It also
	 * tries to pad it using the metric found using the helper function in the scope method.
	 */
	def print = {
		// Require the padding to be at least 8
		var max = 8
		// Loop through all the scopes
		for ( scope <- scopes ) {
			// If the scope identifier name length is greater then max
			if ( scope.maxVarLength > max ) {
				// Record max value
				max = scope.maxVarLength
			}
		}
		// Print new line for style
		println ("")
		// Loop through all the scopes
		for ( scope <- scopes ) {
			// Print each scope
			scope.display ( max )
		}
		// If the size is zero
		if ( size == 0 ) {
			// Print out a message that says that there are no symbols
			println ( Color.colorize ("    #cNo Symbols#d") )
		}
		// Print new line for style
		println ("")
	}

}