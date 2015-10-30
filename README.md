    Predictive Syntax Analyzer
The allowed alphabet is a set of upper and lower case letters, the symbols { '(', ')', '+', '-', '*', '/' } and the
compound symbol '->' that separates the body of the production and its head.
Currently the analyzer only supports single character lexemes. There is an example in the configFiles/grammar.txt file
provided.

The grammar input must be placed in the configFiles/grammar.txt file.
The input to be processed must be placed in the configFiles/input.txt file.

The derivation table will be shown in the standard output.

The configFiles/config.txt may contain two flags:
    - SYNC_ERRORS: Error synchronization flag.
    - PRINT_OUTPUT: Prints the derivation table to a file.
