bits 32

global start        

extern exit, printf            
import exit msvcrt.dll  
import printf msvcrt.dll  
                          
segment data use32 class=data
    sir dd 1234A678h, 12785634h, 1A4D3C26h
    new_sir resw 3
    nr_of_ones db 0
    int_format db "%d", 0
    
segment code use32 class=code
    start:
        mov ecx, 3 ; len of the sir
        mov esi, sir ; the address of the source string
        mov edi, new_sir ; the address of the new_sir
        
        cld
        
        .loop_1:
            lodsd
            ; in eax we'll have the one doubleword at a time
            
            ; we want to form the word in bx, bl + bh
            shr eax, 8
            mov bl, al
            shr eax, 16
            mov bh, al
            
            ; save the built word in the new string
            mov [edi], bx
            add edi, 2
            
            loop .loop_1
        
        mov esi, new_sir ; the address of the new_sir containing the necessary words
        mov ecx, 3
        
        .loop_2:
            lodsw
            ; in ax we'll have a word at a time
            
            ; we want to count the number of ones
            ; possible ones in a word = 16
            
            push ecx
            
            mov ecx, 16
            
            .loop_3:
                test ax, 1
                jnz .is_one
                jmp .over
                .is_one:
                    inc byte [nr_of_ones]
                .over:
                shr ax, 1
                loop .loop_3
            
            pop ecx
            loop .loop_2
            
        mov eax, 0
        mov al, [nr_of_ones]
        push eax
        push dword int_format
        call [printf]
        add esp, 4 * 2
        
        push    dword 0      
        call    [exit]  
             
