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
 * @param           AbstractNode        node1               The LHS of the application
 * @param           AbstractNode        node2               The RHS of the application
 */
class Application ( node1 : AbstractNode, node2 : AbstractNode ) extends AbstractNode {

	// Redefining as var for transparency and mutability
	var lhs = node1
	var rhs = node2

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and specifically target the Function node type.
	 * Within the Function node type, this function aims to change all multi-variable parameters
	 * into a chained node link that contains Function nodes each with one variable parameter.
	 * @return      Boolean                                 Did we simplify the AST functions list?
	 * @override
	 */
	override def simplify : Boolean = {
		// Get the result of the LHS and the RHS
		var resultLHS = lhs.simplify
		var resultRHS = rhs.simplify
		// Return true if either are true
		resultLHS || resultRHS
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
	override def resolve : Boolean = {
		// Get the result of the LHS and the RHS
		var resultLHS = resolve ( true )
		var resultRHS = resolve ( false )
		// Return true if either are true
		resultLHS || resultRHS
	}

	/**
	 * This method is a helper method that will evaluate the LHS or the RHS.  The distinction
	 * between the LHS and the RHS can be accomplished by passing in a flag as a parameter.
	 * @param       Boolean             left                Is it the LHS, or the RHS?
	 */
	def resolve ( left : Boolean ) = {
		// Set the resolved flag to true initially
		var resolved = true
		// Based on the flag, save the target (LHS || RHS)
		var target = if ( left ) lhs else rhs
		// Match the node
		target match {
			case variable : Variable => {
				// Look up if the variable is in the symbol table
				SymbolTable.lookup ( variable.variable ) match {
					case Some ( symbol ) => {
						// If symbol is found and the values are equal
						if ( target.equals ( symbol.node ) ) {
							// Set the flag to false
							resolved = false
						}
						// If it is the left node that we are evaluating
						else if ( left ) {
							// Save the symbol value as the LHS
							lhs = symbol.node.clone
						}
						// If it is the right node that we are evaluating
						else {
							// Save the symbol value as the RHS
							rhs = symbol.node.clone
						}
					}
					case None => resolved = false
				}
			}
			case _ => {
				// Set the resolved result as the target resolve visit recursively
				resolved = target.resolve
			}
		}
		// Return if we resolved anything
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
		// Match the LHS
		lhs match {
			case function : Function => {
				// Initialize a history item
				var historyItem = function.bounded (0).variable + " → " + rhs.toString
				// If there is prior history and the last entry is equal to the current
				if ( history.length > 0 && history.last.equals ( historyItem ) ) {
					// Return itself (trees will match but will be applicable)
					return this
				}
				// Append to the history
				history += historyItem
				// Enter a new scope
				SymbolTable.enterScope
				// Loop through all bounded variables
				for ( bound <- function.bounded ) {
					// Add their values to the symbol table
					SymbolTable.insert ( bound.variable, bound.clone )
				}
				// Insert the last bounded variable and assign it with the RHS
				SymbolTable.insert ( function.bounded.last.variable, rhs.clone )
				// Resolve the function with no scoping
				function.resolve ( false )
				// Exit the scope
				SymbolTable.exitScope
				// Return the body of the function as the new root
				function.body
			}
			case application : Application => {
				// Apply the LHS and save the new node
				lhs = lhs.apply ( history )
				// Return this node, since it was not unwrapped
				this
			}
			case _ => {
				// Apply the RHS and save the new node
				rhs = rhs.apply ( history )
				// Return this node, since it was not unwrapped
				this
			}
		}
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and detect if an application is possible within
	 * the the tree at any point in the tree.
	 * @return      Boolean                                 Is an application possible?
	 * @override
	 */
	override def applicable : Boolean = {
		// Match against LHS
		lhs match {
			case function : Function => {
				// If its a function, then we can apply
				true
			}
			case application : Application => {
				// If it's an application, ask the LHS
				lhs.applicable
			}
			case _ => {
				// Otherwise ask the RHS
				rhs.applicable
			}
		}
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and compare the value of the AST nodes.  This is a
	 * deep traversal and consists of deep comparisons of the values of the AST nodes.
	 * @return      Boolean                                 Are the trees equal in value?
	 * @override
	 */
	override def equals ( target : AbstractNode ) : Boolean = {
		// Match against the sub-type
		target match {
			case application : Application => {
				// If the type matches self, and if the LHS & RHS sub trees are equal
				lhs.equals ( application.lhs ) && rhs.equals ( application.rhs )
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
		// Return the string with highlighted contents
		"(" + lhs.highlighted + " " + rhs.highlighted + ")"
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and creates an value based identical tree.  It then
	 * returns the cloned node as a result.
	 * @return      AbstractNode                            The root of the cloned copy
	 * @override
	 */
	override def clone : AbstractNode = new Application ( lhs.clone, rhs.clone )

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and comprise a string of the contents that the AST
	 * represents into string value.  Effectively we are turning the AST back into it's syntactic
	 * form.
	 * @return      String                                  AST in syntactic string form
	 * @override
	 */
	override def toString : String = "(" + lhs.toString + " " + rhs.toString + ")"

}