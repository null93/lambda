package io.raffi.interpreter.parser

/**
 * The Lexer class is responsible for taking in a string as input from the constructor and then
 * tokenizing the input.  This will ensure that we will be able to parse the grammar more
 * efficiently with the Parser class.  This class is to be used within the Parser class exclusively.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.parser
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 *
 * @param           data            String              The string to run through the lexer
 */
class Lexer ( data : String ) {

	// This is the current expression size
	private val size = data.length ();

	// This is the alphabet that defines a variable
	private val variable = ( ( 'a' to 'z' ) ++ ( 'A' to 'Z' ) ++ ( '0' to '9' ) ).toSet

	// This is the current index that we are parsing
	var index = 0;

	/**
	 * This method simply returns the next token in the data stream that was passed as a string.  It
	 * calculates the next token on the fly, when the request is made.  If no more tokens are left,
	 * then the EOT token will be returned on every call indefinitely.
	 * @return      Token                               The next token is returned
	 */
	def nextToken () : Token = {
		// Initialize the value for the token
		var value = ""
		var integer = ""
		// Loop through while the index is smaller then the length
		while ( index < size ) {
			// Initialize the substring of the data based on index
			var current = data.substring ( index, size )
			// Initialize the current character
			var char = current ( 0 )
			// Check to see if lambda can be parsed
			if ( current.startsWith ( "lambda" ) && value.equals ("") ) {
				// Return the lambda token
				return new Token ( Type.LAMBDA, "lambda", increment ( 6 ) )
			}
			// We will add the option to match the lambda symbol as well
			else if ( char == 'λ' && value.equals ("") ) {
				// Return the lambda token
				return new Token ( Type.LAMBDA, "λ", increment ( 1 ) )
			}
			// We will add the option to match the \ symbol as well for lambda
			else if ( char == '\\' && value.equals ("") ) {
				// Return the lambda token
				return new Token ( Type.LAMBDA, "\\", increment ( 1 ) )
			}
			// Check to see if we are matching a variable
			else if ( variable.contains ( char ) ) {
				// Increment index and append char to value
				value += char
				increment ( 1 )
			}
			// Check to see if we are done matching the variable name
			else if ( !variable.contains ( char ) && !value.equals ("") ) {
				// Return a variable token
				return new Token ( Type.VARIABLE, value, index - value.length () )
			}
			// Attempt to match on the current indexes character
			else {
				char match {
					case '('  => return new Token ( Type.LEFT_PARENTHESIS, "(", increment ( 1 ) )
					case ')'  => return new Token ( Type.RIGHT_PARENTHESIS, ")", increment ( 1 ) )
					case '.'  => return new Token ( Type.DOT, ".", increment ( 1 ) )
					case ' '  => increment ( 1 )
					case '\t' => increment ( 1 )
					case '\n' => increment ( 1 )
					case _    => return new Token ( Type.UNKNOWN, char + "", increment ( 1 ) )
				}
			}
		}
		// Check to see if there is a value that needs to be returned
		if ( !value.equals ("") ) {
			return new Token ( Type.VARIABLE, value, index - value.length () )
		}
		// By default return the EOT
		return new Token ( Type.EOT, "End-Of-Token", index )
	}

	/**
	 * This is a private helper function that helps the nextToken method.  It is used to make the
	 * code in said function look cleaner.  It increments the internally saved index, and returns
	 * the old index value.  This is primarily used to pass in the index into a function, at the
	 * same time incrementing it (All in one line).
	 * @param       Int             amount              Increment index with this amount
	 * @return      Int                                 The old index before increment function call
	 */
	private def increment ( amount : Int ) : Int = {
		// Save the original index
		val original = index
		// Increment the index
		index += amount
		// Return the original value
		return original
	}

}