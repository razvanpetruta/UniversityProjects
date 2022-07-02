; we need to avoid multiple inclusion of this file
%ifndef _PARITY_ASM_ ; if _PARITY_ASM_ is not defined
%define _PARITY_ASM_ ; then we define it

; procedure definition
parity:
    mov ebx, [esp + 4]
    test ebx, 1
    jz is_even
    mov eax, 1
    jmp over
    is_even:
        mov eax, 0
    over:
    ret 4
%endif