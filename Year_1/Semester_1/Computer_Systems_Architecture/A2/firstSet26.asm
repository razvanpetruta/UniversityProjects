bits 32 
global start

extern exit 
import exit msvcrt.dll  

segment data use32 class=data

segment code use32 class=code
start:
; 26. 3 - 4
    mov AL, 3 ; Add 3 to the register
    sub AL, 4 ; Subtract 4 from the AL register that contains 3

    push dword 0 
    call [exit] 
