bits 32

global start        

extern exit              
import exit msvcrt.dll    
                          
segment data use32 class=data
        source dw 57h, 34h, 55h, 22h, 23h
        len equ ($ - source) / 2 ;we need the size in words
        two db 2
        destination times len db 0
    
segment code use32 class=code
    start:
        mov esi, source ; store the FAR address of the source
        mov edi, destination ; store the FAR address of the destination
        cld
        mov ecx, len
        
        my_loop:
            lodsw ; in ax we'll have the word from source
            div byte[two]
            mov al, ah ; we need the remainder from the division by 2
            stosb
        loop my_loop
    
        push dword 0
        call [exit] 
