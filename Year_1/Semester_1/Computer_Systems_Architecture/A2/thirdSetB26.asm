bits 32 
global start

extern exit 
import exit msvcrt.dll  

segment data use32 class=data
        b db 1
        c db 4
        e dw 2
        f dw 8
        g dw 4

segment code use32 class=code
start:
    ; [(e + f - g) + (b + c) * 3] / 5
    
    ; b + c
    mov AL, [b]
    add AL, [c]
    
    ; (b + c) * 3
    mov AH, 3
    mul AH
    
    ; e + f - g
    add AX, [e]
    add AX, [f]
    sub AX, [g]
    
    ; AX / 5
    mov BL, 5
    div BL

    push dword 0 
    call [exit] 
