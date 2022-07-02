bits 32 
global start

extern exit 
import exit msvcrt.dll  

segment data use32 class=data
        a db 2
        b db 3
        c db 1
        d dw 10

segment code use32 class=code
start:
    ; d + [(a + b) * 5 - (c + c) * 5]

    ; a + b
    mov AL, [a] ; move in AL the value of a
    add AL, [b] ; add in AL the value of bits
    
    ; (a + b) * 5
    mov AH, 5
    mul AH
    
    ; move the content of AX in BX
    push AX
    pop BX
    
    ; c + c
    mov AL, [c]
    add AL, [c]
    
    ; (c + c) * 5
    mov AH, 5
    mul AH
    
    ; (a + b) * 5 - (c + c) * 5
    sub BX, AX
    
    ; (a + b) * 5 - (c + c) * 5 + d
    add BX, [d]
    
    ; move the result in AX
    push BX
    pop AX
    
    push dword 0 
    call [exit] 
