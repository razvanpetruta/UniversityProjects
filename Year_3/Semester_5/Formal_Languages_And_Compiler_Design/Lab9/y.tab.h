/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton interface for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     INT = 258,
     CHAR = 259,
     STRING = 260,
     VECTOR = 261,
     IF = 262,
     ELSE = 263,
     WHILE = 264,
     READ = 265,
     WRITE = 266,
     PLUS = 267,
     MINUS = 268,
     TIMES = 269,
     DIV = 270,
     MOD = 271,
     EQUAL = 272,
     NOT_EQUAL = 273,
     LESS_THAN = 274,
     LESS_THAN_OR_EQUAL = 275,
     GREATER_THAN = 276,
     GREATER_THAN_OR_EQUAL = 277,
     ASSIGN = 278,
     LEFT_PARENTHESIS = 279,
     RIGHT_PARENTHESIS = 280,
     LEFT_BRACKET = 281,
     RIGHT_BRACKET = 282,
     LEFT_BRACE = 283,
     RIGHT_BRACE = 284,
     SEMICOLON = 285,
     IDENTIFIER = 286,
     INT_CONSTANT = 287,
     STRING_CONSTANT = 288
   };
#endif
/* Tokens.  */
#define INT 258
#define CHAR 259
#define STRING 260
#define VECTOR 261
#define IF 262
#define ELSE 263
#define WHILE 264
#define READ 265
#define WRITE 266
#define PLUS 267
#define MINUS 268
#define TIMES 269
#define DIV 270
#define MOD 271
#define EQUAL 272
#define NOT_EQUAL 273
#define LESS_THAN 274
#define LESS_THAN_OR_EQUAL 275
#define GREATER_THAN 276
#define GREATER_THAN_OR_EQUAL 277
#define ASSIGN 278
#define LEFT_PARENTHESIS 279
#define RIGHT_PARENTHESIS 280
#define LEFT_BRACKET 281
#define RIGHT_BRACKET 282
#define LEFT_BRACE 283
#define RIGHT_BRACE 284
#define SEMICOLON 285
#define IDENTIFIER 286
#define INT_CONSTANT 287
#define STRING_CONSTANT 288




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE yylval;

