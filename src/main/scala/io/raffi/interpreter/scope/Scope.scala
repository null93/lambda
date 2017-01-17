package io.raffi.interpreter.scope

import io.raffi.interpreter.{ Color }
import io.raffi.interpreter.ast.{ AbstractNode }

/**
 * This class encases symbols in an array.  It can be thought of as a table with symbols definitions
 * within it.  It also has some helper function that help discover, add, remove, etc, symbols from
 * said table.  These scopes like inside the actual scope table.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.scope
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 * @param           String              name                The name identifier for the scope
 */
class Scope ( name : String ) {

	// Initialize an array of symbol definitions
	var symbols = Array [Symbol] ()

	/**
	 * This method simply returns the size of the symbols array.  Effectively telling us how many
	 * symbol definitions exist in this scope.
	 * @return      Int                                     Number of symbols in this scope
	 */
	def size : Int = {
		// Return the length of the symbol array
		return symbols.length
	}

	/**
	 * This method simply returns if a symbol exists in the scopes table, based on the identifier's
	 * string value.
	 * @param       String              target              The identifier binded to the symbol
	 * @return      Boolean                                 Does it exist in the scope table?
	 */
	def exists ( target : String ) : Boolean = {
		// Traverse through all the symbols
		for ( symbol <- symbols ) {
			// If the identifier equals the current symbol name
			if ( symbol.identifier.equals ( target ) ) {
				// The symbol exists
				return true
			}
		}
		// Otherwise it doesn't exist
		return false
	}

	/**
	 * This method takes in an identifier string and tries to return the symbol that is associated
	 * with that identifier.  If returns an option so we can match against it.
	 * @param       String              target              The identifier binded to the symbol
	 * @return      Option [Symbol]                         The result if found, or None otherwise
	 */
	def find ( target : String ) : Option [ Symbol ] = {
		// Traverse through all the symbols
		for ( symbol <- symbols ) {
			// If the identifier equals the current symbol name
			if ( symbol.identifier.equals ( target ) ) {
				// Return the symbol
				return Some ( symbol )
			}
		}
		// Return None otherwise
		return None
	}

	/**
	 * This method takes in a string identifier and an abstract node value and binds them together
	 * using the symbol class.  It then inserts it into the symbol table.  If the identifier is
	 * already binded in the scope table, then it will be replaced by this new one.
	 * @param       String              target              The identifier binded to the symbol
	 * @param       AbstractNode        value               The value to bind with the identifier
	 */
	def insert ( identifier : String, value : AbstractNode ) = {
		// Check to see if the symbol exists in the current scope
		if ( exists ( identifier ) ) {
			// Update the value of the symbol
			find ( identifier ).foreach { i => i.node = value }
		}
		// If it doesn't, then create a new one
		else {
			// Append a new symbol to the scope
			symbols = symbols :+ new Symbol ( identifier, value )
		}
	}

	/**
	 * This function is strictly a helper function that helps with the display function. It returns
	 * the max size for the identifier and the scope name.  It is used simply for formatting
	 * purposes.
	 * @return      Int                                     length ( Scope Name + Identifier )
	 */
	def maxVarLength : Int = {
		// Initialize to zero
		var max = 0
		// Loop through all symbols in scope
		for ( symbol <- symbols ) {
			// If the identifier length plus scope name is greater than recorded max
			if ( symbol.identifier.length + name.length + 1 > max ) {
				// Reassign max value
				max = symbol.identifier.length + name.length + 1
			}
		}
		// Return the max value
		max
	}

	/**
	 * This class simply prints out all they symbols in the scope onto systems out.  It tries to do
	 * this very neatly and aligns the columns based on the input integer.
	 * @param       Int                 padding             The padding to align column
	 */
	def display ( padding : Int ) = {
		// Loop through all the scopes
		for ( symbol <- symbols ) {
			// Print out each symbol
			val id = symbol.identifier.padTo ( padding - name.length - 1, " " ).mkString ("")
			val node = symbol.node.toString
			println ( Color.colorize (f"    #c$name#d $id  #c→#d  $node") )
		}

	}

}