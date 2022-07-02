bits 32
global start

extern exit
import exit msvcrt.dll

segment data use32 class=data
    A db "2345678888888"
    len_a equ $ - A
    B db "1288"
    len_b equ $ - B
    CC times len_a + len_b db 0
    
    
segment code use32 class=code
start:
    ; Two strings of bytes A and B are given. Parse the shortest string of those two and build a third string C as follows:
    ; up to the length of the shortest string C contains the largest element of the same rank from the two strings
    ; then, up to the length of the longest string C will be filled with 1 and 0, alternatively.
    
    mov esi, A
    mov edi, B
    
    mov edx, 0
    
    mov eax, len_a
    mov ebx, len_b
    cmp eax, ebx
    jle a_shorter
    
    mov ecx, ebx
    my_loop_b:
        lodsb
        mov bl, [edi]
        inc edi
        cmp al, bl
        jge al_greater_1
        mov [CC + edx], bl
        jmp over1
        al_greater_1:
            mov [CC + edx], al
        over1:
        inc edx
    loop my_loop_b
    mov ebx, len_a
    cmp edx, ebx
    jge finish
    my_loop_b1:
        mov al, dl
        and al, 1
        mov [CC + edx], al
        inc edx
    cmp edx, ebx
    jl my_loop_b1
    jmp over
    
    a_shorter:
        mov ecx, eax
        my_loop_a:
            lodsb
            mov bl, [edi]
            inc edi
            cmp al, bl
            jge al_greater_2
            mov [CC + edx], bl
            jmp over2
            al_greater_2:
                mov [CC + edx], al
            over2:
            inc edx
        loop my_loop_a
        
        mov ebx, len_b
        cmp edx, ebx
        jge finish
        my_loop_a1:
            mov al, dl
            and al, 1
            mov [CC + edx], al
            inc edx
        cmp edx, ebx
        jl my_loop_a1
    over:
    finish:
    
    push dword 0
    call [exit]