bits 32 
global start

extern exit 
import exit msvcrt.dll  

segment data use32 class=data
        a dw 12
        b dw 2
        c dw 8
        d dw 6

segment code use32 class=code
start:
    ; (a + c) - (b + b + d)
    
    ; b + b + d
    mov AX, [b] ; move in AX the value of bits
    add AX, [b]
    add AX, [d]
    
    ; move the content of AX in BX
    push AX
    pop BX
    
    ; a + c
    mov AX, [a]
    add AX, [c]
    
    ; (a + c) - (b + b + d)
    sub AX, BX

    push dword 0 
    call [exit] 
