bits 32

global start        

extern exit              
import exit msvcrt.dll    
                          
segment data use32 class=data
        r dd 10101110010011b
        t dd 10010111111010001b
        q dd 0
    
segment code use32 class=code
    start:
        ; Given 2 doublewords R and T. Compute the doubleword Q as follows:
        ; (1) the bits 0-6 of Q are the same as the bits 10-16 of T
        ; (2) the bits 7-24 of Q are the same as the bits 7-24 of (R XOR T).
        ; (3) the bits 25-31 of Q are the same as the bits 5-11 of R.
        
        mov EBX, 0
        
        ; (1)
        mov EAX, [t]
        and EAX, 11111110000000000b;
        
        mov CL, 10
        shr EAX, CL
        or EBX, EAX
        
        ; (2)
        ; r xor t
        mov EAX, [r]
        xor EAX, [t]
        
        ; take only the bits from 7 to 24
        and EAX, 1111111111111111110000000b
        or EBX, EAX
        
        ; (3)
        mov EAX, [r]
        and EAX, 111111100000b
        mov CL, 20
        shl EAX, CL
        
        or EBX, EAX
        
        mov [q], EBX
        
        ;       r = 0000 0000 0000 0000 0010 1011 1001 0011
        ;       t = 0000 0000 0000 0001 0010 1111 1101 0001
        
        ; r xor t = 0000 0000 0000 0001 0000 0100 0100 0010
        
        ;     EBX = 1011 1000 0000 0001 0000 0100 0100 1011 = B801044B
    
        push dword 0
        call [exit] 