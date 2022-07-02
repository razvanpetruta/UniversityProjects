bits 32

global _my_min
    
segment code public code use32
_my_min:
    push ebp
    mov ebp, esp
    
    mov eax, [ebp + 8]
    mov ebx, [ebp + 12]
    
    cmp eax, ebx
    jge .ebx_is_greater
    jmp .over
    .ebx_is_greater:
        mov eax, ebx
    .over:
    
    mov esp, ebp
    pop ebp
    
    ret