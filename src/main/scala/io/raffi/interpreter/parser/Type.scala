package io.raffi.interpreter.parser

/**
 * This class is an enumeration class, that specifies the types of tokens that we can encounter in
 * our language.  Each are assigned a string literal for the toString function.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.parser
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
protected object Type extends Enumeration {
	// Initialize all the enumerations
	val LAMBDA =            Value ("LAMBDA")
	val DOT =               Value ("DOT")
	val VARIABLE =          Value ("VARIABLE")
	val LEFT_PARENTHESIS =  Value ("LEFT_PARENTHESIS")
	val RIGHT_PARENTHESIS = Value ("RIGHT_PARENTHESIS")
	val UNKNOWN =           Value ("UNKNOWN")
	val EOT =               Value ("EOT")
}