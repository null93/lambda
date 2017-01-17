package io.raffi.interpreter.parser

import io.raffi.interpreter.Color

/**
 * This class is used to throw a custom exception from the Parser class instance.  It is unique
 * because it can also determine the difference between syntactic and semantic errors.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.parser
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 * @param           String          excepted                The given value of
 */
class ParseException ( expected : Type.Value, current : Token ) extends Exception {
	// Initialize message to empty
	var message = ""
	// Match against some cases to add informative messages
	( expected, current.getType ) match {
		case ( Type.RIGHT_PARENTHESIS, _ ) => message = "Closing parentheses do not match opening parentheses"
		case ( Type.DOT, _ ) => message = "Function declaration is missing '.'"
		case ( Type.VARIABLE, _ ) => message = "Expecting an identifier, but none found"
		case ( _, Type.UNKNOWN ) => message = "This symbol is not supported in this language"
		case _ => message = ""
	}
	// Print out and colorize the given parameters onto the screen
	println ( Color.colorize (
		f"    #rSyntactic Error:#d Expected [#r$expected#d], but received $current.  $message."
	))
}