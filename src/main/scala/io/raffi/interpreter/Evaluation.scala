package io.raffi.interpreter

import io.raffi.interpreter.ast.{ AbstractNode, Application, Function, Variable }
import io.raffi.interpreter.scope.{ SymbolTable }
import scala.collection.mutable.ArrayBuffer

/**
 * This class simply takes in a root node, and evaluates it until it is in normal head form.  It is
 * simply a collection of methods that are needed to manipulate the AST and come up with an end
 * result.  For each input, a new evaluation instance needs to be instantiated.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 * @param           AbstractNode        input               The root node to evaluate
 */
class Evaluation ( input : AbstractNode ) {

	// Initialize the number of steps
	var steps = 0

	// Initialize a history array, to see the application history
	var history = ArrayBuffer [ String ] ()

	// Initialize the root node to be the passed input
	var root = input

	// Run the initialization method to kick things off
	init

	/**
	 * This method kicks everything off, and it continues until a solved solution is achieved.  It
	 * is guaranteed to finish evaluation, because of the properties of Normal-Order Reduction.
	 */
	def init = {
		// Print out the input
		println ( Color.colorize ( "#rα#d   " + answer.highlighted ) )
		// If there is a function bound variable simplification
		if ( root.simplify ) {
			// Print out the new form
			println ( Color.colorize ( "#rα#d   " + root.highlighted ) )
		}
		// While we still have things to solve, solve them
		while ( solve ) {}
		// Check to see if history option is on
		if ( Settings.history && history.length > 0 ) {
			// Print history header
			println ( Color.colorize ("\n    #cApplication History#d (lowest is newest)") )
			// Loop through all the history items
			for ( item <- history ) {
				// Print a history line
				println ( "    " + item )
			}
		}
		// Print the number of reduction steps that were required
		println ( Color.colorize (f"\n    Reduced in #c$steps#d reduction step(s)") )
	}

	/**
	 * This method attempts to solve the input.  If it preformed a beta reduction step, then true is
	 * returned, otherwise false is returned.
	 * @return      Boolean                                 Did we solve anything?
	 */
	def solve : Boolean = {
		// First resolve symbols in symbol table
		var result = resolve
		// Save the old instance
		var old = root.clone
		// Let the apply function decide what happens
		root = root.apply ( history )
		// Print out any changes
		if ( !old.equals ( root ) ) {
			// Print out a beta transformation
			println ( Color.colorize ( "#rβ#d   " + root.highlighted ) )
			// Increment the number of steps
			steps = steps + 1
		}
		if ( root.applicable && old.equals ( root ) ) {
			// Print out the final expression and print warning
			println ( Color.colorize ( "#rε#d   " + root.highlighted ) )
			println ( Color.colorize ("\n    #rHalting#d, due to infinite expansion") )
		}
		// Return if anything changed
		result || !old.equals ( root )
	}

	/**
	 * This method attempts to resolve all the symbols.  This takes care of the base case of if the
	 * root node is a variable.  Otherwise the resolve function calls the visitor in the AST nodes
	 * recursively.
	 * @return      Boolean                                 Did we resolve anything?
	 */
	def resolve : Boolean = {
		// Match against the root node type
		root match {
			case value : Variable => {
				// Look up the variable in the symbol table
				SymbolTable.lookup ( value.variable ) match {
					case Some ( symbol ) => {
						// If a symbol is found and the tree of the symbol is not equal to the root
						if ( !root.equals ( symbol.node ) ) {
							// Save the symbol saved in the symbol table to be the new root
							root = symbol.node.clone
							// Print out an alpha transformation
							println ( Color.colorize ( "#rα#d   " + root.highlighted ) )
							// Return the result of the resolve function recursively
							return resolve
						}
						// Return false by default
						false
					}
					case None => false
				}
			}
			case _ => {
				// If it is not a variable (base case) then check if the root is resolved
				if ( root.resolve ) {
					// Print the highlighted version after resolving
					println ( Color.colorize ( "#rα#d   " + root.highlighted ) )
					// Return the resolve function recursively (or true because we did something)
					return resolve || true
				}
				// Return false by default
				false
			}
		}
	}

	/**
	 * This function simply returns the root node, after the expression is evaluated.
	 * @return      AbstractNode                           The answer after alpha and beta reduction
	 */
	def answer : AbstractNode = root

}