package io.raffi.interpreter

import io.raffi.interpreter.parser.{ Parser, ParseException }
import io.raffi.interpreter.ast.{ Application, Function, Variable }
import io.raffi.interpreter.scope.{ SymbolTable }
import scala.tools.jline.{ TerminalFactory }
import scala.tools.jline.console.{ ConsoleReader }

/**
 * This class is the main class that is used to run the interpreter.  In this class, all the overall
 * logic is handled here. It also implements an intermediate parser in order to run the interpreter
 * commands.
 * @version         1.0.0
 * @university      University of Illinois at Chicago
 * @course          CS474 - Object Oriented Languages & Environments
 * @category        Project #04 - Untyped Lambda Calculus Interpreter
 * @package         io.raffi.interpreter
 * @author          Rafael Grigorian
 * @license         GNU Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 */
object Interpreter extends App {

	// Flag to keep track of if we are still in the running loop
	private var running = true

	// Instance of the terminal object
	private val terminal = TerminalFactory.create;

	// Instance of the console reader
	private val console = new ConsoleReader

	// User input initialized to empty
	private var input = ""

	// Variable (For setting variables) in our interpreter
	private var variable = ""

	// Display welcome message
	welcome
	// Kick off main loop
	loop

	/**
	 * This is the default loop that is used in the logical loop.  This function will loop forever
	 * until the input running flag is set to false.  All input and intermediate parsing is done
	 * here.
	 */
	private def loop = {
		// Loop through until flag is unset
		while ( running ) {
			// Get user input
			input = console.readLine ( Color.colorize ("#cλ#d > ") ).trim
			// Initialize variable variable
			variable = ""
			// Check to see if we are trying to initialize a variable
			if ( input.matches ("[a-zA-Z0-9]{1,}[ \t]*=[ \t]*.*") ) {
				// Check to see if the RHS is not empty
				if ( input.split ("=").length == 2 ) {
					// Extract variable name, and redefine the input string
					variable = input.split ("=") ( 0 ).trim
					input = input.split ("=") ( 1 ).trim
				}
				// Otherwise, set the input to empty string
				else {
					// Print the error
					println ( Color.colorize ("\n    #rError:#d No expression found on the RHS.\n") )
					// Set input to empty
					input = ""
				}
			}
			// Check to see if we are setting a variable
			if ( !variable.equals ("") && input.startsWith (":") ) {
				evaluate
			}
			// Otherwise allow for menu help
			else {
				// Determine what to do based on command
				input match {
					case ""                         => print ("")
					case ":global"                  => SymbolTable.print
					case ":reset"                   => SymbolTable.reset
					case ":settings"                => Settings.print
					case ":history"                 => Settings.toggleHistory
					case ":color"                   => Settings.toggleColor
					case ":debug"                   => Settings.toggleDebug
					case ":help"                    => help
					case ":info"                    => info
					case ":about"                   => about
					case ":clear"                   => clear
					case ":quit" | ":exit"          => exit
					case s if s.startsWith (":")    => unknown ( s )
					case _                          => evaluate
				}
			}
		}
	}

	/**
	 * This method is called when the intermediate parser detects that an untyped lambda calculus
	 * expression is passed.  It kicks off a parser instance and parsed the input.  It also saves
	 * the expression result into the symbol table is an assignment is detected.
	 */
	private def evaluate = {
		// Print another new line for style ;)
		println ()
		// Attempt to parse data
		try {
			// Parse the input expression
			var parser = new Parser ( input )
			// Pass if to a new evaluation class
			var evaluation = new Evaluation ( parser.root )
			// Check to see if we are initializing a variable
			if ( !variable.equals ("") ) {
				// Insert the answer into the symbol table
				SymbolTable.insert ( variable, evaluation.answer )
			}
		}
		// Catch any exceptions that are thrown
		catch {
			// In case it's a parse exception, then let it print error message
			case e : ParseException => print ("")
		}
		// Print another new line for style ;)
		println ()
	}

	/**
	 * This function simply prints out an error message if an incorrect ":" call is detected
	 * @param       String              s                   The command that was passed
	 */
	private def unknown ( s : String ) = println (
		Color.colorize (f"\n    Unknown command #r$s#d, type #g:help#d for a list of commands\n")
	)

	/**
	 * This function simply clears the screen by printing new lines enough to clear the height of
	 * the user's console.
	 */
	private def clear = {
		// Print out empty lines to clear terminal
		for ( i <- 1 to console.getTerminal.getHeight ) { println () }
	}

	/**
	 * This method simply prints out the welcome splash screen when the interpreter is launched.
	 */
	private def welcome = {
		// Define an array of lines
		var lines = Array (
			"#c .~~~~.#d                                     #d",
			"#c !_.   \\#d                                    #d",
			"#c    \\   \\#r      UNTYPED                      #d",
			"#c     \\   \\#r      LAMBDA                      #d",
			"#c      \\   \\#r      CALCULUS                   #d",
			"#c      /    \\#r      INTERPRETER               #d",
			"#c     /      \\#d                               #d",
			"#c    /   /\\   \\#d      By: Rafael Grigorian    #d",
			"#c   /   /  \\   \\#d                             #d",
			"#c  /   /    \\   \\#d                            #d",
			"#c !___/      \\___!#d      #g:help#d for assistance "
		)
		// Print out the welcome message in to middle of the screen
		for ( i <- 1 to ( terminal.getHeight / 2 ) - ( lines.length / 2 ) ) { println ("") }
		var padding = Array.fill ( ( terminal.getWidth - lines ( 0 ).length + 6 ) / 2 ) {" "}
		lines = lines.map ( line => padding.mkString ("") + line )
		lines.map ( line => println ( Color.colorize ( line ) ) )
		for ( i <- 1 to ( terminal.getHeight / 2 ) - ( lines.length / 2 ) - 1 ) { println ("") }
	}

	/**
	 * This method prints out the help menu and additional information that would be useful to the
	 * user.
	 */
	private def help = {
		// Print new line for style
		println ("")
		// Print out the settings
		println ( Color.colorize ("    :about    #c→  #dInformation about author") )
		println ( Color.colorize ("    :clear    #c→  #dClear the console") )
		println ( Color.colorize ("    :color    #c→  #dSyntax highlighting based on binding") )
		println ( Color.colorize ("    :debug    #c→  #dToggle debug settings") )
		println ( Color.colorize ("    :exit     #c→  #dTerminate the interpreter") )
		println ( Color.colorize ("    :global   #c→  #dDisplay symbols in global scope") )
		println ( Color.colorize ("    :help     #c→  #dShow this menu") )
		println ( Color.colorize ("    :history  #c→  #dToggle history after reduction") )
		println ( Color.colorize ("    :info     #c→  #dBehind the scenes description") )
		println ( Color.colorize ("    :quit     #c→  #dTerminate the interpreter") )
		println ( Color.colorize ("    :reset    #c→  #dClear symbols from symbol table") )
		println ( Color.colorize ("    :settings #c→  #dDisplay current settings") )
		// Print new line for style
		println ("")
		// Print out the meanings of the Latin symbols
		println ( Color.colorize ("    #rα#d         #c→  #dEquivalence & symbol resolution") )
		println ( Color.colorize ("    #rβ#d         #c→  #dBeta-reduction step") )
		println ( Color.colorize ("    #rε#d         #c→  #dAnswer before infinite expansion") )
		// Print new line for style
		println ("")
		// Print out additional information
		println ( Color.colorize ("    For information about how everything works, type #g:info#d") )
		// Print new line for style
		println ("")
		// Print out how the input should look like
		println ( Color.colorize ("    To set variables in this interpreter, please type in the variable name followed by an") )
		println ( Color.colorize ("    equals sign (#r=#d) and finally followed by an untyped lambda calculus expression.") )
		println ( Color.colorize ("    An example would be as follows: #rvar = lambda x.x#d.  Space surrounding the equals") )
		println ( Color.colorize ("    sign is of course optional.  This interpreter also supports your input for lambda to") )
		println ( Color.colorize ("    be as follows: #rlambda#d, #rλ#d, #r\\#d.") )
		// Print new line for style
		println ("")
	}

	/**
	 * This method simply prints out the information about the author.
	 */
	private def about = {
		// Print new line for style
		println ("")
		// Print out the author information
		println ( Color.colorize ("    Name      #c→  #dRafael Grigorian") )
		println ( Color.colorize ("    Email     #c→  #dme@raffi.io") )
		println ( Color.colorize ("    Class     #c→  #dObject Oriented Languages & Environments") )
		println ( Color.colorize ("    College   #c→  #dUniversity of Illinois @ Chicago") )
		// Print new line for style
		println ("")
	}

	/**
	 * This method simply prints out the extended information about the interpreter.
	 */
	private def info = {
		// Print new line for style
		println ("")
		// Print out the information
		println ( Color.colorize ("    This interpreter uses the CFG found below to parse user input.  This language insures") )
		println ( Color.colorize ("    precedence for applications over functions, as well as insuring left-associativity") )
		println ( Color.colorize ("    for functions and right-associativity for applications.") )
		// Print new line for style
		println ("")
		// Print out the CFL
		println ( Color.colorize ("    <EXP>     #c→  #d( <EXP> ) <APP> | <VAR> <APP> | <FUN> <APP>") )
		println ( Color.colorize ("    <TER>     #c→  #d( <EXP> ) | <VAR> | <FUN>") )
		println ( Color.colorize ("    <FUN>     #c→  #dlambda <VAR> . <EXP>") )
		println ( Color.colorize ("    <APP>     #c→  #d<TER> <APP> | ε") )
		println ( Color.colorize ("    <VAR>     #c→  #d[a-zA-Z0-9]+") )
		// Print new line for style
		println ("")
	}

	/**
	 * This method simply exits the event loop by setting up the running flag to false, and restores
	 * the terminal environmental variables.
	 */
	private def exit = {
		running = false
		console.getTerminal.restore
	}

}