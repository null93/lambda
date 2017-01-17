package io.raffi.interpreter.ast

import io.raffi.interpreter.{ Color, Settings }
import io.raffi.interpreter.scope.{ SymbolTable }
import scala.collection.mutable.ArrayBuffer

/**
 * This class inherits from the AbstractNode class and it overrides some methods that the parent
 * class defines.  All visitors in this class are AST node type specific and are designed to work in
 * a recursive manner.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.ast
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 * @param           Array[Variable]     vars                Array of binded variables
 * @param           AbstractNode        node                The body of the function declaration
 */
class Function ( vars : Array [ Variable ], node : AbstractNode ) extends AbstractNode {

	// Redefining as var for transparency and mutability
	var bounded = vars
	var body = node

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and specifically target the Function node type.
	 * Within the Function node type, this function aims to change all multi-variable parameters
	 * into a chained node link that contains Function nodes each with one variable parameter.
	 * @return      Boolean                                 Did we simplify the AST functions list?
	 * @override
	 */
	override def simplify : Boolean = {
		var flag = false
		while ( bounded.length > 1 ) {
			var popped = bounded.last
			bounded = bounded.dropRight ( 1 )
			body = new Function ( Array ( popped ), body )
			flag = true
		}
		flag || body.simplify
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and resolve any free variables based on the values
	 * in the symbol table.  Note that when entering a function definition, we enter a new scope
	 * in the symbol table, and insert the binded variables relative to that function definition
	 * into the symbol table.  This way we are able to resolve symbols in a more elegant manner.
	 * @return      Boolean                                 Did we resolve any symbols?
	 * @override
	 */
	override def resolve : Boolean = this.resolve ( true )

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and resolve any free variables based on the values
	 * in the symbol table.  Note that when entering a function definition, we enter a new scope
	 * in the symbol table, and insert the binded variables relative to that function definition
	 * into the symbol table.  This way we are able to resolve symbols in a more elegant manner.
	 * This method is used as a helper to either open scopes for the binded variables or not to
	 * based on the passed boolean flag.
	 * @param       Boolean             scoped              Open scope & insert symbols for binded?
	 * @return      Boolean                                 Did we resolve any symbols?
	 */
	def resolve ( scoped : Boolean ) : Boolean = {
		// Check to see if we should open scope and insert symbols
		if ( scoped ) {
			// Enter a new scope
			SymbolTable.enterScope
			// Loop through bounded variables
			for ( bound <- bounded ) {
				// Insert the values into the symbol table
				SymbolTable.insert ( bound.variable, bound.clone )
			}
		}
		// Initially set the resolved flag to true
		var resolved = true
		// Match the body type
		body match {
			case variable : Variable => {
				// Loop up symbol in the symbol table
				SymbolTable.lookup ( variable.variable ) match {
					case Some ( symbol ) => {
						// If symbol is found and the values are equal
						if ( variable.equals ( symbol.node ) ) {
							// Set resolved to false
							resolved = false
						}
						// If the symbol is found and differ in value
						else {
							// Set the new body to be the value of the symbol
							body = symbol.node.clone
						}
					}
					case None => resolved = false
				}
			}
			case _ => {
				// If it isn't a variable then return the result of a resolve visitor to the body
				resolved = body.resolve
			}
		}
		// If the scoped flag is set
		if ( scoped ) {
			// Exit the symbol table
			SymbolTable.exitScope
		}
		// Return the resolved flag
		resolved
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and apply an untyped lambda calculus expression
	 * into a function definition.  Effectively setting the bounded variable in the function
	 * definition to the expression that is being applied to the definition.  The returned result is
	 * an AST node, which can change if a successful application occurs, effectively unwrapping the
	 * application and attempting to resolve the final result when no more changes are seen.  For
	 * this detection of this behavior, the clone method is used to copy the AST before calling this
	 * method and then the equals method is used to compare the AST structure to determine if any
	 * changes have been made.
	 * @param       ArrayBuffer[String]     history         The history array to chain applications
	 * @return      AbstractNode                            The new root node, after application
	 * @override
	 */
	override def apply ( history : ArrayBuffer [String] ) : AbstractNode = {
		// Recursively apply and save new root node
		body = body.apply ( history )
		// Return this instance
		this
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and detect if an application is possible within
	 * the the tree at any point in the tree.
	 * @return      Boolean                                 Is an application possible?
	 * @override
	 */
	override def applicable : Boolean = {
		// Return the result of if the body is applicable
		body.applicable
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and compare the value of the AST nodes.  This is a
	 * deep traversal and consists of deep comparisons of the values of the AST nodes.
	 * @return      Boolean                                 Are the trees equal in value?
	 * @override
	 */
	override def equals ( target : AbstractNode ) : Boolean = {
		// Match against the target node
		target match {
			case function : Function => {
				// If the type matches, check the bounded variables and if tree is equal
				bounded.deep == function.bounded.deep && body.equals ( function.body )
			}
			case _ => false
		}
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and comprise a string of the contents that the AST
	 * represents into string value.  Effectively we are turning the AST back into it's syntactic
	 * form.  Of course this is with one exception, syntax highlighting will occur and certain
	 * symbols will be highlighted based on my developer choice.
	 * @return      String                                  AST in syntax highlighted string form
	 * @override
	 */
	override def highlighted : String = {
		// Initialize the default result
		var result = Color.colorize ( "(#cλ#d" + bounded.mkString (" ") + "." + body.highlighted + ")" )
		// If the color setting is on
		if ( Settings.color ) {
			// Enter the scope table
			SymbolTable.enterScope
			// Loop though all bounded variable
			for ( variable <- bounded ) {
				// Insert them as symbols
				SymbolTable.insert ( variable.variable, variable )
			}
			// Then reconstruct the string
			result = Color.colorize ( "(#cλ#d" + bounded.mkString (" ") + "." + body.highlighted + ")" )
			// Exit the scope
			SymbolTable.exitScope
		}
		// Return the result
		result
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and creates an value based identical tree.  It then
	 * returns the cloned node as a result.
	 * @return      AbstractNode                            The root of the cloned copy
	 * @override
	 */
	override def clone : AbstractNode = new Function ( bounded.clone, body.clone )

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and comprise a string of the contents that the AST
	 * represents into string value.  Effectively we are turning the AST back into it's syntactic
	 * form.
	 * @return      String                                  AST in syntactic string form
	 * @override
	 */
	override def toString : String = "(λ" + bounded.mkString (" ") + "." + body.toString + ")"

}