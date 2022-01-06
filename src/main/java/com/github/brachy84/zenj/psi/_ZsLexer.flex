package com.github.brachy84.zenj.psi;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.github.brachy84.zenj.psi.ZsTypes.*;

%%

%{
  public _ZsLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _ZsLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

LINE_COMMENT="//".*
BLOCK_COMMENT="/"\*([^*]|\*+[^*/])*(\*+)?"/"
DOUBLE_QUOTED_STRING=\"([^\\\"\r\n]|\\[^\r\n])*\"?
SINGLE_QUOTED_STRING='([^\\'\r\n]|\\[^\r\n])*'?
PREPROCESSOR=#[^ \t\n\x0B\f\r]*( [^ \t\n\x0B\f\r]*)?
DIGITS=-?[0-9]+
FLOATING_POINT=-?[0-9]+\.[0-9]+
EXP_NUMBER=-?[0-9]+\.[0-9]+[Ee]-?[0-9]+
IN=in|has
EOL=\R
IDENTIFIER=[:jletter:] [:jletterdigit:]*

%%
<YYINITIAL> {
  {WHITE_SPACE}               { return WHITE_SPACE; }

  "("                         { return L_ROUND_BRACKET; }
  ")"                         { return R_ROUND_BRACKET; }
  "<"                         { return L_ANGLE_BRACKET; }
  ">"                         { return R_ANGLE_BRACKET; }
  "[ "                        { return L_SQUARE_BRACKET; }
  " ]"                        { return R_SQUARE_BRACKET; }
  "{ "                        { return L_CURLY_BRACKET; }
  " }"                        { return R_CURLY_BRACKET; }
  "="                         { return EQUAL; }
  "!"                         { return EXCL; }
  "~"                         { return TILDE; }
  "?"                         { return QUEST; }
  ":"                         { return COLON; }
  "+"                         { return PLUS; }
  "-"                         { return MINUS; }
  "*"                         { return ASTERISK; }
  "/"                         { return DIV; }
  "|"                         { return OR; }
  "&"                         { return AND; }
  "^"                         { return XOR; }
  "%"                         { return PERC; }
  "@"                         { return AT; }
  "#"                         { return HASH; }
  ";"                         { return SEMICOLON; }
  ","                         { return COMMA; }
  "."                         { return DOT; }
  ".."                        { return DOTDOT; }
  "=="                        { return EQEQ; }
  "!="                        { return NOT_EQUAL; }
  "<="                        { return LESS_EQUAL; }
  ">="                        { return GREATER_EQUAL; }
  "any"                       { return ANY; }
  "bool"                      { return BOOL; }
  "byte"                      { return BYTE; }
  "short"                     { return SHORT; }
  "int"                       { return INT; }
  "long"                      { return LONG; }
  "float"                     { return FLOAT; }
  "double"                    { return DOUBLE; }
  "string"                    { return STRING; }
  "function"                  { return FUNCTION; }
  "to"                        { return TO; }
  "void"                      { return VOID; }
  "as"                        { return AS; }
  "version"                   { return VERSION; }
  "if"                        { return IF; }
  "else"                      { return ELSE; }
  "for"                       { return FOR; }
  "while"                     { return WHILE; }
  "return"                    { return RETURN; }
  "import"                    { return IMPORT; }
  "break"                     { return BREAK; }
  "var"                       { return VAR; }
  "val"                       { return VAL; }
  "static"                    { return STATIC; }
  "global"                    { return GLOBAL; }
  "null"                      { return NULL; }
  "true"                      { return TRUE; }
  "false"                     { return FALSE; }

  {LINE_COMMENT}              { return LINE_COMMENT; }
  {BLOCK_COMMENT}             { return BLOCK_COMMENT; }
  {DOUBLE_QUOTED_STRING}      { return DOUBLE_QUOTED_STRING; }
  {SINGLE_QUOTED_STRING}      { return SINGLE_QUOTED_STRING; }
  {PREPROCESSOR}              { return PREPROCESSOR; }
  {DIGITS}                    { return DIGITS; }
  {FLOATING_POINT}            { return FLOATING_POINT; }
  {EXP_NUMBER}                { return EXP_NUMBER; }
  {IN}                        { return IN; }
  {EOL}                       { return EOL; }
  {IDENTIFIER}                { return IDENTIFIER; }

}

[^] { return BAD_CHARACTER; }
