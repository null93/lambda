package io.raffi.interpreter.parser

import io.raffi.interpreter.Color

/**
 * This class is a simple data structure that abstracts a token type.  It holds values such as the
 * kind of token, the value of the matched token, and the index where to find the start of the token
 * in the original data string.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.parser
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 * @param           Type.Value      kind                    The kind of token to initialize to
 * @param           String          value                   The value of the token
 * @param           Int             index                   The index of the start of the token
 */
protected class Token ( kind : Type.Value, value : String, index : Int )  {

	/**
	 * This function takes in a Type enum and returns if it the internally saved type matches the
	 * passed one.  It returns a Boolean based on the answer.
	 * @param       Type.Value      other                   The type to test against
	 * @return      Boolean
	 */
	def equals ( other : Type.Value ) : Boolean = {
		// Return if they are equal
		return other == kind
	}

	/**
	 * This method is a simple get method that will return the internally saved token type.
	 * @return      Type.Value                              The internally saved type enum
	 */
	def getType : Type.Value = {
		// Return the kind
		return kind
	}

	/**
	 * This method is a simple get method that will return the internally saved token value.
	 * @return      String                                  The value of the token
	 */
	def getValue : String = {
		// Return the value
		return value
	}

	/**
	 * This function is overridden, and it returns a custom string that represents all the data
	 * saved with the token data structure.
	 * @return      String                                  The string to return
	 * @override
	 */
	override def toString () : String = {
		// Make a string from the data
		return Color.colorize (f"#r$kind#d '$value' @ $index")
	}

}