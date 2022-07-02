bits 32
global start

extern exit 
import exit msvcrt.dll

segment data use32 class=data
        a db 10
        b dw 200
        c dd 1000
        d dq 20000
        
segment code use32 class=code
start:
    ; (c - d - a) + (b + b) - (c + a)
    
    ; c + a
    ; we need to convert a from binary into doubleword
    ; step 1 -> convert a from binary into word, AX = a
    mov AL, [a]
    cbw
    ; step 2 -> convert a from word into doubleword, EAX = a
    cwde
    
    add EAX, [c]
    mov EBX, EAX
    
    ; b + b
    mov AX, [b]
    add AX, [b]
    
    ; we need to convert to doubleword, EAX = b + b
    cwde 
    
    ; (b + b) - (c + a)
    sub EAX, EBX
    mov EBX, EAX 
    
    ; c - d - a
    mov AL, [a]
    cbw
    cwde
    ; c - a
    mov EDX, EAX
    mov EAX, [c]
    sub EAX, EDX
    ; c - a + (b + b) - (c + a)
    add EAX, EBX
    
    cdq
    ; EDX:EAX - d
    sub EAX, [d]
    sbb EDX, [d+4]

    push dword 0
    call [exit]