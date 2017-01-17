package io.raffi.interpreter

/**
 * This singleton object simply stores a sessions configuration.  It is initialized with default
 * settings.  It contains functions for toggling different options and printing a list of the
 * current configurations.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
object Settings {

	// Initially we will not show the history
	var history = false

	// Initially we will not color free variables
	var color = false

	// Initially we will not be in debug mode
	var debug = false

	/**
	 * This method takes in a boolean flag and based on the flag state, it either returns "on" in
	 * green or "off" in red if true or false, respectively.
	 * @param       Boolean             value               The state to represent
	 * @return      String                                  The color format string based on state
	 */
	private def state ( value : Boolean ) : String = {
		// If it is true, then green.  Otherwise, red
		if ( value ) "#gon#d" else "#roff#d"
	}

	/**
	 * This function toggles the current state of the toggle variable.
	 */
	def toggleHistory = {
		// Change the state
		history = !history
		// Print out the change
		println ("")
		println ( Color.colorize ( "    :history  #c→  " + state ( history ) ) )
		println ("")
	}

	/**
	 * This function toggles the current state of the color variable.
	 */
	def toggleColor = {
		// Change the state
		color = !color
		// Print out the change
		println ("")
		println ( Color.colorize ( "    :color    #c→  " + state ( color ) ) )
		if ( color ) println ( Color.colorize ("\n    Free variables are #ggreen#d") )
		println ("")
	}

	/**
	 * This function toggles the current state of the debug variable.
	 */
	def toggleDebug = {
		// Change the variables that are part of debug
		debug = !debug
		history = debug
		color = debug
		// Print out the current configuration
		print
	}

	/**
	 * This function simply prints out the current configuration of the states of the variables.
	 */
	def print = {
		// Print extra line for style
		println ("")
		// Print the state of all the variables
		println ( Color.colorize ( "    :debug    #c→  " + state ( debug ) ) )
		println ( Color.colorize ( "    :color    #c→  " + state ( color ) ) )
		println ( Color.colorize ( "    :history  #c→  " + state ( history ) ) )
		// If the color is on, print a message
		if ( color ) println ( Color.colorize ("\n    Free variables are #ggreen#d") )
		// Print empty line for style
		println ("")
	}

}