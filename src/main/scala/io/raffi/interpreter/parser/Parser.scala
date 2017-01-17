package io.raffi.interpreter.parser

import io.raffi.interpreter.ast.{ AbstractNode, Application, Function, Variable }

/**
 * This parser class uses the lexer to tokenize the passed input string and match it to the
 * constructed CFL for an untyped lambda calculus parser.  Along the way, it builds an AST that is
 * saved in the root variable.  Any time a new data string is parsed, it is suggested that a new
 * instance of the parser is initiated.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter.parser
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>

 * @param           String              data                The value of the target string to parse
 */
class Parser ( data : String ) {

	// Initialize the lexer instance with the passed data
	private var lexer = new Lexer ( data )

	// Use the lexer to get the first token
	private var current = lexer.nextToken ()

	// Use the lexer to get the next token
	private var next = lexer.nextToken ()

	// Initialize an array of types that are considered to be a possible terminal
	private var terminals = Array ( Type.LAMBDA, Type.VARIABLE, Type.LEFT_PARENTHESIS )

	// Start parsing, and save the root AST node
	var root = expression ( false )
	// Make sure that EOT is matched after expression is parsed successfully
	matchToken ( Type.EOT )

	/**
	 * This is the start of our CFL.  It takes in a flag that specifies if we return EXPRESSION if
	 * flag is false, or EXPRESSION' if it is true.  These two derivative cases were combined only
	 * to make the class simpler.  The CFL definitions can be found below.
	 *
	 * EXPRESSION       := ( EXPRESSION ) APPLICATION | VARIABLE APPLICATION | FUNCTION APPLICATION
	 * EXPRESSION'      := ( EXPRESSION ) | VARIABLE | FUNCTION
	 *
	 * @param           Boolean         fromApp                 Are we calling this from APPLICATION
	 * @return          AbstractNode                            A node in the AST
	 */
	private def expression ( fromApp : Boolean ) : AbstractNode = {
		// Check to see if it is a function
		if ( current.equals ( Type.LAMBDA ) ) {
			// Call the function derivative and check for application
			var func = function
			return if ( !fromApp ) application ( func ) else func
		}
		// Check to see if it is a wrapped expression
		else if ( current.equals ( Type.LEFT_PARENTHESIS ) ) {
			// Match the parenthesis, match expression, and check for application
			matchToken ( Type.LEFT_PARENTHESIS )
			var exp = expression ( false )
			matchToken ( Type.RIGHT_PARENTHESIS )
			return if ( !fromApp ) application ( exp ) else exp
		}
		// Otherwise it must be a variable, check for application after matching
		else {
			// Save the variable into the AST node
			var variable = new Variable ( current.getValue )
			// Match the variable token
			matchToken ( Type.VARIABLE )
			// Check for application
			return if ( !fromApp ) application ( variable ) else variable
		}
	}

	/**
	 * This method abstracts to be the derivative that matches a lambda function definition.  The
	 * CFL definitions can be found below.
	 *
	 * FUNCTION         := lambda VARIABLE . EXPRESSION
	 *
	 * @return          AbstractNode                            A Function node in the AST
	 */
	private def function : Function = {
		// Match the lambda token
		matchToken ( Type.LAMBDA )
		// Initialize variables array
		var variables = Array [Variable] ()
		// Match at least one variable token
		do {
			// Append variable to variable array
			variables = variables :+ new Variable ( current.getValue )
			// Match the variable and dot
			matchToken ( Type.VARIABLE )
		}
		// Be greedy and match as much variables as possible
		while ( current.getType.equals ( Type.VARIABLE ) )
		// Finally match the dot token
		matchToken ( Type.DOT )
		// Match the function body
		var body = expression ( false )
		// Return the function AST node
		return new Function ( variables, body )
	}

	/**
	 * This method abstracts to be the derivative that matches a potential function application.
	 * The CFL definitions can be found below.  Note that ';' is just used to represent epsilon.
	 *
	 * APPLICATION      := EXPRESSION' APPLICATION | ;
	 *
	 * @return          AbstractNode                            A node in the AST
	 */
	private def application ( lhs : AbstractNode ) : AbstractNode = {
		// Check to see if we an match any terminals (from expression)
		if ( terminals.contains ( current.getType ) ) {
			// Match expression, check if there are more with application
			var rhs = expression ( true )
			return application ( new Application ( lhs, rhs ) )
		}
		return lhs
	}

	/**
	 * This function simply takes in an expected token type and compares it to the current one.  If
	 * they match, then the current and next token are replaced with the next ones.  Otherwise, an
	 * exception is thrown because of either syntactic or semantic errors.
	 * @param           Type.Value          expected            The expected type for current
	 * @throws          ParseException                          If types don't match
	 */
	private def matchToken ( expected : Type.Value ) = {
		// Check to see if the token matches expected
		if ( current.equals ( expected ) ) {
			// Advance the token
			current = next
			next = lexer.nextToken ()
		}
		// Otherwise, throw exception
		else {
			// Throw a new parse exception
			throw new ParseException ( expected, current )
		}
	}

}