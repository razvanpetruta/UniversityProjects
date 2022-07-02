bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    s db 1, 4, 2, 3, 8, 4, 9, 5
    len equ $ - s ; the length of the string
    max_even_pos db 0 ; for the maximum on the even positions
    min_odd_pos db 127 ; for the minimum on the odd positions

segment code use32 class=code
start:
    ; A byte string S is given. Obtain the maximum of the elements found on the even positions and the minimum of the elements found on the odd positions of S
    ; Ex
    ; S: 1, 4, 2, 3, 8, 4, 9, 5
    ; max_even_pos = 9
    ; min_odd_pos = 3
    
    ; for(int i = 0; i < len(s); i++)
    ; {
    ;     if(i % 2 == 0)
    ;     {
    ;         if(s[i] > max_even_pos)
    ;             max_even_pos = s[i];
    ;     }
    ;     else
    ;     {
    ;         if(s[i] < min_odd_pos)
    ;             min_odd_pos = s[i];
    ;     }
    ; }
    
    mov ecx, len ; we move in ecx the length in order to make the loop
    mov esi, 0
    
    jecxz end_for ; in case our ecx is 0
    
    for_loop:
        mov al, [s + esi] ; the current number
        test esi, 01h ; ZF will be 1 if even, 0 if odd
        jz even_pos ; jump to the even position handling block
        ; odd position block
        odd_pos:
            cmp al, [min_odd_pos]
            jl update_min ; check if we have a minimum
            jmp break_min ; if not, don't execute update_min
            ; our jump if it is minimum
            update_min:
                mov [min_odd_pos], al
            break_min:
            jmp break_if ; don't execute even position block
        ; even position block
        even_pos:
            cmp al, [max_even_pos]
            jg update_max ; check if we have a maximum
            jmp break_max ; if not, don't execute the update_max
            update_max:
                mov [max_even_pos], al
            break_max:
        ;
        break_if:
        inc esi
    loop for_loop
        
    end_for:
    push dword 0
    call [exit]