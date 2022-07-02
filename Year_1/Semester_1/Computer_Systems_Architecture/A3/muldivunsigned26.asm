bits 32
global start

extern exit 
import exit msvcrt.dll

segment data use32 class=data
        a dw 10
        b db 20
        c dw 4
        d dd 1000
        x dq 100000
        
segment code use32 class=code
start:
    ;(a * a + b / c - 1) / (b + c) + d - x unsigned
    mov EAX, 0
    ; b + c
    mov AL, [b]
    add AX, [c]
    mov CX, AX
    
    ; b / c
    mov AL, [b]
    mov AH, 0
    mov DX, 0
    div word [c]
    
    ; b / c - 1
    sub AX, 1
    mov EBX, EAX
    
    ; a * a
    mov AX, [a]
    mul word [a]
    
    push DX
    push AX
    pop EAX
    
    ; (a * a + b / c - 1)
    add EAX, EBX
    
    push EAX
    pop BX
    pop DX
    
    div word CX
    
    mov EDX, 0
    
    add EAX, [d]
    sub EAX, [x]
    sbb EDX, [x+4]

    push dword 0
    call [exit]