bits 32 
global start

extern exit 
import exit msvcrt.dll  

segment data use32 class=data
        a db 10 ; a = 10
        b db 5  ; b = 5
        c db 2  ; c = 2

segment code use32 class=code
start:
    ; (a + a) - (b + b) - c
    
    ; a + a
    mov AL, [a] ; move in AL the value of a
    add AL, [a] ; add in AL the value of add
    
    ; b + b
    mov AH, [b] ; move in AH the value of b
    add AH, [b] ; add in AH the value of b
    
    ; (a + a) - (b + b)
    sub AL, AH ; subtract the value stored in AH from AL
    
    ; (a + a) - (b + b) - c
    sub AL, [c]

    push dword 0 
    call [exit] 
