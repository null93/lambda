package io.raffi.interpreter.ast

import io.raffi.interpreter.{ Color, Settings }
import io.raffi.interpreter.scope.{ SymbolTable }

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
 * @param           String              value               The string identifier to save internally
 */
class Variable ( value : String ) extends AbstractNode {

	// Redefining as var for transparency and mutability
	var variable = value

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and compare the value of the AST nodes.  This is a
	 * deep traversal and consists of deep comparisons of the values of the AST nodes.
	 * @return      Boolean                                 Are the trees equal in value?
	 * @override
	 */
	override def equals ( target : AbstractNode ) : Boolean = {
		// Match against the node type
		target match {
			case targetVar : Variable => {
				// Return if the symbol value's are equal
				variable.equals ( targetVar.variable )
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
		// Check to see if the color setting is on and if the variable is a free one
		if ( Settings.color && !SymbolTable.existsNotInGlobal ( variable ) ) {
			// Return the variable in color form
			return Color.colorize (f"#g$variable#d")
		}
		// Otherwise return a plain string
		return toString
	}

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and creates an value based identical tree.  It then
	 * returns the cloned node as a result.
	 * @return      AbstractNode                            The root of the cloned copy
	 * @override
	 */
	override def clone : AbstractNode = new Variable ( variable )

	/**
	 * This method recursively calls this method on all children nodes within our AST.  This method
	 * was designed to traverse through the AST, and comprise a string of the contents that the AST
	 * represents into string value.  Effectively we are turning the AST back into it's syntactic
	 * form.
	 * @return      String                                  AST in syntactic string form
	 * @override
	 */
	override def toString : String = variable

}