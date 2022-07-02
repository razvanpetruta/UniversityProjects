bits 32 

global start        

extern exit, scanf              
import exit msvcrt.dll 
import scanf msvcrt.dll   
                          
segment data use32 class=data
    n dd -1
    arr resd 100
    new_arr resd 100
    int_format db "%d", 0
    ten dd 10

segment code use32 class=code
    start:
        ; read n
        push dword n
        push dword int_format
        call [scanf]
        add esp, 4 * 2
        
        ; read the string of numbers
        mov esi, arr
        mov ecx, [n]
        .loop_1:
            push ecx
        
            ; read a number at a time
            push dword esi ; first index in the array containing the elements
            push dword int_format
            call [scanf]
            add esp, 4 * 2
            
            add esi, 4 ; go to the next index
            
            pop ecx
            loop .loop_1
            
        mov esi, arr
        mov edi, new_arr
        mov ecx, [n]
        .loop_2:
            mov ax, [esi]
            mov dx, [esi + 2]
            add esi, 4
            mov bx, 0 ; in bx we will compute the sum of the even digits
            .loop_3:
                div word [ten]
                ; dx = the last digits
                test dx, 1
                jz .is_even
                jmp .not_even
                .is_even:
                    add bx, dx
                .not_even:
                cwd
                cmp ax, 0
                jne .loop_3
            ; save the sum
            mov [edi], bx
            add edi, 2
            
            loop .loop_2
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
        
        
        
