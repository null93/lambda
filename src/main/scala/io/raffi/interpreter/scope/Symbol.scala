package io.raffi.interpreter.scope

import io.raffi.interpreter.ast.{ AbstractNode }

/**
 * This class simply binds two variables together into a scope entry.  Consider it a row in a table.
 * Variable names are changed in order to access them outside of the class and so they were mutable.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.scope
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 * @param           String            name              The identifier binded to the value
 * @param           AbstractNode      value             The value that the identifier is binded to
 */
class Symbol ( name : String, value : AbstractNode ) {
	// Redefining as var for transparency and mutability
	var identifier = name
	var node = value
}