%{
    #include <stdio.h>
    #include <stdlib.h>   
    #include <string.h>

    int yylex(void);
    int yyerror(char *s);
%}

%token INT;
%token CHAR;
%token STRING;
%token VECTOR;
%token IF;
%token ELSE;
%token WHILE;
%token READ;
%token WRITE;

%token PLUS;
%token MINUS;
%token TIMES;
%token DIV;
%token MOD;

%token EQUAL;
%token NOT_EQUAL;
%token LESS_THAN;
%token LESS_THAN_OR_EQUAL;
%token GREATER_THAN;
%token GREATER_THAN_OR_EQUAL;

%token ASSIGN;

%token LEFT_PARENTHESIS;
%token RIGHT_PARENTHESIS;
%token LEFT_BRACKET;
%token RIGHT_BRACKET;
%token LEFT_BRACE;
%token RIGHT_BRACE;
%token SEMICOLON;

%token IDENTIFIER;
%token INT_CONSTANT;
%token STRING_CONSTANT;

%%
program : statement_list {printf("program\n");}

statement_list : statement statement_list {printf("statement statement_list\n");}
               | statement {printf("statement\n");}

statement : simple_statement SEMICOLON {printf("simple_statement ;\n");}
          | struct_statement {printf("struct_statement\n");}

simple_statement : declaration_statement {printf("declaration_statement\n");}
                 | assignment_statement {printf("assignment_statement\n");}
                 | io_statement {printf("io_statement\n");}

struct_statement : if_statement {printf("if_statement\n");}
                 | while_statement {printf("while_statement\n");}

declaration_statement : type IDENTIFIER {printf("declaration_statement\n");}

type : INT {printf("int\n");}
     | CHAR {printf("char\n");}
     | STRING {printf("string\n");}
     | VECTOR LEFT_BRACKET INT_CONSTANT RIGHT_BRACKET {printf("vector[?]\n");}

assignment_statement : IDENTIFIER ASSIGN expression {printf("assignment_statement\n");}
                     | IDENTIFIER LEFT_BRACKET IDENTIFIER RIGHT_BRACKET ASSIGN expression {printf("vector[?]\n");}

io_statement : READ LEFT_PARENTHESIS IDENTIFIER RIGHT_PARENTHESIS {printf("read(?)\n");}
             | WRITE LEFT_PARENTHESIS IDENTIFIER RIGHT_PARENTHESIS {printf("write(?)\n");}
             | WRITE LEFT_PARENTHESIS STRING_CONSTANT RIGHT_PARENTHESIS {printf("write(?)\n");}
             | WRITE LEFT_PARENTHESIS INT_CONSTANT RIGHT_PARENTHESIS {printf("write(?)\n");}

if_statement : IF LEFT_PARENTHESIS condition RIGHT_PARENTHESIS LEFT_BRACE statement_list RIGHT_BRACE {printf("if_statement\n");}
             | IF LEFT_PARENTHESIS condition RIGHT_PARENTHESIS LEFT_BRACE statement_list RIGHT_BRACE ELSE LEFT_BRACE statement_list RIGHT_BRACE {printf("if_statement else\n");}

while_statement : WHILE LEFT_PARENTHESIS condition RIGHT_PARENTHESIS LEFT_BRACE statement_list RIGHT_BRACE {printf("while_statement\n");}

expression : expression PLUS term {printf("expression + term\n");}
           | expression MINUS term {printf("expression - term\n");}
           | term {printf("term\n");}

term : term TIMES factor {printf("term * factor\n");}
     | term DIV factor {printf("term / factor\n");}
     | term MOD factor {printf("term % factor\n");}
     | factor {printf("factor\n");}

factor : LEFT_PARENTHESIS expression RIGHT_PARENTHESIS {printf("(expression)\n");}
       | INT_CONSTANT {printf("int_constant\n");}
       | STRING_CONSTANT {printf("string_constant\n");}
       | IDENTIFIER {printf("identifier\n");}
       | IDENTIFIER LEFT_BRACKET IDENTIFIER RIGHT_BRACKET {printf("vector[?]\n");}

condition : expression EQUAL expression {printf("expression == expression\n");}
          | expression NOT_EQUAL expression {printf("expression != expression\n");}
          | expression LESS_THAN expression {printf("expression < expression\n");}
          | expression LESS_THAN_OR_EQUAL expression {printf("expression <= expression\n");}
          | expression GREATER_THAN expression {printf("expression > expression\n");}
          | expression GREATER_THAN_OR_EQUAL expression {printf("expression >= expression\n");}
%%

extern FILE *yyin;

yyerror(char *s)
{	
	printf("%s\n",s);
}

int main(int argc, char **argv)
{
    if (argc != 2)
    {
        printf("Usage: ./result.exe <input file>\n");
        exit(1);
    }

    FILE *fp = fopen(argv[1], "r");
    if (fp == NULL)
    {
        printf("Cannot open file %s\n", argv[1]);
        exit(1);
    }

    yyin = fp;
    yyparse();
    fclose(fp);

    return 0;
}