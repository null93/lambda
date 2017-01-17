package io.raffi.interpreter.ast

import io.raffi.interpreter.Color
import scala.collection.mutable.ArrayBuffer

/**
 * This class is a parent class that provides basic functionality that we want each child class that
 * inherits from this class to have.  Methods are overridden to change the functionality.  The model
 * that was used for this AST, was to include each AST 'visitor' as a separate function which visits
 * the tree nodes recursively rather than having another separate visitor class/package.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.ast
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
class AbstractNode {

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and specifically target the Function node type.
	 * Within the Function node type, this function aims to change all multi-variable parameters
	 * into a chained node link that contains Function nodes each with one variable parameter.
	 * @return      Boolean                                 Did we simplify the AST functions list?
	 */
	def simplify : Boolean = false

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and resolve any free variables based on the values
	 * in the symbol table.  Note that when entering a function definition, we enter a new scope
	 * in the symbol table, and insert the binded variables relative to that function definition
	 * into the symbol table.  This way we are able to resolve symbols in a more elegant manner.
	 * @return      Boolean                                 Did we resolve any symbols?
	 */
	def resolve : Boolean = false

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
	 */
	def apply ( history : ArrayBuffer [String] ) : AbstractNode = this

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and detect if an application is possible within
	 * the the tree at any point in the tree.
	 * @return      Boolean                                 Is an application possible?
	 */
	def applicable : Boolean = false

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and compare the value of the AST nodes.  This is a
	 * deep traversal and consists of deep comparisons of the values of the AST nodes.
	 * @return      Boolean                                 Are the trees equal in value?
	 */
	def equals ( target : AbstractNode ) : Boolean = false

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and comprise a string of the contents that the AST
	 * represents into string value.  Effectively we are turning the AST back into it's syntactic
	 * form.  Of course this is with one exception, syntax highlighting will occur and certain
	 * symbols will be highlighted based on my developer choice.
	 * @return      String                                  AST in syntax highlighted string form
	 */
	def highlighted : String = "null"

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and creates an value based identical tree.  It then
	 * returns the cloned node as a result.
	 * @return      AbstractNode                            The root of the cloned copy
	 * @override
	 */
	override def clone : AbstractNode = this

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and comprise a string of the contents that the AST
	 * represents into string value.  Effectively we are turning the AST back into it's syntactic
	 * form.
	 * @return      String                                  AST in syntactic string form
	 * @override
	 */
	override def toString : String = "null"

}