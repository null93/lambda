package io.raffi.interpreter

/**
 * This singleton object simply stores the escape characters that will color the terminal output
 * when it is printed.  It also has a method that will colorize a string given a simple format.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
object Color {

	// Initialize terminal escape characters for coloring text
	val red = "\033[31m"
	val green = "\033[32m"
	val yellow = "\033[33m"
	val blue = "\033[34m"
	val purple = "\033[35m"
	val cyan = "\033[36m"
	val default = "\033[0m"

	/**
	 * Whenever you pass a string and use '#' followed by the first letter of the color, it will simply
	 * replace it.  For example, "Hello #rWorld#d!" will simply print out "Hello World!" with "World" in
	 * a red font color.
	 * @param       String              target              The uncolored formatted string
	 * @return      String                                  Colorized string with escaped characters
	 */
	def colorize ( target : String ) : String = {
		// Save the original string
		var result = target
		// Replace all the format strings
		result = result.replaceAll ( "#r", red )
		result = result.replaceAll ( "#g", green )
		result = result.replaceAll ( "#y", yellow )
		result = result.replaceAll ( "#b", blue )
		result = result.replaceAll ( "#p", purple )
		result = result.replaceAll ( "#c", cyan )
		result = result.replaceAll ( "#d", default )
		// Return the result
		return result
	}

}